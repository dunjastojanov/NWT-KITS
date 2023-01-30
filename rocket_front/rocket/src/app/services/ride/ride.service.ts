import { Injectable } from '@angular/core';
import { Store } from '@ngrx/store';
import { AxiosResponse } from 'axios';
import { ToastrService } from 'ngx-toastr';
import { Destination } from 'src/app/interfaces/Destination';
import {
  CurrentRide,
  RideStatus,
  UserRidingStatus,
} from 'src/app/interfaces/Ride';
import { User } from 'src/app/interfaces/User';
import { RideInfo } from 'src/app/page/ride-request-page/data-info/ride-info.type';
import {
  CurrentRideAction,
  CurrentRideActionType,
} from 'src/app/shared/store/current-ride-slice/current-ride.actions';
import { StoreType } from 'src/app/shared/store/types';
import { http } from '../../shared/api/axios-wrapper';

@Injectable({
  providedIn: 'root',
})
export class RideService {
  currentRide: CurrentRide | null = null;
  constructor(private store: Store<StoreType>, private toastr: ToastrService) {
    this.store.select('currentRide').subscribe((data) => {
      this.currentRide = data.currentRide;
    });
  }

  async validateRide(ride: CurrentRide) {}

  createCurrentRide(
    rideInfo: RideInfo,
    distance: number,
    duration: number,
    price: number,
    route: string,
    destinations: Destination[],
    user: User
  ): CurrentRide {
    let ride = new CurrentRide();
    ride.client = {
      firstName: user.firstName,
      lastName: user.lastName,
      email: user.email,
      profilePicture: user.profilePicture,
      role: user.roles[0],
    };
    ride.isSplitFair = rideInfo.split;
    ride.destinations = destinations;
    ride.route = route;
    ride.price = price;
    ride.estimatedDistance = distance;
    ride.estimatedTime = duration;
    ride.isRouteFavorite = false;
    ride.vehicle = { type: rideInfo.vehicle! };
    ride.ridingPals = rideInfo.friends.map((fr) => {
      return { ...fr, status: UserRidingStatus.WAITING };
    });
    ride.isNow = rideInfo.isNow;
    ride.time = rideInfo.time;
    ride.features = rideInfo.features;
    return ride;
  }
  async getRide(id: string): Promise<any | null> {
    let result: AxiosResponse = await http.get('/api/ride/' + id);
    return result.data;
  }

  async saveCurrentRide(currentRide: CurrentRide) {
    try {
      let result: AxiosResponse<number> = await http.post(
        `/api/ride/currentRide`,
        currentRide
      );
      return result.data;
    } catch {
      return -1;
    }
  }

  async getFavourites(): Promise<any | null> {
    let result: AxiosResponse = await http.get('/api/ride/favourite');
    return result.data;
  }

  async deleteFavourite(id: string) {
    let result: AxiosResponse = await http.delete('/api/ride/favourite/' + id);
    return result.data;
  }

  async addFavorite(id: string) {
    let result: AxiosResponse = await http.post('/api/ride/favourite/' + id);
    return result.data;
  }

  async addReview(dto: {
    vehicleRating: number;
    description: string;
    driverRating: number;
    rideId: string;
  }) {
    let result: AxiosResponse = await http.post('/api/ride/review', dto);
    return result.data;
  }

  async getCurrentRide(id: number) {
    let result: AxiosResponse<CurrentRide | null> = await http.get(
      `/api/ride/currentRideId/${id}`
    );
    return result.data;
  }

  async createNotifsLookForDriver(id: number) {
    await http.get(`/api/ride/post-create-ride/${id}`);
  }
  async onRideStatusChanged() {
    if (!this.currentRide) return;
    if (this.currentRide.rideStatus === RideStatus.DENIED) {
      //ovo ne ide driveru
      this.store.dispatch(new CurrentRideAction(CurrentRideActionType.REMOVE));
      this.toastr.error('Ride is denied. You can try to book ride again.');
    }
    if (this.currentRide.rideStatus === RideStatus.CONFIRMED) {
      //pokreni simulaciju
      //skini novac
      //posalji notifikaciju korisnicima
    }
    if (
      this.currentRide.ridingPals &&
      this.currentRide.rideStatus === RideStatus.REQUESTED
    ) {
      if (
        this.currentRide.ridingPals.length ===
        this.currentRide.ridingPals.filter(
          (pal) => pal.status! === UserRidingStatus.ACCEPTED
        ).length
      ) {
        this.toastr.success('Looking for driver, please wait');
      }
    }
    if (this.currentRide.rideStatus === RideStatus.STARTED) {
      // sacuvaj u bazu da je ride started i promeni start time
    }
    if (this.currentRide.rideStatus === RideStatus.ENDED) {
      // sacuvaj u bazu da je ride ended i promeni end time i current ride = null
    }
  }

  async bookExisting(rideId: string) {
    let result: AxiosResponse<number | null> = await http.post(
      `/api/ride/currentRide/${rideId}`
    );
    return result.data;
  }

  async findDriver(id: number) {
    let result: AxiosResponse<boolean | null> = await http.put(
      `/api/ride/currentRide/driver/${id}`
    );
    return result.data;
  }

  async getMap(id: string) {
    let result:AxiosResponse = await http.get(
      "/api/ride/map/" + id);
    return result.data;
  }

  async report(driverId: string) {
    let result:AxiosResponse = await http.post(
      "/api/vehicle/report/driver/" + driverId);
    return result.data;
  }
}
