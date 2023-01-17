import { Injectable } from '@angular/core';
import { AxiosResponse } from 'axios';
import { User } from 'src/app/interfaces/User';
import { http } from 'src/app/shared/api/axios-wrapper';
import { loggedUserToken } from 'src/app/shared/consts';

@Injectable({
  providedIn: 'root',
})
export class LoginService {
  constructor() {}

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
    let token: string | null = window.localStorage.getItem(loggedUserToken);
    if (token) {
      let result: AxiosResponse<User> = await http.get<User>(
        '/api/user/logged'
      );
      return <User>result.data;
    }
    return null;
  }
}
