import {Injectable} from '@angular/core';
import {AxiosResponse} from "axios";
import {http} from "../../shared/api/axios-wrapper";
import {loggedUserToken} from "../../shared/consts";

@Injectable({
  providedIn: 'root'
})
export class AdminService {

  constructor() {
  }

  private getToken() {
    let token: string | null = window.localStorage.getItem(loggedUserToken);
    return token;
  }

  async getHistory(currentPage: string): Promise<any> {
    if (this.getToken()) {
      let result: AxiosResponse<any> = await http.get<object>(
        '/api/ride/all/8/' + (+currentPage - 1).toString()
      );
      return <any>result.data;
    }
    return null;
  }

  async getHistoryForEmail(currentPage: string, email: string): Promise<any> {
    if (this.getToken()) {
      let result: AxiosResponse<any> = await http.get<object>(
        '/api/ride/8/' + (+currentPage - 1).toString() + '/' + email
      );
      return <any>result.data;
    }
    return null;
  }

  async getStatistics(dto: { startDate: string, endDate: string }, type: string): Promise<any> {
    if (this.getToken()) {
      let result: AxiosResponse<any> = await http.put<object>(
        '/api/ride/report/all/' + type, dto
      );
      return <any>result.data;
    }
    return null;
  }
}
