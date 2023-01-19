import { VehicleType } from 'src/app/interfaces/VehicleType';

export class RideInfo {
  public isNow: boolean;
  public time?: string;
  public friends: string[];
  public split: boolean;
  public features: string[];
  public vehicle?: VehicleType;
  constructor() {
    this.isNow = false;
    this.split = false;
    this.friends = [];
    this.features = [];
  }
}
