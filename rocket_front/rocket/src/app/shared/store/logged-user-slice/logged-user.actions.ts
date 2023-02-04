import {Action} from "@ngrx/store";
import {User} from "src/app/interfaces/User";

export enum LoggedUserActionType {
  LOGIN = "LOGIN",
  LOGOUT = "LOGOUT"
}

export class LoggedUserAction implements Action {
  constructor(readonly type: string, public payload?: User) {
  }
}
