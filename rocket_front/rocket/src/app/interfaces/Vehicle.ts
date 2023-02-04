import { VehicleType } from './VehicleType';

export interface Vehicle {
  longitude?: number;
  latitude?: number;
  type: VehicleType;
}

export interface ActiveVehicle {
  id: number;
  longitude: number;
  latitude: number;
  free: boolean;
}
