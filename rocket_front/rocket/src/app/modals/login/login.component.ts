import { Component, OnInit, Input } from '@angular/core';
import {FacebookLoginProvider, SocialAuthService, SocialUser} from '@abacritt/angularx-social-login';
import {Router} from '@angular/router';
import { api } from 'src/app/shared/api/api';

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

  openErrorToast = false;

  user: SocialUser | null;
  loggedIn: boolean;

  email: string;
  password: string;

  constructor(private router: Router,
    private socialAuthService: SocialAuthService) {
      this.user = null;
      this.loggedIn = false;
      this.email = '';
      this.password = '';
  }

    ngOnInit() {
      this.socialAuthService.authState.subscribe((user) => {
        this.user = user;
        this.loggedIn = (user != null);
      });
    }

  toggleForgotPasswordModal = () => {
    this.openForgotPasswordModal =!this.openForgotPasswordModal;
  }

  toggleErrorToast = () => {
    this.openErrorToast =!this.openErrorToast;
  }

  signInWithFB(): void {
    this.socialAuthService.signIn(FacebookLoginProvider.PROVIDER_ID);
  }

  onSubmit() {
    if (this.valid()) {
      let encodedData = new URLSearchParams()
      encodedData.append("username", this.email)
      encodedData.append("password", this.password)
      api.post('/api/login', encodedData)
      .then((res:any) => console.log(res))
      
      .catch((err: any) => console.log(`error: ${err}`))
    }
    else {
      this.openErrorToast = true;
    }
  }

  valid(): boolean {
    return (/^.+[@].+[\.].+$/.test(this.email)) && (/^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])[0-9a-zA-Z]{8,20}$/.test(this.password))
  }
}
