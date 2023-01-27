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
import { NotificationService } from '../../services/notification/notification.service';
import { multiSelectProp } from 'src/app/shared/utils/input/multi-select-with-icons/multi-select-with-icons.component';
import {
  CurrentRideAction,
  CurrentRideActionType,
} from 'src/app/shared/store/current-ride-slice/current-ride.actions';
import { ToastrService } from 'ngx-toastr';
import { SocketService } from 'src/app/services/sockets/sockets.service';
import { VehicleService } from 'src/app/services/vehicle/vehicle.service';
import { ChatService } from '../../services/chat/chat.service';

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

  user: User | null = null;

  email: string;
  password: string;

  constructor(
    private service: UserService,
    private socialAuthService: SocialAuthService,
    private store: Store<StoreType>,
    private socketService: SocketService,
    private notificationService: NotificationService,
    private toastr: ToastrService,
    private vehicleService: VehicleService,
    private userService: UserService,
    private chatService: ChatService
  ) {
    this.email = '';
    this.password = '';
    this.store.select('loggedUser').subscribe((res) => {
      this.socketService.initializeWebSocketConnection();
    });
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
        await this.userService.loginGoogleUser(user).then((result) => {
          //TODO ovde se dobije access token i treba da se namesti
          console.log(result);
        });
        await this.login(user);
      }
    );
  }

  hasRole(user: User | null, role: string): boolean {
    return user !== null && user?.roles.indexOf(role) !== -1;
  }

  toggleForgotPasswordModal = () => {
    this.openForgotPasswordModal = !this.openForgotPasswordModal;
  };

  toggleChooseRoleModal = () => {
    this.openChooseRoleModal = !this.openChooseRoleModal;
  };

  signInWithFB(): void {
    this.socialAuthService.signIn(FacebookLoginProvider.PROVIDER_ID);
  }

  async login(user: User) {
    this.toastr.success('Login successful!');
    this.store.dispatch(new LoggedUserAction(LoggedUserActionType.LOGIN, user));
    await this.chatService.loadMessages();
    if (this.hasRole(user, 'DRIVER')) {
      this.vehicleService.changeStatus('ACTIVE').then(() => {});
    }

    await this.notificationService.loadNotifications();
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
          this.toastr.error('Invalid email or password.');
          //token nije upisan
        }
      } else {
        this.toastr.error('Invalid email or password.');
        //ne postoji user sa unetim kredencijalima
      }
    } else {
      this.toastr.error('Invalid email or password.');
      //email ili sifra nisu u dobrom formatu upisani
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
    return (
      /^.+[@].+[.].+$/.test(this.email) &&
      /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])[0-9a-zA-Z]{8,20}$/.test(this.password)
    );
  }
}
