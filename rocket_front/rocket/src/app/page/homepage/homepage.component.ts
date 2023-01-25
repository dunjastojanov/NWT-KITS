import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Params} from '@angular/router';
import {SocketService} from 'src/app/services/sockets/sockets.service';
import {PaypalService} from "../../services/paypal/paypal.service";
import {ToastrService} from "ngx-toastr";

@Component({
  selector: 'homepage',
  templateUrl: './homepage.component.html',
  styleUrls: ['./homepage.component.css'],
})
export class HomepageComponent implements OnInit {
  constructor(
    private socketService: SocketService,
    private route: ActivatedRoute,
    private payPalService: PaypalService,
    private toastService:ToastrService
  ) {
  }

  initSockets() {
    this.socketService.initializeWebSocketConnection();
  }


  openNewPasswordModal: boolean = false;

  token: string = "";

  ngOnInit(): void {
    this.route.queryParams.subscribe((params) => {
      console.log(params)
      this.payPalListener(params);
      const forgottenPasswordToken = params['token'];
      if (forgottenPasswordToken) {
        this.token = forgottenPasswordToken;
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
            this.toastService.success("Successful payment");
          } else {
            this.toastService.error("Unsuccessful payment");
          }
        }
      );
      setTimeout(() => {
        window.location.href = 'http://localhost:4200/';
      }, 5000);
    }
  }

  sendMessage() {
    this.socketService.sendMessageUsingSocket();
  }


  togglePasswordModal = () => {
    this.openNewPasswordModal = !this.openNewPasswordModal;
  }

}
