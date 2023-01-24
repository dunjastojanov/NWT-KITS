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
import { CurrentRide, UserRidingStatus } from 'src/app/interfaces/Ride';
import {
  CurrentRideAction,
  CurrentRideActionType,
} from 'src/app/shared/store/current-ride-slice/current-ride.actions';
import { http } from 'src/app/shared/api/axios-wrapper';
import { RideService } from '../ride/ride.service';

@Injectable()
export class SocketService {
  isCustomSocketOpened: boolean = false;

  constructor(
    private httpService: HttpClient,
    private store: Store<StoreType>,
    private rideService: RideService
  ) {}

  serverUrl = 'http://localhost:8443/ws';
  stompClient: any;
  isLoaded = false;

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

  sendRideRequestToPalUsingSocket(email: string) {
    console.log(email);
    // this.stompClient.send("/socket-subscriber/send/message", {}, JSON.stringify(message));
    this.httpService
      .get(
        `http://localhost:8443/api/notification/send-ride-request-invitation/${email}`
      )
      .subscribe((data: any) => {
        console.log(data);
      });
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
      .subscribe((data: any) => {
        console.log('status');
        console.log(data);
      });
  }

  openSocket() {
    if (this.isLoaded) {
      console.log('Opening socket...');

      this.isCustomSocketOpened = true;
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
    }
  }

  handleResultNotification(message: any) {
    console.log(message);
    const notifications: Notif[] = JSON.parse(message.body);
    this.store.dispatch(
      new NotificationsAction(
        NotificationsActionType.SET_NOTIFICATIONS,
        notifications
      )
    );
  }

  async handleResultRide(message: any) {
    console.log(message);
    const id: number = JSON.parse(message.body);
    console.log(id);
    const currentRide = await this.rideService.getCurrentRide(id);
    console.log(currentRide);
    if (currentRide) {
      this.store.dispatch(
        new CurrentRideAction(CurrentRideActionType.SET, currentRide)
      );
    }
  }
}
