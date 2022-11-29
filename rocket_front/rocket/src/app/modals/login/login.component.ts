import { Component, OnInit, Input } from '@angular/core';
import {FacebookLoginProvider, SocialAuthService, SocialUser} from '@abacritt/angularx-social-login';
import { api } from 'src/app/shared/api/api';
import { Store } from '@ngrx/store'
import { StoreType } from 'src/app/shared/store/types';
import { LoggedUserAction, LoggedUserActionType } from 'src/app/shared/store/logged-user-slice/logged-user.actions';
import { User } from 'src/app/interfaces/User';
import { AxiosError } from 'axios';

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
  openSuccessToast = false;

  user: User | null = null;

  email: string;
  password: string;

  constructor(
    private socialAuthService: SocialAuthService,
    private store: Store<StoreType>) {
      this.email = '';
      this.password = '';
  }

    ngOnInit() {
      this.socialAuthService.authState.subscribe((socialUser: SocialUser) => {
        let user: User = {id:socialUser.id, email: socialUser.email, firstName: socialUser.firstName, lastName: socialUser.lastName, role:"client", profilePicture:socialUser.photoUrl};
        this.login(user)
      });
    }

  toggleForgotPasswordModal = () => {
    this.openForgotPasswordModal =!this.openForgotPasswordModal;
  }

  toggleErrorToast = () => {
    this.openErrorToast =!this.openErrorToast;
  }

  toggleSuccessToast = () => {
    this.openSuccessToast =!this.openSuccessToast;
  }

  signInWithFB(): void {
    this.socialAuthService.signIn(FacebookLoginProvider.PROVIDER_ID);
  }

  login(user:User) {
    this.openSuccessToast = true;
    this.store.dispatch(new LoggedUserAction(LoggedUserActionType.LOGIN, user))
    this.closeFunc();
  }

  onSubmit() {
    if (this.valid()) {
      let encodedData = new URLSearchParams()
      encodedData.append("username", this.email)
      encodedData.append("password", this.password)
      const that = this;
      /*api.post('/api/login', encodedData)
      .then((res:any) => {
        console.log(res)
      })
      .catch((err: AxiosError) => {
        that.openErrorToast = true
      })*/
      //OVO IDE U THEN
      //sa backa se dobije user koji ima roles: string[], ukoliko je len 1 onda se samo ta rola dodeli useru na frontu, u suprotnom se ponudi korisniku da izabre kao ko zeli da se prijavi i ta rola se doda useru na frontu
      let user: User = {id:"1", email: "email", firstName: "First", lastName: "Last", city:"city", phoneNumber:"phone", role:"client", profilePicture:"profilePic"};
      this.login(user);
    }
    else {
      this.openErrorToast = true;
    }
  }

  valid(): boolean {
    return (/^.+[@].+[\.].+$/.test(this.email)) && (/^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])[0-9a-zA-Z]{8,20}$/.test(this.password))
  }
}
