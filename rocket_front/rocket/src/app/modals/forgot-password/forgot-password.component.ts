import {Component, Input, OnInit} from '@angular/core';
import {UserService} from "../../services/user/user.service";

@Component({
  selector: 'forgot-password',
  templateUrl: './forgot-password.component.html',
  styleUrls: ['./forgot-password.component.css']
})
export class ForgotPasswordComponent implements OnInit {
  @Input('open') open!: boolean;
  @Input('closeFunc') closeFunc!: () => void;
  email: string = "";


  constructor(private userService: UserService) {
  }

  ngOnInit(): void {
  }

  sendRequestForForgottenPassword() {
    if (this.isInputValid()) {
      this.userService.sendRequestForPassword(this.email);
    }
  }

  isInputValid() {
    return /^.+[@].+[.].+$/.test(this.email)
  }
}
