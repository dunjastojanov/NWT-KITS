import { Component, Input, OnInit } from '@angular/core';
import { SocialAuthService, SocialUser } from '@abacritt/angularx-social-login';
import { Store } from '@ngrx/store';
import { StoreType } from 'src/app/shared/store/types';
import {
  LoggedUserAction,
  LoggedUserActionType,
} from 'src/app/shared/store/logged-user-slice/logged-user.actions';
import { User } from 'src/app/interfaces/User';
import { ToastrService } from 'ngx-toastr';
import { SocketService } from 'src/app/services/sockets/sockets.service';
import { AuthService } from 'src/app/services/auth/auth.service';

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

  user: User | null = null;

  email: string;
  password: string;

  constructor(
    private socialAuthService: SocialAuthService,
    private store: Store<StoreType>,
    private toastr: ToastrService,
    private socketService: SocketService,
    private authService: AuthService
  ) {
    this.email = '';
    this.password = '';
    this.store.select('loggedUser').subscribe((res) => {
      if (res.user != null) {
        this.socketService.initializeWebSocketConnection();
      }
      this.socketService.initializeWebSocketConnectionNonUser();
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
        await this.authService.loginGoogleUser(user).then(async (token) => {
          this.authService.setToken(token);
          let user: User | null = await this.authService.getUser();
          if (user) {
            await this.login(user);
          }
        });
      }
    );
  }

  hasRole(user: User | null, role: string): boolean {
    return user !== null && user?.roles.indexOf(role) !== -1;
  }

  toggleForgotPasswordModal = () => {
    this.openForgotPasswordModal = !this.openForgotPasswordModal;
  };

  async login(user: User) {
    this.toastr.success('Login successful!');
    this.store.dispatch(new LoggedUserAction(LoggedUserActionType.LOGIN, user));
    await this.authService.loadResources(user.email);
    this.closeFunc();
  }

  async onSubmit() {
    if (this.valid()) {
      let success: boolean = await this.authService.loginUser(
        this.getEncodedData()
      );

      if (success) {
        this.user = await this.authService.getUser();

        if (this.hasRole(this.user, 'DRIVER')) {
          await this.authService.changeStatus('ACTIVE').then((status) => {
            if (status === 'ACTIVE' && this.user) {
              this.user.status = 'ACTIVE';
            }
          });
        }

        if (this.user) {
          await this.login(this.user);
        }
      } else {
        this.toastr.error('Invalid email or password.'); //ne postoji user sa unetim kredencijalima
      }
    } else {
      this.toastr.error('Invalid email or password.'); //email ili sifra nisu u dobrom formatu upisani
    }
  }

  getEncodedData(): URLSearchParams {
    let encodedData = new URLSearchParams();
    encodedData.append('username', this.email);
    encodedData.append('password', this.password);
    return encodedData;
  }

  valid(): boolean {
    return (
      /^.+[@].+[.].+$/.test(this.email) &&
      /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])[0-9a-zA-Z]{8,20}$/.test(this.password)
    );
  }
}
