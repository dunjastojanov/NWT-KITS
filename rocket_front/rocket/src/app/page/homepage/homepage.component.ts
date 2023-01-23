import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Params} from '@angular/router';
import {SocketService} from 'src/app/services/sockets/sockets.service';
import {PaypalService} from "../../services/paypal/paypal.service";

@Component({
  selector: 'homepage',
  templateUrl: './homepage.component.html',
  styleUrls: ['./homepage.component.css'],
})
export class HomepageComponent implements OnInit {
  constructor(
    private socketService: SocketService,
    private route: ActivatedRoute,
    private payPalService: PaypalService
  ) {
  }

  initSockets() {
    this.socketService.initializeWebSocketConnection();
  }

  openErrorToast = false;
  openSuccessToast = false;

  openNewPasswordModal: boolean = false;

  ngOnInit(): void {
    this.route.queryParams.subscribe((params) => {
      this.payPalListener(params);
      const forgottenPasswordToken = params['token'];
      if (forgottenPasswordToken) {
        this.togglePasswordModal();
      }
    });
  }

  private payPalListener(params: Params) {
    const paymentId = params['paymentId'];
    let payerId = params['PayerID'];
    if (paymentId && payerId) {
      this.payPalService.triggerPaymentExecution(paymentId, payerId).then(
        result => {
          if (result === 'Successful payment') {
            this.toggleSuccessToast();
          } else {
            this.toggleErrorToast()
          }
        }
      );
      setTimeout((args) => {
        window.location.href = 'http://localhost:4200/';
      }, 5000);
    }
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

  togglePasswordModal = () => {
    this.openNewPasswordModal = !this.openNewPasswordModal;
  }

}
