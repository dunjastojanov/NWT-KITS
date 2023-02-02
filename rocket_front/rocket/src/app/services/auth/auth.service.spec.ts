import { TestBed } from '@angular/core/testing';
import { Store } from '@ngrx/store';
import { CookieService } from 'ngx-cookie-service';
import { StoreType } from 'src/app/shared/store/types';
import { of } from 'rxjs';
import { AuthService } from './auth.service';
import { http } from 'src/app/shared/api/axios-wrapper';
import { User } from 'src/app/interfaces/User';

fdescribe('Authservice', () => {
  let authService: AuthService;
  let cookieService: jasmine.SpyObj<CookieService>;
  let store: jasmine.SpyObj<Store<StoreType>>;

  let httpSpy: jasmine.SpyObj<typeof http>;

  beforeEach(async () => {
    cookieService = jasmine.createSpyObj('CookieService', ['set']);
    store = jasmine.createSpyObj('Store', ['select', 'dispatch']);
    httpSpy = jasmine.createSpyObj('Http', ['post', 'get']);
    store.select.and.returnValue(of(null));
    TestBed.configureTestingModule({
      providers: [
        { provide: Store, useValue: store },
        { provide: CookieService, useValue: cookieService },
      ],
    }).compileComponents();
    authService = TestBed.inject(AuthService);
    authService.setHttp(httpSpy);
  });

  it('should be created', () => {
    expect(authService).toBeTruthy();
  });

  describe('AuthService', () => {
    it('should return true on successful registration', async () => {
      const data = {
        email: 'test@gmail.com',
        password: 'Password123',
        firstName: 'Test',
        lastName: 'Testic',
        city: 'Novi Sad',
        phoneNumber: '06006511201',
      };
      httpSpy.post.and.returnValue(
        Promise.resolve({ data: 'Successful registration' })
      );
      const result = await authService.registerUser(data);
      expect(httpSpy.post).toHaveBeenCalledWith('/api/user', data);
      expect(result).toBe(true);
    });

    it('should return false on failed registration', async () => {
      const data = {
        email: 'test@gmail.com',
        password: 'Password123',
        firstName: 'Test',
        lastName: 'Testic',
        city: 'Novi Sad',
        phoneNumber: '06006511201',
      };

      httpSpy.post.and.returnValue(Promise.reject('User already exists'));
      const result = await authService.registerUser(data);
      expect(httpSpy.post).toHaveBeenCalledWith('/api/user', data);
      expect(result).toBe(false);
    });

    it('should return false on bad data for registration', async () => {
      const data = {};
      httpSpy.post.and.returnValue(Promise.reject('User already exists'));
      const result = await authService.registerUser(data);
      expect(httpSpy.post).toHaveBeenCalledWith('/api/user', data);
      expect(result).toBe(false);
    });

    it('should return true on successful login', async () => {
      const data = { email: 'test@test.com', password: 'password' };
      httpSpy.post.and.returnValue(
        Promise.resolve({ data: 'Successful login' })
      );
      const result = await authService.loginUser(data);
      expect(httpSpy.post).toHaveBeenCalledWith('/api/user/login', data);
      expect(result).toBe(true);
    });

    it('should return false on failed login', async () => {
      const data = { email: 'test@test.com', password: 'password' };

      httpSpy.post.and.returnValue(Promise.reject(new Error()));
      const result = await authService.loginUser(data);
      expect(httpSpy.post).toHaveBeenCalledWith('/api/user/login', data);
      expect(result).toBe(false);
    });

    it('should return User for getUser', async () => {
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

      spyOn(authService, 'getToken').and.returnValue('token');
      httpSpy.get.and.returnValue(Promise.resolve({ data: user }));
      const result = await authService.getUser();
      expect(httpSpy.get).toHaveBeenCalledWith('/api/user/logged');
      expect(result).toBe(user);
    });

    it('should return null on getUser fail', async () => {
      spyOn(authService, 'getToken').and.returnValue(null);
      httpSpy.get.and.returnValue(Promise.resolve({ data: null }));
      const result = await authService.getUser();
      expect(httpSpy.get).not.toHaveBeenCalledWith('/api/user/logged');
      expect(result).toBe(null);
    });
  });
});
