import { Injectable } from '@angular/core';
import { Store } from '@ngrx/store';
import { AxiosResponse } from 'axios';
import { CookieService } from 'ngx-cookie-service';
import { GoogleUser } from 'src/app/interfaces/GoogleUser';
import { MessageInfo } from 'src/app/interfaces/MessageInfo';
import { Notif } from 'src/app/interfaces/Notification';
import { CurrentRide } from 'src/app/interfaces/Ride';
import { User } from 'src/app/interfaces/User';
import { http, Http } from 'src/app/shared/api/axios-wrapper';
import { loggedUserToken } from 'src/app/shared/consts';
import {
  CurrentRideAction,
  CurrentRideActionType,
} from 'src/app/shared/store/current-ride-slice/current-ride.actions';
import {
  MessageAction,
  MessageActionType,
} from 'src/app/shared/store/message-slice/message.actions';
import {
  NotificationsAction,
  NotificationsActionType,
} from 'src/app/shared/store/notifications-slice/notifications.actions';
import { StoreType } from 'src/app/shared/store/types';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  http!: Http;
  constructor(
    private cookieService: CookieService,
    private store: Store<StoreType>
  ) {
    this.http = http;
  }

  public setHttp(newHttp: Http) {
    this.http = newHttp;
  }

  async registerUser(data: any): Promise<boolean> {
    try {
      await this.http.post<string>('/api/user', data);
      return true;
    } catch (err) {
      return false;
    }
  }

  async loginUser(data: any): Promise<boolean> {
    try {
      let result: AxiosResponse<string> = await this.http.post<
        string,
        URLSearchParams
      >('/api/user/login', data);
      this.setToken(result.data);
      return true;
    } catch (err) {
      return false;
    }
  }

  setToken(token: string) {
    window.localStorage.setItem(loggedUserToken, token);
    this.cookieService.set('access_token', token);
  }

  public getToken() {
    let token: string | null = window.localStorage.getItem(loggedUserToken);
    return token;
  }

  async loginGoogleUser(user: User): Promise<string> {
    const googleDto: GoogleUser = {
      id: user.id,
      email: user.email,
      firstName: user.firstName,
      lastName: user.lastName,
      profilePicture: user.profilePicture,
    };
    let result: AxiosResponse<string> = await this.http.post<string>(
      '/api/user/google',
      googleDto
    );
    return result.data;
  }

  async getUser(): Promise<User | null> {
    if (this.getToken()) {
      let result: AxiosResponse<User> = await this.http.get<User>(
        '/api/user/logged'
      );
      return <User>result.data;
    }
    return null;
  }

  async loadResources(email: string) {
    await this.loadMessages();
    await this.loadCurrentRide(email);
    await this.loadNotifications();
  }

  async loadMessages() {
    await this.http.get<MessageInfo[]>('/api/chat/message').then((result) => {
      this.store.dispatch(
        new MessageAction(MessageActionType.SET_MESSAGES, result.data)
      );
    });
  }

  async loadCurrentRide(email: string) {
    const currentRide = await this.getCurrentRide(email);
    if (currentRide) {
      this.store.dispatch(
        new CurrentRideAction(CurrentRideActionType.SET, currentRide)
      );
    }
  }

  async getCurrentRide(email: string): Promise<CurrentRide | null> {
    if (this.getToken()) {
      let result: AxiosResponse<CurrentRide | null> = await this.http.get(
        `/api/ride/currentRide/${email}`
      );
      return <CurrentRide>result.data;
    }
    return null;
  }

  async loadNotifications() {
    const notifications: Notif[] = await this.getNotification();
    this.store.dispatch(
      new NotificationsAction(
        NotificationsActionType.SET_NOTIFICATIONS,
        notifications
      )
    );
  }

  async getNotification(): Promise<any> {
    let result: AxiosResponse = await this.http.get('/api/notification');
    return result.data;
  }

  async changeStatus(
    status: 'ACTIVE' | 'INACTIVE' | 'DRIVING'
  ): Promise<string> {
    let result: AxiosResponse<string> = await this.http.put<string>(
      '/api/vehicle/' + status
    );
    return <string>result.data;
  }
}
