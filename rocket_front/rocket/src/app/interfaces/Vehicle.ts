import { VehicleType } from './VehicleType';

export interface Vehicle {
  location: { longitude: number; latitude: number };
  type: VehicleType;
}
