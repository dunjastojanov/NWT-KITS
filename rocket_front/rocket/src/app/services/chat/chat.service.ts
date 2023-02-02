import { Injectable } from '@angular/core';
import { MessageInfo } from '../../interfaces/MessageInfo';
import { AxiosResponse } from 'axios';
import { http } from '../../shared/api/axios-wrapper';
import { User } from '../../interfaces/User';
import { StoreType } from '../../shared/store/types';
import { Store } from '@ngrx/store';

@Injectable({
  providedIn: 'root',
})
export class ChatService {
  constructor(private store: Store<StoreType>) {}

  async sendMessage(dto: {
    message: string;
    receiverEmail: string;
  }): Promise<MessageInfo[]> {
    let result: AxiosResponse<MessageInfo[]> = await http.post<MessageInfo[]>(
      '/api/chat/',
      dto
    );
    return result.data;
  }

  async getRandomAdmin(): Promise<User> {
    let result: AxiosResponse<User> = await http.get<User>(
      '/api/user/random/admin'
    );
    return result.data;
  }
}
