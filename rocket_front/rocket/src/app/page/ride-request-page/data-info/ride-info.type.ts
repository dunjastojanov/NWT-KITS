import { sideUser } from 'src/app/interfaces/User';
import { VehicleType } from 'src/app/interfaces/VehicleType';

export class RideInfo {
  public isNow: boolean;
  public time?: string;
  public friends: sideUser[];
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
