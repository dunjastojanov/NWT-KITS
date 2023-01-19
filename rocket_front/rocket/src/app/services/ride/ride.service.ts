import {Injectable} from '@angular/core';
import {AxiosResponse} from "axios";
import {http} from "../../shared/api/axios-wrapper";

@Injectable({
  providedIn: 'root'
})
export class RideService {

  constructor() {
  }

  async getRide(id: string): Promise<any | null> {
    let result: AxiosResponse = await http.get(
      '/api/ride/' + id
    );
    return result.data;
  }

  async getFavourites(): Promise<any | null> {
    let result: AxiosResponse = await http.get(
      '/api/ride/favourite'
    );
    return result.data;
  }

  async deleteFavourite(id: string) {
    let result: AxiosResponse = await http.delete(
      '/api/ride/favourite/' + id
    );
    return result.data;
  }

  async addFavorite(id: string) {
    let result: AxiosResponse = await http.post(
      '/api/ride/favourite/' + id
    );
    return result.data;
  }

  async addReview(dto: { vehicleRating: number; description: string; driverRating: number; rideId: string }) {
    let result: AxiosResponse = await http.post(
      '/api/ride/review', dto
    );
    return result.data;
  }
}
