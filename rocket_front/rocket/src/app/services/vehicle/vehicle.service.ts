import {Injectable} from '@angular/core';
import {AxiosResponse} from "axios";
import {http} from "../../shared/api/axios-wrapper";

@Injectable({
  providedIn: 'root'
})
export class VehicleService {

  constructor() {
  }


  async changeStatus(status: 'ACTIVE' | 'INACTIVE' | 'DRIVING'): Promise<boolean> {

    let result: AxiosResponse<boolean> = await http.put<boolean>(
      '/api/vehicle/' + status,
    );
    return <boolean>result.data;

  }
}
