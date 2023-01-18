import {Destination} from "src/app/interfaces/Destination";
import {User} from "src/app/interfaces/User"

export type LoggedUserStateType = {
  user: User | null;
}

export type DestinationsStateType = {
  destinations: Destination[],
  estimated_route_time: number,
  estimated_route_distance: number,
}

export type StoreType = {
  loggedUser: LoggedUserStateType
  destinations: DestinationsStateType
}
