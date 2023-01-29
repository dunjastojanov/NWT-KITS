import { Component, OnInit } from '@angular/core';
import { Store } from '@ngrx/store';
import { User } from '../interfaces/User';
import { Notif } from '../interfaces/Notification';
import {
  LoggedUserAction,
  LoggedUserActionType,
} from '../shared/store/logged-user-slice/logged-user.actions';

import { StoreType } from '../shared/store/types';
import { NotificationService } from '../services/notification/notification.service';
import {
  CurrentRideAction,
  CurrentRideActionType,
} from '../shared/store/current-ride-slice/current-ride.actions';
import { Router } from '@angular/router';
import { VehicleService } from '../services/vehicle/vehicle.service';
import { SocketService } from '../services/sockets/sockets.service';

@Component({
  selector: 'navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css'],
})
export class NavbarComponent implements OnInit {
  showPayPalModal = false;
  showLoginModal = false;
  showRegisterModal = false;
  showNotifications = false;
  newNotification = false;

  notifications: Notif[] = [];
  user: User | null = null;

  constructor(
    private store: Store<StoreType>,
    private router: Router,
    private vehicleService: VehicleService
  ) {
    let loggedUserSlice = store.select('loggedUser');
    loggedUserSlice.subscribe((resData) => {
      this.user = resData.user;
    });
    this.store.select('notifications').subscribe((res) => {
      console.log(res.notifications);
      this.notifications = res.notifications;
    });
  }

  hasRole(role: string): boolean {
    return this.user !== null && this.user?.roles.indexOf(role) !== -1;
  }

  ngOnInit(): void {
    /*this.notificationService.getNotification().then(
      res => {
        this.notifications = res;
      }
    )*/
  }

  toggleLogin = (): void => {
    this.showRegisterModal = false;
    this.showLoginModal = !this.showLoginModal;
    this.showPayPalModal = false;
  };

  logout = (): void => {
    this.router.navigate(['/']).then(() => {
      if (this.hasRole('DRIVER')) {
        this.vehicleService.changeStatus('INACTIVE').then((res) => {

          alert(res);

        });
      }
    });
    this.store.dispatch(new LoggedUserAction(LoggedUserActionType.LOGOUT));
    this.store.dispatch(new CurrentRideAction(CurrentRideActionType.REMOVE));
  };

  toggleRegister = (): void => {
    this.showPayPalModal = false;
    this.showLoginModal = false;
    this.showRegisterModal = !this.showRegisterModal;
  };

  togglePayPal = (): void => {
    this.showLoginModal = false;
    this.showRegisterModal = false;
    this.showPayPalModal = !this.showPayPalModal;
  };

  toggleNotifications = (): void => {
    this.showNotifications = !this.showNotifications;
  };
}
