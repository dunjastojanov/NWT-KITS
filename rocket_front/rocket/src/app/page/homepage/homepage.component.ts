import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { AxiosResponse } from 'axios';
import { http } from '../../shared/api/axios-wrapper';
import { SocketService } from 'src/app/services/sockets/sockets.service';

@Component({
  selector: 'homepage',
  templateUrl: './homepage.component.html',
  styleUrls: ['./homepage.component.css'],
})
export class HomepageComponent implements OnInit {
  constructor(
    private socketService: SocketService,
    private route: ActivatedRoute
  ) {}

  initSockets() {
    this.socketService.initializeWebSocketConnection();
  }

  openErrorToast = false;
  openSuccessToast = false;

  ngOnInit(): void {
    this.route.queryParams.subscribe((params) => {
      const paymentId = params['paymentId'];
      let payerId = params['PayerID'];
      if (paymentId && payerId) {
        this.triggerPaymentExecution(paymentId, payerId);
        setTimeout((args) => {
          window.location.href = 'http://localhost:4200/';
        }, 5000);
      }
      // do something with the parameters
    });
  }

  sendMessage() {
    this.socketService.sendMessageUsingSocket();
  }
  toggleErrorToast = () => {
    this.openErrorToast = !this.openErrorToast;
  };

  toggleSuccessToast = () => {
    this.openSuccessToast = !this.openSuccessToast;
  };

  async triggerPaymentExecution(
    paymentId: string,
    payerId: string
  ): Promise<void> {
    try {
      let data = {
        payerId: payerId,
        paymentId: paymentId,
      };
      let link: AxiosResponse<any> | void = await http
        .post<string, Object>('/api/payment/confirm', data)
        .then((value) => {
          if (value.data === 'Successful payment') {
            this.toggleSuccessToast();
          }
        });
    } catch (err) {
      this.toggleErrorToast();
    }
  }
}
