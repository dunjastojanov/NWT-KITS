import {Action} from '@ngrx/store';
import {MessageInfo} from "../../../interfaces/MessageInfo";

export enum MessageActionType {
  SET_MESSAGES = 'SET_MESSAGES',

}

export class MessageAction implements Action {
  constructor(readonly type: string, public payload: MessageInfo[]) {
  }
}
