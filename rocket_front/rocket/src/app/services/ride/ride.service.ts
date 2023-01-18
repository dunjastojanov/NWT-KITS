import { Injectable } from '@angular/core';
import {User} from "../../interfaces/User";
import {AxiosResponse} from "axios";
import {http} from "../../shared/api/axios-wrapper";

@Injectable({
  providedIn: 'root'
})
export class RideService {

  constructor() { }

  async getRide(): Promise<User | null> {
      let result: AxiosResponse = await http.get(
        '/api/ride/1'
      );
      console.log(result)
      return result.data;
  }
}
