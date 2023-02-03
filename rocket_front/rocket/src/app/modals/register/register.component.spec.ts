import {
  ComponentFixture,
  fakeAsync,
  TestBed,
  tick,
} from '@angular/core/testing';
import { AuthService } from 'src/app/services/auth/auth.service';
import { ErrorComponent } from '../error/error.component';
import { SuccessComponent } from '../success/success.component';

import { RegisterComponent } from './register.component';
import {ToastrService} from "ngx-toastr";

fdescribe('RegisterComponent', () => {
  let component: RegisterComponent;
  let fixture: ComponentFixture<RegisterComponent>;
  let toastr: jasmine.SpyObj<ToastrService>;
  let authService: jasmine.SpyObj<AuthService>;

  beforeEach(async () => {
    authService = jasmine.createSpyObj('AuthService', ['registerUser']);
    toastr = jasmine.createSpyObj('ToastrService', ['success', 'error']);

    await TestBed.configureTestingModule({
      declarations: [RegisterComponent, SuccessComponent, ErrorComponent],
      providers: [{ provide: AuthService, useValue: authService }, { provide: ToastrService, useValue: toastr }],
    }).compileComponents();

    fixture = TestBed.createComponent(RegisterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    component.closeFunc = function () {
      component.open = false;
    };
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should go to next page on click', () => {
    component.currentPage = 1;
    component.nextPage();
    expect(component.currentPage).toBe(2);
  });

  it('should go to previous page on click', () => {
    component.currentPage = 2;
    component.previousPage();
    expect(component.currentPage).toBe(1);
  });

  it('should successfully register with valid input', fakeAsync(() => {
    component.email = 'test@test.com';
    component.password = 'Password123';
    component.confirmPassword = 'Password123';
    component.firstName = 'Test';
    component.lastName = 'Testic';
    component.city = 'Novi Sad';
    component.phone = '0600123456';

    const data = {
      email: component.email,
      password: component.password,
      firstName: component.firstName,
      lastName: component.lastName,
      city: component.city,
      phoneNumber: component.phone,
    };

    authService.registerUser.and.returnValue(Promise.resolve(true));

    spyOn(component, 'toggleSuccessToast').and.callThrough();
    spyOn(component, 'toggleErrorToast').and.callThrough();

    component.onSignUp();
    tick();

    expect(authService.registerUser).toHaveBeenCalledWith(
      jasmine.objectContaining(data)
    );
    expect(component.toggleSuccessToast).toHaveBeenCalled();
    expect(component.toggleErrorToast).not.toHaveBeenCalled();
    expect(component.errorMessage).toBeFalsy();
  }));

  it('should register with valid input, but user already exists', fakeAsync(() => {
    component.email = 'test@test.com';
    component.password = 'Password123';
    component.confirmPassword = 'Password123';
    component.firstName = 'Test';
    component.lastName = 'Testic';
    component.city = 'Novi Sad';
    component.phone = '0600123456';

    const data = {
      email: component.email,
      password: component.password,
      firstName: component.firstName,
      lastName: component.lastName,
      city: component.city,
      phoneNumber: component.phone,
    };

    authService.registerUser.and.returnValue(Promise.resolve(false));

    spyOn(component, 'toggleSuccessToast').and.callThrough();
    spyOn(component, 'toggleErrorToast').and.callThrough();

    component.onSignUp();
    tick();

    expect(authService.registerUser).toHaveBeenCalledWith(
      jasmine.objectContaining(data)
    );
    expect(component.toggleSuccessToast).not.toHaveBeenCalled();
    expect(component.toggleErrorToast).toHaveBeenCalled();
    expect(component.errorMessage).toBe(
      'User with this email already registered.'
    );
  }));

  it('should register with invalid email', fakeAsync(() => {
    component.email = 'invalid';
    component.password = 'Password123';
    component.confirmPassword = 'Password123';
    component.firstName = 'Test';
    component.lastName = 'Testic';
    component.city = 'Novi Sad';
    component.phone = '0600123456';

    authService.registerUser.and.returnValue(Promise.resolve(false));

    spyOn(component, 'toggleSuccessToast').and.callThrough();
    spyOn(component, 'toggleErrorToast').and.callThrough();

    component.onSignUp();
    tick();

    expect(authService.registerUser).not.toHaveBeenCalled();
    expect(component.toggleSuccessToast).not.toHaveBeenCalled();
    expect(component.toggleErrorToast).toHaveBeenCalled();
    expect(component.errorMessage).toBe(
      'Invalid format of email. Please enter email in format: username@domain.com.'
    );
  }));

  it('should register with empty email', fakeAsync(() => {
    component.email = '';
    component.password = 'Password123';
    component.confirmPassword = 'Password123';
    component.firstName = 'Test';
    component.lastName = 'Testic';
    component.city = 'Novi Sad';
    component.phone = '0600123456';

    authService.registerUser.and.returnValue(Promise.resolve(false));

    spyOn(component, 'toggleSuccessToast').and.callThrough();
    spyOn(component, 'toggleErrorToast').and.callThrough();

    component.onSignUp();
    tick();

    expect(authService.registerUser).not.toHaveBeenCalled();
    expect(component.toggleSuccessToast).not.toHaveBeenCalled();
    expect(component.toggleErrorToast).toHaveBeenCalled();
    expect(component.errorMessage).toBe(
      'Invalid format of email. Please enter email in format: username@domain.com.'
    );
  }));

  it('should register with invalid password', fakeAsync(() => {
    component.email = 'test@test.com';
    component.password = 'invalid';
    component.confirmPassword = 'Password123';
    component.firstName = 'Test';
    component.lastName = 'Testic';
    component.city = 'Novi Sad';
    component.phone = '0600123456';

    authService.registerUser.and.returnValue(Promise.resolve(false));

    spyOn(component, 'toggleSuccessToast').and.callThrough();
    spyOn(component, 'toggleErrorToast').and.callThrough();

    component.onSignUp();
    tick();

    expect(authService.registerUser).not.toHaveBeenCalled();
    expect(component.toggleSuccessToast).not.toHaveBeenCalled();
    expect(component.toggleErrorToast).toHaveBeenCalled();
    expect(component.errorMessage).toBe(
      'Invalid format of password. Password must be at least 8 characters long and contain at least one capital letter and one number.'
    );
  }));

  it('should register with empty password', fakeAsync(() => {
    component.email = 'test@test.com';
    component.password = '';
    component.confirmPassword = 'Password123';
    component.firstName = 'Test';
    component.lastName = 'Testic';
    component.city = 'Novi Sad';
    component.phone = '0600123456';

    authService.registerUser.and.returnValue(Promise.resolve(false));

    spyOn(component, 'toggleSuccessToast').and.callThrough();
    spyOn(component, 'toggleErrorToast').and.callThrough();

    component.onSignUp();
    tick();

    expect(authService.registerUser).not.toHaveBeenCalled();
    expect(component.toggleSuccessToast).not.toHaveBeenCalled();
    expect(component.toggleErrorToast).toHaveBeenCalled();
    expect(component.errorMessage).toBe(
      'Invalid format of password. Password must be at least 8 characters long and contain at least one capital letter and one number.'
    );
  }));

  it('should register with password and confirm mail do not match', fakeAsync(() => {
    component.email = 'test@test.com';
    component.password = 'Password123';
    component.confirmPassword = 'Password12345';
    component.firstName = 'Test';
    component.lastName = 'Testic';
    component.city = 'Novi Sad';
    component.phone = '0600123456';

    authService.registerUser.and.returnValue(Promise.resolve(false));

    spyOn(component, 'toggleSuccessToast').and.callThrough();
    spyOn(component, 'toggleErrorToast').and.callThrough();

    component.onSignUp();
    tick();

    expect(authService.registerUser).not.toHaveBeenCalled();
    expect(component.toggleSuccessToast).not.toHaveBeenCalled();
    expect(component.toggleErrorToast).toHaveBeenCalled();
    expect(component.errorMessage).toBe(
      'The password and confirmation password you have entered do not match. Please double-check your password and try again.'
    );
  }));

  it('should register with empty confirm password', fakeAsync(() => {
    component.email = 'test@test.com';
    component.password = 'Password123';
    component.confirmPassword = '';
    component.firstName = 'Test';
    component.lastName = 'Testic';
    component.city = 'Novi Sad';
    component.phone = '0600123456';

    authService.registerUser.and.returnValue(Promise.resolve(false));

    spyOn(component, 'toggleSuccessToast').and.callThrough();
    spyOn(component, 'toggleErrorToast').and.callThrough();

    component.onSignUp();
    tick();

    expect(authService.registerUser).not.toHaveBeenCalled();
    expect(component.toggleSuccessToast).not.toHaveBeenCalled();
    expect(component.toggleErrorToast).toHaveBeenCalled();
    expect(component.errorMessage).toBe(
      'The password and confirmation password you have entered do not match. Please double-check your password and try again.'
    );
  }));

  it('should register with invalid first name', fakeAsync(() => {
    component.email = 'test@test.com';
    component.password = 'Password123';
    component.confirmPassword = 'Password123';
    component.firstName = 'invalid123';
    component.lastName = 'Testic';
    component.city = 'Novi Sad';
    component.phone = '0600123456';

    authService.registerUser.and.returnValue(Promise.resolve(false));

    spyOn(component, 'toggleSuccessToast').and.callThrough();
    spyOn(component, 'toggleErrorToast').and.callThrough();

    component.onSignUp();
    tick();

    expect(authService.registerUser).not.toHaveBeenCalled();
    expect(component.toggleSuccessToast).not.toHaveBeenCalled();
    expect(component.toggleErrorToast).toHaveBeenCalled();
    expect(component.errorMessage).toBe('First name must contain all letters.');
  }));

  it('should register with empty first name', fakeAsync(() => {
    component.email = 'test@test.com';
    component.password = 'Password123';
    component.confirmPassword = 'Password123';
    component.firstName = '';
    component.lastName = 'Testic';
    component.city = 'Novi Sad';
    component.phone = '0600123456';

    authService.registerUser.and.returnValue(Promise.resolve(false));

    spyOn(component, 'toggleSuccessToast').and.callThrough();
    spyOn(component, 'toggleErrorToast').and.callThrough();

    component.onSignUp();
    tick();

    expect(authService.registerUser).not.toHaveBeenCalled();
    expect(component.toggleSuccessToast).not.toHaveBeenCalled();
    expect(component.toggleErrorToast).toHaveBeenCalled();
    expect(component.errorMessage).toBe('First name must contain all letters.');
  }));

  it('should register with invalid last name', fakeAsync(() => {
    component.email = 'test@test.com';
    component.password = 'Password123';
    component.confirmPassword = 'Password123';
    component.firstName = 'Test';
    component.lastName = '123';
    component.city = 'Novi Sad';
    component.phone = '0600123456';

    authService.registerUser.and.returnValue(Promise.resolve(false));

    spyOn(component, 'toggleSuccessToast').and.callThrough();
    spyOn(component, 'toggleErrorToast').and.callThrough();

    component.onSignUp();
    tick();

    expect(authService.registerUser).not.toHaveBeenCalled();
    expect(component.toggleSuccessToast).not.toHaveBeenCalled();
    expect(component.toggleErrorToast).toHaveBeenCalled();
    expect(component.errorMessage).toBe('Last name must contain all letters.');
  }));

  it('should register with empty last name', fakeAsync(() => {
    component.email = 'test@test.com';
    component.password = 'Password123';
    component.confirmPassword = 'Password123';
    component.firstName = 'Test';
    component.lastName = '';
    component.city = 'Novi Sad';
    component.phone = '0600123456';

    authService.registerUser.and.returnValue(Promise.resolve(false));

    spyOn(component, 'toggleSuccessToast').and.callThrough();
    spyOn(component, 'toggleErrorToast').and.callThrough();

    component.onSignUp();
    tick();

    expect(authService.registerUser).not.toHaveBeenCalled();
    expect(component.toggleSuccessToast).not.toHaveBeenCalled();
    expect(component.toggleErrorToast).toHaveBeenCalled();
    expect(component.errorMessage).toBe('Last name must contain all letters.');
  }));

  it('should register with invalid city', fakeAsync(() => {
    component.email = 'test@test.com';
    component.password = 'Password123';
    component.confirmPassword = 'Password123';
    component.firstName = 'Test';
    component.lastName = 'Testic';
    component.city = 'Novi@';
    component.phone = '0600123456';

    authService.registerUser.and.returnValue(Promise.resolve(false));

    spyOn(component, 'toggleSuccessToast').and.callThrough();
    spyOn(component, 'toggleErrorToast').and.callThrough();

    component.onSignUp();
    tick();

    expect(authService.registerUser).not.toHaveBeenCalled();
    expect(component.toggleSuccessToast).not.toHaveBeenCalled();
    expect(component.toggleErrorToast).toHaveBeenCalled();
    expect(component.errorMessage).toBe('City must contain all letters.');
  }));

  it('should register with empty city', fakeAsync(() => {
    component.email = 'test@test.com';
    component.password = 'Password123';
    component.confirmPassword = 'Password123';
    component.firstName = 'Test';
    component.lastName = 'Testic';
    component.city = '';
    component.phone = '0600123456';

    authService.registerUser.and.returnValue(Promise.resolve(false));

    spyOn(component, 'toggleSuccessToast').and.callThrough();
    spyOn(component, 'toggleErrorToast').and.callThrough();

    component.onSignUp();
    tick();

    expect(authService.registerUser).not.toHaveBeenCalled();
    expect(component.toggleSuccessToast).not.toHaveBeenCalled();
    expect(component.toggleErrorToast).toHaveBeenCalled();
    expect(component.errorMessage).toBe('City must contain all letters.');
  }));

  it('should register with invalid phone number', fakeAsync(() => {
    component.email = 'test@test.com';
    component.password = 'Password123';
    component.confirmPassword = 'Password123';
    component.firstName = 'Test';
    component.lastName = 'Testic';
    component.city = 'Novi Sad';
    component.phone = '%381554848';

    authService.registerUser.and.returnValue(Promise.resolve(false));

    spyOn(component, 'toggleSuccessToast').and.callThrough();
    spyOn(component, 'toggleErrorToast').and.callThrough();

    component.onSignUp();
    tick();

    expect(authService.registerUser).not.toHaveBeenCalled();
    expect(component.toggleSuccessToast).not.toHaveBeenCalled();
    expect(component.toggleErrorToast).toHaveBeenCalled();
    expect(component.errorMessage).toBe(
      "Phone number must contain only numbers and must start with '06'."
    );
  }));

  it('should register with empty phone number', fakeAsync(() => {
    component.email = 'test@test.com';
    component.password = 'Password123';
    component.confirmPassword = 'Password123';
    component.firstName = 'Test';
    component.lastName = 'Testic';
    component.city = 'Novi Sad';
    component.phone = '';

    authService.registerUser.and.returnValue(Promise.resolve(false));

    spyOn(component, 'toggleSuccessToast').and.callThrough();
    spyOn(component, 'toggleErrorToast').and.callThrough();

    component.onSignUp();
    tick();

    expect(authService.registerUser).not.toHaveBeenCalled();
    expect(component.toggleSuccessToast).not.toHaveBeenCalled();
    expect(component.toggleErrorToast).toHaveBeenCalled();
    expect(component.errorMessage).toBe(
      "Phone number must contain only numbers and must start with '06'."
    );
  }));
});
