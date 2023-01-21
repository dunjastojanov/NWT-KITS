import {Injectable} from '@angular/core';
import {AxiosResponse} from "axios";
import {http} from "../../shared/api/axios-wrapper";
import {loggedUserToken} from "../../shared/consts";
import {sideUser} from "../../interfaces/User";
import {UserChatInfo} from "../../interfaces/UserChatInfo";
import {MessageInfo} from "../../interfaces/MessageInfo";

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

  async getStatistics(dto: { startDate: string, endDate: string }, type: string): Promise<any> {
    if (this.getToken()) {
      let result: AxiosResponse<any> = await http.put<object>(
        '/api/ride/report/all/' + type, dto
      );
      return <any>result.data;
    }
    return null;
  }

  async getAdminData() {
    let result: AxiosResponse<sideUser | null> = await http.get<sideUser | null>('/api/user/logged');
    return result.data;
  }

  async getAllAdminsChat(): Promise<UserChatInfo[]> {
    let result: AxiosResponse<UserChatInfo[]> = await http.get<UserChatInfo[]>('/api/chat');
    return result.data;
  }

  async getMessagesWith(receiverEmail: string): Promise<MessageInfo[]> {
    let result: AxiosResponse<MessageInfo[]> = await http.get<MessageInfo[]>('/api/chat/message/' + receiverEmail);
    return result.data;
  }

  async sendMessage(dto: { message: string, receiverEmail: string }): Promise<MessageInfo[]> {
    let result: AxiosResponse<MessageInfo[]> = await http.post<MessageInfo[]>('/api/chat/', dto);
    return result.data;
  }
}
