import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  @Input('open') open!: boolean;
  @Input('closeFunc') closeFunc!: () => void;
  @Input('openRegisterModal') openRegisterModal!: () => void;

  openForgotPasswordModal = false;
  constructor() { }

  ngOnInit(): void {
  }

  toggleForgotPasswordModal = () => {
    this.openForgotPasswordModal =!this.openForgotPasswordModal;
  }

}
