import { Destination } from "./Destination";

export interface RideHistory {
  'start': string,
  'end': string,
  'driver': string,
  'duration': string,
  'price': string,
  'id': string,
  'date':string,
  rideLocation: string,
  destinations: Destination[];
}
