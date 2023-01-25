import { Route } from '../shared/utils/map/map/route.type';
import { Destination } from './Destination';
import { RidingPal, sideUser } from './User';
import { Vehicle } from './Vehicle';

export class CurrentRide {
  public rideId?: number;
  public client: sideUser;
  public ridingPals?: RidingPal[];
  public isSplitFair: boolean;
  public driver?: RidingPal;
  public destinations: Destination[];
  public route?: string;
  public price: number;
  public estimatedDistance: number;
  public estimatedTime: number;
  public vehicle?: Vehicle;
  public isRouteFavorite?: boolean;
  public isNow: boolean;
  public time?: string;
  public features: string[];

  constructor() {
    this.client = {
      firstName: '',
      lastName: '',
      email: '',
      profilePicture: '',
    };
    this.isSplitFair = false;
    this.destinations = [];
    this.price = 0;
    this.estimatedDistance = 0;
    this.estimatedTime = 0;
    this.isNow = false;
    this.features = [];
  }
}

export enum UserRidingStatus {
  WAITING = 'WAITING',
  ACCEPTED = 'ACCEPTED',
  DENIED = 'DENIED',
}
