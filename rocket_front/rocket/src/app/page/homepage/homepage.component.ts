import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Params} from '@angular/router';
import {PaypalService} from '../../services/paypal/paypal.service';
import {ToastrService} from 'ngx-toastr';
import {Store} from "@ngrx/store";
import {StoreType} from "../../shared/store/types";
import {UserService} from "../../services/user/user.service";
import {LoggedUserAction, LoggedUserActionType} from "../../shared/store/logged-user-slice/logged-user.actions";

@Component({
  selector: 'homepage',
  templateUrl: './homepage.component.html',
  styleUrls: ['./homepage.component.css'],
})
export class HomepageComponent implements OnInit {
  constructor(
    private route: ActivatedRoute,
    private payPalService: PaypalService,
    private toastService: ToastrService,
    private store: Store<StoreType>,
    private userService: UserService
  ) {
  }

  openNewPasswordModal: boolean = false;

  token: string = '';

  ngOnInit(): void {
    this.route.queryParams.subscribe((params) => {
      if (params['paymentId'] !== undefined) {
        this.payPalListener(params);
      } else {
        const forgottenPasswordToken = params['token'];
        if (forgottenPasswordToken) {
          this.token = forgottenPasswordToken;
          this.togglePasswordModal();
        }
      }
    });
  }

  private payPalListener(params: Params) {
    const paymentId = params['paymentId'];
    let payerId = params['PayerID'];
    if (paymentId && payerId) {
      this.payPalService
        .triggerPaymentExecution(paymentId, payerId)
        .then((result) => {
          if (result === 'Successful payment') {
            this.toastService.success('Successful payment');
            this.userService.getUser().then(value => {
              if (value)
                this.store.dispatch(new LoggedUserAction(LoggedUserActionType.LOGIN, value));
            });
          } else {
            this.toastService.error('Unsuccessful payment');
          }
        });
      setTimeout(() => {
        window.location.href = 'http://localhost:4200/';
      }, 5000);
    }
  }

  togglePasswordModal = () => {
    this.openNewPasswordModal = !this.openNewPasswordModal;
  };
}
