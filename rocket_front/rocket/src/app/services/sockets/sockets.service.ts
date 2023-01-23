import { Component, Injectable, OnInit } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import {
  HttpClient,
  HttpEvent,
  HttpHandler,
  HttpHeaders,
  HttpInterceptor,
  HttpRequest,
} from '@angular/common/http';
import * as Stomp from 'stompjs';
import * as SockJS from 'sockjs-client';
import { Notif } from 'src/app/interfaces/Notification';
import {
  NotificationsAction,
  NotificationsActionType,
} from 'src/app/shared/store/notifications-slice/notifications.actions';
import { StoreType } from 'src/app/shared/store/types';
import { Store } from '@ngrx/store';

@Injectable()
export class SocketService {
  isCustomSocketOpened: boolean = false;

  constructor(
    private httpService: HttpClient,
    private store: Store<StoreType>
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

  openSocket() {
    if (this.isLoaded) {
      console.log('Opening socket...');

      this.isCustomSocketOpened = true;
      this.stompClient.subscribe(
        '/user/queue/notifications',
        (message: { body: string }) => {
          this.handleResult(message);
        }
      );
    }
  }
  // Funkcija koja se poziva kada server posalje poruku na topic na koji se klijent pretplatio
  handleResult(message: any) {
    console.log(message);
    const notifications: Notif[] = JSON.parse(message.body);
    this.store.dispatch(
      new NotificationsAction(
        NotificationsActionType.SET_NOTIFICATIONS,
        notifications
      )
    );
  }
}
