import { Injectable } from '@angular/core';
import { AxiosResponse } from 'axios';
import { Destination } from 'src/app/interfaces/Destination';
import { CurrentRide } from 'src/app/interfaces/Ride';
import { sideUser, User } from 'src/app/interfaces/User';
import { RideInfo } from 'src/app/page/ride-request-page/data-info/ride-info.type';
import { Route } from 'src/app/shared/utils/map/map/route.type';
import { http } from '../../shared/api/axios-wrapper';

@Injectable({
  providedIn: 'root',
})
export class RideService {
  constructor() {}

  async validateRide(ride: CurrentRide) {}

  createCurrentRide(
    rideInfo: RideInfo,
    price: number,
    route: Route,
    destinations: Destination[],
    user: User
  ): CurrentRide {
    let ride = new CurrentRide();
    ride.client = {
      firstName: user.firstName,
      lastName: user.lastName,
      email: user.email,
      profilePicture: user.profilePicture,
    };
    ride.isSplitFair = rideInfo.split;
    ride.destinations = destinations;
    ride.route = route;
    ride.price = price;
    ride.estimatedDistance = route.distance;
    ride.estimatedTime = route.duration;
    ride.isRouteFavorite = false;
    ride.ridingPals = rideInfo.friends;
    return ride;
  }
  async getRide(id: string): Promise<any | null> {
    let result: AxiosResponse = await http.get('/api/ride/' + id);
    return result.data;
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
}
