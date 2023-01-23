import {Component, Input, OnInit} from '@angular/core';
import {UserService} from "../../services/user/user.service";

@Component({
  selector: 'app-new-password',
  templateUrl: './new-password.component.html',
  styleUrls: ['./new-password.component.css']
})
export class NewPasswordComponent implements OnInit {
  @Input('open') open!: boolean;
  @Input('open') token: string;
  @Input('closeFunc') closeFunc!: () => void;
  password: string = "";

  openErrorToast: boolean;
  openSuccessToast: boolean;


  constructor(private userService: UserService) {
  }

  ngOnInit(): void {
  }

  sendRequestForForgottenPassword() {
    const data = {
      token: this.token,
      password: this.password
    }
    if (this.isPasswordValid()) {
      this.userService.forgottenPasswordChangeConfirmation(data).then(result => {
        if (result === "Successful password update") {
          this.toggleSuccessToast();
        }else{
          this.toggleErrorToast()
        }
        setTimeout((args) => {
          window.location.href = 'http://localhost:4200/';
        }, 5000);
      })
    }
  }

  private isPasswordValid() {
    return /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])[0-9a-zA-Z]{8,20}$/.test(this.password)
  }

  toggleErrorToast = () => {
    this.openErrorToast = !this.openErrorToast;
  };

  toggleSuccessToast = () => {
    this.openSuccessToast = !this.openSuccessToast;
  };
}
