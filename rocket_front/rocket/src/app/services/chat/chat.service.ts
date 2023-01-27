import {Injectable} from '@angular/core';
import {MessageInfo} from "../../interfaces/MessageInfo";
import {AxiosResponse} from "axios";
import {http} from "../../shared/api/axios-wrapper";
import {User} from "../../interfaces/User";
import {LoggedUserAction, LoggedUserActionType} from "../../shared/store/logged-user-slice/logged-user.actions";
import {StoreType} from "../../shared/store/types";
import {Store} from "@ngrx/store";
import {MessageAction, MessageActionType} from "../../shared/store/message-slice/message.actions";
import {UserChatInfo} from "../../interfaces/UserChatInfo";

@Injectable({
  providedIn: 'root'
})
export class ChatService {

  constructor(private store: Store<StoreType>) {
  }

  async loadMessages() {
    await http.get<MessageInfo[]>('/api/chat/message').then(result => {
      this.store.dispatch(new MessageAction(MessageActionType.SET_MESSAGES, result.data));
    });
  }

  async sendMessage(dto: { message: string, receiverEmail: string }): Promise<MessageInfo[]> {
    let result: AxiosResponse<MessageInfo[]> = await http.post<MessageInfo[]>('/api/chat/', dto);
    return result.data;
  }

  async getRandomAdmin(): Promise<User> {
    let result: AxiosResponse<User> = await http.get<User>('/api/user/random/admin');
    return result.data;
  }


}
