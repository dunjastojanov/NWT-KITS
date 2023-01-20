import { Route } from '../shared/utils/map/map/route.type';
import { Destination } from './Destination';
import { sideUser } from './User';
import { Vehicle } from './Vehicle';

export class CurrentRide {
  public rideId?: number;
  public client?: sideUser;
  public ridingPals?: sideUser[];
  public isSplitFair: boolean;
  public driver?: sideUser;
  public destinations: Destination[];
  public route?: Route;
  public price: number;
  public estimatedDistance: number;
  public estimatedTime: number;
  public vehicle?: Vehicle;
  public isRouteFavorite?: boolean;

  constructor() {
    this.isSplitFair = false;
    this.destinations = [];
    this.price = 0;
    this.estimatedDistance = 0;
    this.estimatedTime = 0;
  }
}
