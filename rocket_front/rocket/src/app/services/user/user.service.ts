import {Injectable} from '@angular/core';
import {AxiosResponse} from 'axios';
import {User} from 'src/app/interfaces/User';
import {http} from 'src/app/shared/api/axios-wrapper';
import {loggedUserToken} from 'src/app/shared/consts';

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
  constructor() {
  }

  async loginUser(data: any): Promise<boolean> {
    try {
      let result: AxiosResponse<string> = await http.post<
        string,
        URLSearchParams
      >('/api/login', data);
      this.setToken(result.data);
      return true;
    } catch (err) {
      return false;
    }
  }

  setToken(token: string) {
    window.localStorage.setItem(loggedUserToken, token);
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

  async getClients(filter: string, number: string, size: number): Promise<any> {
    if (this.getToken()) {
      let result: AxiosResponse = await http.get(
        '/api/user/', {
          params: {
            filter: filter,
            number: number,
            size: size
          }
        }
      );
      return result.data;
    }
    return [];
  }

  async getDrivers(filter: string, number: string, size: number): Promise<any> {
    if (this.getToken()) {
      let result: AxiosResponse = await http.get(
        '/api/vehicle/', {
          params: {
            filter: filter,
            number: number,
            size: size
          }
        }
      );
      return result.data;
    }
    return [];
  }

  async editProfileImage(image: File): Promise<any> {
    if (this.getToken()) {

      const formData = new FormData();
      formData.append('file', image);

      let result: AxiosResponse<any> = await http.put<FormData>(
        '/api/user/image', formData
      );
      return <any>result.data;
    }
    return null;
  }

  async editUser(dto: { firstName: string, lastName: string, city: string, phoneNumber: string }): Promise<any> {
    if (this.getToken()) {
      let result: AxiosResponse<any> = await http.put<object>(
        '/api/user', dto
      );
      return <any>result.data;
    }
    return null;
  }

  async getUserStatistics(dto: { startDate: string, endDate: string }, type: string): Promise<any> {
    if (this.getToken()) {
      let result: AxiosResponse<any> = await http.put<object>(
        '/api/ride/report/' + type, dto
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

  async blockUser(email: string) {
    let result: AxiosResponse<any> = await http.delete<object>(
      '/api/user/' + email
    );
    return result.data;
  }

  async registerDriver(dto: NewDriver) {
    let result: AxiosResponse<any> = await http.post<object>(
      '/api/vehicle', dto
    );
    return result.data;
  }
}
