import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import * as Stomp from 'stompjs';
import * as SockJS from 'sockjs-client';
import { Notif } from 'src/app/interfaces/Notification';
import {
  NotificationsAction,
  NotificationsActionType,
} from 'src/app/shared/store/notifications-slice/notifications.actions';
import { StoreType } from 'src/app/shared/store/types';
import { Store } from '@ngrx/store';
import {
  CurrentRideAction,
  CurrentRideActionType,
} from 'src/app/shared/store/current-ride-slice/current-ride.actions';
import { RideService } from '../ride/ride.service';
import { User } from 'src/app/interfaces/User';
import { MessageInfo } from '../../interfaces/MessageInfo';
import {
  MessageAction,
  MessageActionType,
} from '../../shared/store/message-slice/message.actions';
import { ActiveVehicle } from 'src/app/interfaces/Vehicle';
import {
  ActiveVehiclesAction,
  ActiveVehiclesActionType,
} from 'src/app/shared/store/active-vehicles-slice/active-vehicles.actions';
import {
  CurrentRide,
  LongitudeLatitude,
  RideStatus,
  UserRidingStatus,
} from 'src/app/interfaces/Ride';
import {
  LoggedUserAction,
  LoggedUserActionType,
} from '../../shared/store/logged-user-slice/logged-user.actions';
import { ToastrService } from 'ngx-toastr';

@Injectable()
export class SocketService {
  isCustomSocketOpened: boolean = false;
  isCustomSocketOpenedNonUser: boolean = false;
  user: User | null = null;
  currentRide: CurrentRide | null = null;

  constructor(
    private httpService: HttpClient,
    private store: Store<StoreType>,
    private rideService: RideService,
    private toastr: ToastrService
  ) {
    this.store.select('loggedUser').subscribe((res) => {
      this.user = res.user;
    });
    this.store.select('currentRide').subscribe((res) => {
      this.currentRide = res.currentRide;
    });
  }

  serverUrl = 'http://localhost:8443/ws';
  stompClient: any;
  stompClientNonUser: any;
  isLoaded = false;
  isLoadedNonUser = false;

  // Funkcija za otvaranje konekcije sa serverom
  initializeWebSocketConnection() {
    let ws = new SockJS(this.serverUrl);
    this.stompClient = Stomp.over(ws);
    let that = this;

    this.stompClient.connect({}, function () {
      that.isLoaded = true;
      that.openSocket();
    });
  }

  initializeWebSocketConnectionNonUser() {
    let ws = new SockJS(this.serverUrl);
    this.stompClientNonUser = Stomp.over(ws);
    let that = this;
    this.stompClientNonUser.connect({}, function () {
      that.isLoadedNonUser = true;
      that.openSocketNonUser();
    });
  }

  sendRideRequestToPalUsingSocket(email: string) {
    // this.stompClient.send("/socket-subscriber/send/message", {}, JSON.stringify(message));
    this.httpService
      .get(
        `http://localhost:8443/api/notification/send-ride-request-invitation/${email}`
      )
      .subscribe((data: any) => {});
  }

  sendResponseOnRideRequest(
    rideId: string,
    userId: string,
    status: UserRidingStatus
  ) {
    this.httpService
      .post(`http://localhost:8443/api/ride/status/${rideId}`, {
        userId: userId,
        ridingStatus: status,
      })
      .subscribe((data: any) => {});
  }

  openSocket() {
    if (this.isLoaded) {
      console.log('Opening socket...');

      this.isCustomSocketOpened = true;
      this.stompClient.subscribe(
        '/user/queue/update-vehicle',
        (message: any) => {
          this.handleVehicleLocationUpdate(message);
        }
      );
      this.stompClient.subscribe(
        '/user/queue/notifications',
        (message: { body: string }) => {
          this.handleResultNotification(message);
        }
      );
      this.stompClient.subscribe(
        '/user/queue/rides',
        (message: { body: string }) => {
          this.handleResultRide(message);
        }
      );
      this.stompClient.subscribe(
        '/user/queue/message',
        (message: { body: object }) => {
          this.handleMessage(message);
        }
      );
      if (this.user?.roles.includes('DRIVER')) {
        this.stompClient.subscribe(
          '/user/queue/driver-status',
          (message: { body: string }) => {
            this.handleDriverStatus(message);
          }
        );
      }
    }
  }

  openSocketNonUser() {
    if (this.isLoadedNonUser) {
      console.log('Opening socket for everyone...');
      this.isCustomSocketOpenedNonUser = true;
      this.stompClientNonUser.subscribe(
        '/queue/active-vehicles',
        (message: any) => {
          this.handleActiveVehicles(message);
        }
      );
    }
  }

  handleActiveVehicles(message: any) {
    const activeVehicle: ActiveVehicle = JSON.parse(message.body);
    this.store.dispatch(
      new ActiveVehiclesAction(
        ActiveVehiclesActionType.UPDATE_ACTIVE_VEHICLE,
        activeVehicle
      )
    );
  }

  handleVehicleLocationUpdate(message: any) {
    const longLat: LongitudeLatitude = JSON.parse(message.body);
    this.store.dispatch(
      new CurrentRideAction(
        CurrentRideActionType.UPDATE_VEHICLE_LOCATION,
        longLat
      )
    );
  }

  handleResultNotification(message: any) {
    const notifications: Notif[] = JSON.parse(message.body);
    this.store.dispatch(
      new NotificationsAction(
        NotificationsActionType.SET_NOTIFICATIONS,
        notifications
      )
    );
  }

  async handleResultRide(message: any) {
    const id: number = JSON.parse(message.body);
    if (id === -1) {
      this.toastr.error('There is no available driver');
      this.store.dispatch(new CurrentRideAction(CurrentRideActionType.REMOVE));
    }
    const currentRide = await this.rideService.getCurrentRide(id);
    if (currentRide) {
      if (this.user?.roles[0] === 'CLIENT') {
        this.store.dispatch(
          new CurrentRideAction(CurrentRideActionType.SET, currentRide)
        );
      } else if (this.user?.roles[0] === 'DRIVER' && currentRide.vehicle) {
        if (currentRide.rideStatus != RideStatus.SCHEDULED) {
          this.store.dispatch(
            new CurrentRideAction(CurrentRideActionType.SET, currentRide)
          );
        } else if (
          currentRide.rideStatus == RideStatus.SCHEDULED &&
          !this.currentRide
        ) {
          this.store.dispatch(
            new CurrentRideAction(CurrentRideActionType.SET, currentRide)
          );
        }
      }
    }
    await this.rideService.onRideStatusChanged();
  }

  handleMessage(message: any) {
    const messages: MessageInfo[] = JSON.parse(message.body);
    this.store.dispatch(
      new MessageAction(MessageActionType.SET_MESSAGES, messages)
    );
  }

  handleDriverStatus(message: any) {
    const status: string = JSON.parse(message.body);
    if (this.user) {
      let user = { ...this.user, status: status };
      // this.user.status = status;
      this.store.dispatch(
        new LoggedUserAction(LoggedUserActionType.LOGIN, user)
      );
    }
  }
}
