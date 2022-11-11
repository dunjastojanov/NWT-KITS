import { stateType } from "../types";
import { LoggedUserAction, LoggedUserActionType } from "./logged-user.actions";

const initialState: stateType = {
    user: null,
   
}

export const loggedUserReducer = (state: stateType = initialState, action: LoggedUserAction) => {
    switch (action.type) {
        case LoggedUserActionType.LOGIN:
            return {
               ...state,
               user : action.payload!,
         
            }

        case LoggedUserActionType.LOGOUT:
            return {
                ...state,
                user : null,
             
            }
        default: {
            return state;
        }
    }
}