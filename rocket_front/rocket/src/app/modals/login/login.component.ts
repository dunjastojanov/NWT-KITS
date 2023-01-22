import { Component, Input, OnInit } from '@angular/core';
import {
  FacebookLoginProvider,
  SocialAuthService,
  SocialUser,
} from '@abacritt/angularx-social-login';
import { Store } from '@ngrx/store';
import { StoreType } from 'src/app/shared/store/types';
import {
  LoggedUserAction,
  LoggedUserActionType,
} from 'src/app/shared/store/logged-user-slice/logged-user.actions';
import { User } from 'src/app/interfaces/User';
import { UserService } from '../../services/user/user.service';
import { multiSelectProp } from 'src/app/shared/utils/input/multi-select-with-icons/multi-select-with-icons.component';
import {
  CurrentRideAction,
  CurrentRideActionType,
} from 'src/app/shared/store/current-ride-slice/current-ride.actions';

@Component({
  selector: 'login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
})
export class LoginComponent implements OnInit {
  @Input('open') open!: boolean;
  @Input('closeFunc') closeFunc!: () => void;
  @Input('openRegisterModal') openRegisterModal!: () => void;

  openForgotPasswordModal = false;
  openChooseRoleModal = false;
  userRoleItems: multiSelectProp[] = [];

  openErrorToast = false;
  openSuccessToast = false;

  user: User | null = null;

  email: string;
  password: string;

  constructor(
    private service: UserService,
    private socialAuthService: SocialAuthService,
    private store: Store<StoreType>
  ) {
    this.email = '';
    this.password = '';
  }

  ngOnInit() {
    this.socialAuthService.authState.subscribe(
      async (socialUser: SocialUser) => {
        let user: User = {
          city: '',
          phoneNumber: '',
          id: socialUser.id,
          email: socialUser.email,
          firstName: socialUser.firstName,
          lastName: socialUser.lastName,
          roles: ['CLIENT'],
          profilePicture: socialUser.photoUrl,
        };
        await this.login(user);
      }
    );
  }

  toggleForgotPasswordModal = () => {
    this.openForgotPasswordModal = !this.openForgotPasswordModal;
  };

  toggleChooseRoleModal = () => {
    this.openChooseRoleModal = !this.openChooseRoleModal;
  };

  toggleErrorToast = () => {
    this.openErrorToast = !this.openErrorToast;
  };

  toggleSuccessToast = () => {
    this.openSuccessToast = !this.openSuccessToast;
  };

  signInWithFB(): void {
    this.socialAuthService.signIn(FacebookLoginProvider.PROVIDER_ID);
  }

  async login(user: User) {
    this.openSuccessToast = true;
    this.store.dispatch(new LoggedUserAction(LoggedUserActionType.LOGIN, user));
    const currentRide = await this.service.getCurrentRide(user.email);
    if (currentRide) {
      this.store.dispatch(
        new CurrentRideAction(CurrentRideActionType.SET, currentRide)
      );
    }
    this.closeFunc();
  }

  async loginWithOneRole(role: string) {
    this.user!.roles = [role];
    await this.login(this.user!);
    this.toggleChooseRoleModal();
  }

  async onSubmit() {
    if (this.valid()) {
      let success: boolean = await this.service.loginUser(
        this.getEncodedData()
      );
      if (success) {
        this.user = await this.service.getUser();
        if (this.user && this.user.roles.length === 1) {
          await this.login(this.user);
        } else if (this.user && this.user.roles.length > 1) {
          this.chooseRoleForLogin();
        } else {
          this.openErrorToast = true; //token nije upisan
        }
      } else {
        this.openErrorToast = true; //ne postoji user sa unetim kredencijalima
      }
    } else {
      this.openErrorToast = true; //email ili sifra nisu u dobrom formatu upisani
    }
  }

  getEncodedData(): URLSearchParams {
    let encodedData = new URLSearchParams();
    encodedData.append('username', this.email);
    encodedData.append('password', this.password);
    return encodedData;
  }

  chooseRoleForLogin() {
    this.userRoleItems = [];
    for (let role of this.user!.roles) {
      this.userRoleItems.push({
        path: './assets/icons/user-role.png',
        title: role.charAt(0).toUpperCase() + role.slice(1).toLowerCase(),
      });
    }
    this.toggleChooseRoleModal();
  }

  valid(): boolean {
    return true;
    // return (
    //   /^.+[@].+[.].+$/.test(this.email) &&
    //   /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])[0-9a-zA-Z]{8,20}$/.test(this.password)
    // );
  }
}
