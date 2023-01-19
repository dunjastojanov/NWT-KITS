import { Destination } from './Destination';
import { sideUser } from './User';
import { VehicleType } from './VehicleType';

export interface CurrentRide {
  rideId: string;
  client?: sideUser;
  ridingPals: sideUser[];
  isSplitFair?: boolean;
  driver: sideUser;
  destinations: Destination[];
  price: number;
  estimatedTime: string;
  vehicleLocation: { longitude: number; latitude: number };
  vehicleType?: VehicleType;
  minutesToCome: number;
  isRouteFavorite?: boolean;
}
