import { Action } from "@ngrx/store";
import { Destination } from "src/app/interfaces/Destination";
import { User } from "src/app/interfaces/User"

export type LoggedUserStateType = {
    user: User | null;
}

export type DestinationsStateType = {
    destinations: Destination[]
}

export type StoreType = {
    loggedUser : LoggedUserStateType
    destinations: DestinationsStateType
}