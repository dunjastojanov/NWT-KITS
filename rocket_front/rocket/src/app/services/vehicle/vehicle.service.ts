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

  async getVehicle() {
    let result: AxiosResponse = await http.get(
      '/api/vehicle/vehicle',
    );
    return result.data;

  }

  async respondDriverDataUpdateRequest(resourceId: string, confirmed: boolean) {
    let result: AxiosResponse = await http.post(
      '/api/vehicle/validate', {id: resourceId, confirmed: confirmed}
    );
    return result.data;
  }

  async respondDriverImageUpdateRequest(resourceId: string, confirmed: boolean) {
    let result: AxiosResponse = await http.post(
      '/api/vehicle/validate/image', {id: resourceId, confirmed: confirmed}
    );
    return result.data;
  }
}
