declare var global: typeof window;

import { SocialAuthService } from '@abacritt/angularx-social-login';

import {
  ComponentFixture,
  fakeAsync,
  TestBed,
  tick,
} from '@angular/core/testing';
import { Store } from '@ngrx/store';
import { ToastrService } from 'ngx-toastr';
import { User } from 'src/app/interfaces/User';
import { AuthService } from 'src/app/services/auth/auth.service';
import { SocketService } from 'src/app/services/sockets/sockets.service';
import { StoreType } from 'src/app/shared/store/types';
import { LoginComponent } from './login.component';
import { of } from 'rxjs';
import { ForgotPasswordComponent } from '../forgot-password/forgot-password.component';

fdescribe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let authService: jasmine.SpyObj<AuthService>;
  let toastr: jasmine.SpyObj<ToastrService>;
  let socketService: any;
  let store: jasmine.SpyObj<Store<StoreType>>;

  beforeEach(async () => {
    authService = jasmine.createSpyObj('AuthService', [
      'loginGoogleUser',
      'getUser',
      'loadResources',
      'loginUser',
      'changeStatus',
      'setToken',
    ]);
    toastr = jasmine.createSpyObj('ToastrService', ['success', 'error']);
    socketService = {};
    store = jasmine.createSpyObj('Store', ['select', 'dispatch']);
    store.select.and.returnValue(of(null));

    await TestBed.configureTestingModule({
      declarations: [LoginComponent, ForgotPasswordComponent],
      providers: [
        {
          provide: SocialAuthService,
          useValue: { authState: { subscribe: () => {} } },
        },
        { provide: AuthService, useValue: authService },
        { provide: ToastrService, useValue: toastr },
        { provide: SocketService, useValue: socketService },
        { provide: Store, useValue: store },
      ],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    component.closeFunc = function () {
      component.open = false;
    };
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should call client login on submit with valid email and password', fakeAsync(() => {
    store.select.and.callThrough();
    const user: User = {
      city: 'Novi Sad',
      phoneNumber: '0600200021',
      id: '1',
      email: 'test@test.com',
      firstName: 'Test',
      lastName: 'Testic',
      roles: ['CLIENT'],
      profilePicture: '',
    };
    authService.loginUser.and.returnValue(Promise.resolve(true));
    authService.getUser.and.returnValue(Promise.resolve(user));
    component.email = 'test@test.com';
    component.password = 'Password123';

    spyOn(component, 'login').and.callThrough();
    component.onSubmit();
    tick();
    expect(authService.loginUser).toHaveBeenCalledWith(
      jasmine.any(URLSearchParams)
    );
    expect(authService.getUser).toHaveBeenCalled();
    expect(component.login).toHaveBeenCalledWith(
      jasmine.objectContaining(user)
    );
    expect(authService.changeStatus).not.toHaveBeenCalledWith('ACTIVE');
  }));

  it('should call driver login on submit with valid email and password', fakeAsync(() => {
    store.select.and.callThrough();
    const user: User = {
      city: 'Novi Sad',
      phoneNumber: '0600200021',
      id: '1',
      email: 'test@test.com',
      firstName: 'Test',
      lastName: 'Testic',
      roles: ['DRIVER'],
      profilePicture: '',
    };
    authService.loginUser.and.returnValue(Promise.resolve(true));
    authService.getUser.and.returnValue(Promise.resolve(user));
    authService.changeStatus.and.returnValue(Promise.resolve('ACTIVE'));
    component.email = 'test@test.com';
    component.password = 'Password123';

    spyOn(component, 'login').and.callThrough();
    component.onSubmit();
    tick();
    expect(authService.loginUser).toHaveBeenCalledWith(
      jasmine.any(URLSearchParams)
    );
    expect(component.login).toHaveBeenCalledWith(
      jasmine.objectContaining(user)
    );
    expect(authService.changeStatus).toHaveBeenCalledWith('ACTIVE');
  }));

  it('should call client login on submit with invalid email and valid password', fakeAsync(() => {
    store.select.and.callThrough();
    const user: User = {
      city: 'Novi Sad',
      phoneNumber: '0600200021',
      id: '1',
      email: 'test@test.com',
      firstName: 'Test',
      lastName: 'Testic',
      roles: ['CLIENT'],
      profilePicture: '',
    };
    authService.loginUser.and.returnValue(Promise.resolve(false));
    authService.getUser.and.returnValue(Promise.resolve(user));
    component.email = 'invalid@test.com';
    component.password = 'Password123';

    spyOn(component, 'valid').and.callThrough();
    spyOn(component, 'login').and.callThrough();
    component.onSubmit();
    tick();
    expect(authService.loginUser).toHaveBeenCalledWith(
      jasmine.any(URLSearchParams)
    );
    expect(component.login).not.toHaveBeenCalledWith(
      jasmine.objectContaining(user)
    );
    expect(authService.changeStatus).not.toHaveBeenCalledWith('ACTIVE');
    expect(toastr.error).toHaveBeenCalledWith('Invalid email or password.');
  }));

  it('should call client login on submit with valid email and invalid password', fakeAsync(() => {
    store.select.and.callThrough();
    const user: User = {
      city: 'Novi Sad',
      phoneNumber: '0600200021',
      id: '1',
      email: 'test@test.com',
      firstName: 'Test',
      lastName: 'Testic',
      roles: ['CLIENT'],
      profilePicture: '',
    };
    authService.loginUser.and.returnValue(Promise.resolve(false));
    authService.getUser.and.returnValue(Promise.resolve(user));
    component.email = 'test@test.com';
    component.password = 'Invalid123';

    spyOn(component, 'valid').and.callThrough();
    spyOn(component, 'login').and.callThrough();
    component.onSubmit();
    tick();
    expect(authService.loginUser).toHaveBeenCalledWith(
      jasmine.any(URLSearchParams)
    );
    expect(component.login).not.toHaveBeenCalledWith(
      jasmine.objectContaining(user)
    );
    expect(authService.changeStatus).not.toHaveBeenCalledWith('ACTIVE');
    expect(toastr.error).toHaveBeenCalledWith('Invalid email or password.');
  }));

  it('should call driver login on submit with invalid email and valid password', fakeAsync(() => {
    store.select.and.callThrough();
    const user: User = {
      city: 'Novi Sad',
      phoneNumber: '0600200021',
      id: '1',
      email: 'test@test.com',
      firstName: 'Test',
      lastName: 'Testic',
      roles: ['DRIVER'],
      profilePicture: '',
    };
    authService.loginUser.and.returnValue(Promise.resolve(false));
    authService.getUser.and.returnValue(Promise.resolve(user));
    component.email = 'invalid@test.com';
    component.password = 'Password123';

    spyOn(component, 'valid').and.callThrough();
    spyOn(component, 'login').and.callThrough();
    component.onSubmit();
    tick();
    expect(authService.loginUser).toHaveBeenCalledWith(
      jasmine.any(URLSearchParams)
    );
    expect(component.login).not.toHaveBeenCalledWith(
      jasmine.objectContaining(user)
    );
    expect(authService.changeStatus).not.toHaveBeenCalledWith('ACTIVE');
    expect(toastr.error).toHaveBeenCalledWith('Invalid email or password.');
  }));

  it('should call driver login on submit with valid email and invalid password', fakeAsync(() => {
    store.select.and.callThrough();
    const user: User = {
      city: 'Novi Sad',
      phoneNumber: '0600200021',
      id: '1',
      email: 'test@test.com',
      firstName: 'Test',
      lastName: 'Testic',
      roles: ['DRIVER'],
      profilePicture: '',
    };
    authService.loginUser.and.returnValue(Promise.resolve(false));
    authService.getUser.and.returnValue(Promise.resolve(user));
    component.email = 'test@test.com';
    component.password = 'Invalid123';

    spyOn(component, 'valid').and.callThrough();
    spyOn(component, 'login').and.callThrough();
    component.onSubmit();
    tick();
    expect(authService.loginUser).toHaveBeenCalledWith(
      jasmine.any(URLSearchParams)
    );
    expect(component.login).not.toHaveBeenCalledWith(
      jasmine.objectContaining(user)
    );
    expect(authService.changeStatus).not.toHaveBeenCalledWith('ACTIVE');
    expect(toastr.error).toHaveBeenCalledWith('Invalid email or password.');
  }));

  it('should call client login on submit with invalid form of email and password', fakeAsync(() => {
    store.select.and.callThrough();
    const user: User = {
      city: 'Novi Sad',
      phoneNumber: '0600200021',
      id: '1',
      email: 'test@test.com',
      firstName: 'Test',
      lastName: 'Testic',
      roles: ['CLIENT'],
      profilePicture: '',
    };
    authService.loginUser.and.returnValue(Promise.resolve(false));
    authService.getUser.and.returnValue(Promise.resolve(user));
    component.email = 'invalid';
    component.password = 'Invalid123';

    spyOn(component, 'valid').and.callThrough();
    spyOn(component, 'login').and.callThrough();
    component.onSubmit();
    tick();
    expect(authService.loginUser).not.toHaveBeenCalledWith(
      jasmine.any(URLSearchParams)
    );
    expect(component.login).not.toHaveBeenCalledWith(
      jasmine.objectContaining(user)
    );
    expect(authService.changeStatus).not.toHaveBeenCalledWith('ACTIVE');
    expect(toastr.error).toHaveBeenCalledWith('Invalid email or password.');
  }));

  it('should call client login on submit with valid email and invalid form password', fakeAsync(() => {
    store.select.and.callThrough();
    const user: User = {
      city: 'Novi Sad',
      phoneNumber: '0600200021',
      id: '1',
      email: 'test@test.com',
      firstName: 'Test',
      lastName: 'Testic',
      roles: ['CLIENT'],
      profilePicture: '',
    };

    component.email = 'test@gmail.com';
    component.password = 'invalid';

    spyOn(component, 'valid').and.callThrough();
    spyOn(component, 'login').and.callThrough();
    component.onSubmit();
    tick();
    expect(authService.loginUser).not.toHaveBeenCalledWith(
      jasmine.any(URLSearchParams)
    );
    expect(component.login).not.toHaveBeenCalledWith(
      jasmine.objectContaining(user)
    );
    expect(authService.changeStatus).not.toHaveBeenCalledWith('ACTIVE');
    expect(toastr.error).toHaveBeenCalledWith('Invalid email or password.');
  }));

  it('should call client login on submit with empty email and empty password', fakeAsync(() => {
    store.select.and.callThrough();
    const user: User = {
      city: 'Novi Sad',
      phoneNumber: '0600200021',
      id: '1',
      email: 'test@test.com',
      firstName: 'Test',
      lastName: 'Testic',
      roles: ['CLIENT'],
      profilePicture: '',
    };

    component.email = '';
    component.password = '';

    spyOn(component, 'valid').and.callThrough();
    spyOn(component, 'login').and.callThrough();
    component.onSubmit();
    tick();
    expect(authService.loginUser).not.toHaveBeenCalledWith(
      jasmine.any(URLSearchParams)
    );
    expect(component.login).not.toHaveBeenCalledWith(
      jasmine.objectContaining(user)
    );
    expect(authService.changeStatus).not.toHaveBeenCalledWith('ACTIVE');
    expect(toastr.error).toHaveBeenCalledWith('Invalid email or password.');
  }));

  it('should call client login on submit with valid email and empty password', fakeAsync(() => {
    store.select.and.callThrough();
    const user: User = {
      city: 'Novi Sad',
      phoneNumber: '0600200021',
      id: '1',
      email: 'test@test.com',
      firstName: 'Test',
      lastName: 'Testic',
      roles: ['CLIENT'],
      profilePicture: '',
    };

    component.email = 'test@test.com';
    component.password = '';

    spyOn(component, 'valid').and.callThrough();
    spyOn(component, 'login').and.callThrough();
    component.onSubmit();
    tick();
    expect(authService.loginUser).not.toHaveBeenCalledWith(
      jasmine.any(URLSearchParams)
    );
    expect(component.login).not.toHaveBeenCalledWith(
      jasmine.objectContaining(user)
    );
    expect(authService.changeStatus).not.toHaveBeenCalledWith('ACTIVE');
    expect(toastr.error).toHaveBeenCalledWith('Invalid email or password.');
  }));

  it('should call driver login on submit with empty email and valid password', fakeAsync(() => {
    store.select.and.callThrough();
    const user: User = {
      city: 'Novi Sad',
      phoneNumber: '0600200021',
      id: '1',
      email: 'test@test.com',
      firstName: 'Test',
      lastName: 'Testic',
      roles: ['CLIENT'],
      profilePicture: '',
    };

    component.email = '';
    component.password = 'Password1';

    spyOn(component, 'valid').and.callThrough();
    spyOn(component, 'login').and.callThrough();
    component.onSubmit();
    tick();
    expect(authService.loginUser).not.toHaveBeenCalledWith(
      jasmine.any(URLSearchParams)
    );
    expect(component.login).not.toHaveBeenCalledWith(
      jasmine.objectContaining(user)
    );
    expect(authService.changeStatus).not.toHaveBeenCalledWith('ACTIVE');
    expect(toastr.error).toHaveBeenCalledWith('Invalid email or password.');
  }));
});
