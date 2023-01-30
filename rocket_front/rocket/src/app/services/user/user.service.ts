import { Injectable } from '@angular/core';
import { AxiosResponse } from 'axios';
import { sideUser, User } from 'src/app/interfaces/User';
import { http } from 'src/app/shared/api/axios-wrapper';
import { loggedUserToken } from 'src/app/shared/consts';
import { CookieService } from 'ngx-cookie-service';
import { CurrentRide } from 'src/app/interfaces/Ride';
import { GoogleUser } from '../../interfaces/GoogleUser';
import { Store } from '@ngrx/store';
import { StoreType } from '../../shared/store/types';
import {
  LoggedUserAction,
  LoggedUserActionType,
} from '../../shared/store/logged-user-slice/logged-user.actions';

interface NewDriver {
  firstName: string;
  lastName: string;
  phoneNumber: string;
  city: string;
  kidFriendly: boolean;
  petFriendly: boolean;
  email: string;
  vehicleType: string;
}

@Injectable({
  providedIn: 'root',
})
export class UserService {
  constructor(
    private cookieService: CookieService,
    private store: Store<StoreType>
  ) {}

  async loginUser(data: any): Promise<boolean> {
    try {
      let result: AxiosResponse<string> = await http.post<
        string,
        URLSearchParams
      >('/api/user/login', data);
      this.setToken(result.data);
      return true;
    } catch (err) {
      return false;
    }
  }

  setToken(token: string) {
    window.localStorage.setItem(loggedUserToken, token);
    this.cookieService.set('access_token', token);
  }

  async getUser(): Promise<User | null> {
    if (this.getToken()) {
      let result: AxiosResponse<User> = await http.get<User>(
        '/api/user/logged'
      );
      return <User>result.data;
    }
    return null;
  }

  async getCurrentRide(email: string): Promise<CurrentRide | null> {
    if (this.getToken()) {
      let result: AxiosResponse<CurrentRide | null> = await http.get(
        `/api/ride/currentRide/${email}`
      );
      return <CurrentRide>result.data;
    }
    return null;
  }

  async getClients(filter: string, number: string, size: number): Promise<any> {
    if (this.getToken()) {
      let result: AxiosResponse = await http.get('/api/user/', {
        params: {
          filter: filter,
          number: number,
          size: size,
        },
      });
      return result.data;
    }
    return [];
  }

  async getDrivers(filter: string, number: string, size: number): Promise<any> {
    if (this.getToken()) {
      let result: AxiosResponse = await http.get('/api/vehicle/', {
        params: {
          filter: filter,
          number: number,
          size: size,
        },
      });
      return result.data;
    }
    return [];
  }

  async editProfileImage(image: File): Promise<any> {
    if (this.getToken()) {
      const formData = new FormData();
      formData.append('file', image);

      let result: AxiosResponse<any> = await http.put<FormData>(
        '/api/user/image',
        formData
      );
      return <any>result.data;
    }
    return null;
  }

  async editUser(dto: {
    firstName: string;
    lastName: string;
    city: string;
    phoneNumber: string;
  }): Promise<any> {
    if (this.getToken()) {
      let result: AxiosResponse<any> = await http.put<object>('/api/user', dto);
      return <any>result.data;
    }
    return null;
  }

  async editDriver(dto: {
    firstName: string;
    lastName: string;
    city: string;
    phoneNumber: string;
  }): Promise<any> {
    if (this.getToken()) {
      let result: AxiosResponse<any> = await http.put<object>(
        '/api/vehicle',
        dto
      );
      return <any>result.data;
    }
    return null;
  }

  async getUserStatistics(
    dto: { startDate: string; endDate: string },
    type: string
  ): Promise<any> {
    if (this.getToken()) {
      let result: AxiosResponse<any> = await http.put<object>(
        '/api/ride/report/' + type,
        dto
      );
      return <any>result.data;
    }
    return null;
  }

  private getToken() {
    let token: string | null = window.localStorage.getItem(loggedUserToken);
    return token;
  }

  async getProfileHistory(currentPage: string): Promise<any> {
    if (this.getToken()) {
      let result: AxiosResponse<any> = await http.get<object>(
        '/api/ride/8/' + (+currentPage - 1).toString()
      );
      return result.data;
    }
    return null;
  }

  async blockUser(email: string, message: string) {
    let result: AxiosResponse<any> = await http.post<object>(
      '/api/user/block/' + email,
      message
    );
    return result.data;
  }

  async registerDriver(dto: NewDriver) {
    let result: AxiosResponse<any> = await http.post<object>(
      '/api/vehicle',
      dto
    );
    return result.data;
  }

  async getRidingPal(email: string): Promise<sideUser | string> {
    try {
      let result: AxiosResponse<any> = await http.get(`/api/ride/pal/${email}`);
      return result.data;
    } catch (err: any) {
      if (err.message === 'Forbidden')
        return `User ${email} already has scheduled ride.`;
      else return `There is no user with email ${email}`;
    }
  }

  async changePassword(dto: { oldPassword: string; newPassword: string }) {
    let result: AxiosResponse<any> = await http.put<object>(
      '/api/user/password',
      dto
    );
    return result.data;
  }

  async cancelRide(rideId: string, message: string) {
    let result: AxiosResponse<any> = await http.post<object>(
      '/api/ride/cancel/' + rideId,
      message
    );
    return result.data;
  }

  async sendRequestForPassword(email: string) {
    await http.post<string>('/api/user/password', email, {
      headers: {
        'Content-Type': 'text/plain',
      },
    });
  }

  async forgottenPasswordChangeConfirmation(data: {
    token: string;
    password: string;
  }): Promise<string> {
    let result: AxiosResponse<string> = await http.post<string, object>(
      '/api/user/confirm',
      data
    );
    return result.data;
  }

  async verifyRegistration(token: string): Promise<string> {
    let result: AxiosResponse<string> = await http.get<string>(
      '/api/user/confirm/' + token
    );
    return result.data;
  }

  async loginGoogleUser(user: User): Promise<string> {
    const googleDto: GoogleUser = {
      id: user.id,
      email: user.email,
      firstName: user.firstName,
      lastName: user.lastName,
      profilePicture: user.profilePicture,
    };
    let result: AxiosResponse<string> = await http.post<string>(
      '/api/user/google',
      googleDto
    );
    return result.data;
  }

  refreshUser() {
    this.getUser().then((user) => {
      if (user) {
        this.store.dispatch(
          new LoggedUserAction(LoggedUserActionType.LOGIN, user)
        );
      }
    });
  }
}
