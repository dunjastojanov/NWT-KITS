import {MessageStateType} from '../types';
import {
  MessageAction,
  MessageActionType,
} from './message.actions';
import {MessageInfo} from "../../../interfaces/MessageInfo";

const initialState: MessageStateType = {
  messages: [],
};

export const MessagesReducer = (
  state: MessageStateType = initialState,
  action: MessageAction
) => {
  switch (action.type) {
    case MessageActionType.SET_MESSAGES:
      return {
        ...state,
        messages: <MessageInfo[]>action.payload!,
      };
    default: {
      return state;
    }
  }
};
