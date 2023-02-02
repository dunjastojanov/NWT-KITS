import { Injectable } from '@angular/core';
import { AxiosResponse } from 'axios';
import { http } from '../../shared/api/axios-wrapper';

@Injectable({
  providedIn: 'root',
})
export class VehicleService {
  constructor() {}

  async changeStatus(
    status: 'ACTIVE' | 'INACTIVE' | 'DRIVING'
  ): Promise<string> {
    let result: AxiosResponse<string> = await http.put<string>(
      '/api/vehicle/' + status
    );
    return <string>result.data;
  }

  async getVehicle() {
    let result: AxiosResponse = await http.get('/api/vehicle/vehicle');
    return result.data;
  }

  async respondDriverDataUpdateRequest(resourceId: string, confirmed: boolean) {
    let result: AxiosResponse = await http.post('/api/vehicle/validate', {
      id: resourceId,
      confirmed: confirmed,
    });
    return result.data;
  }

  async respondDriverImageUpdateRequest(
    resourceId: string,
    confirmed: boolean
  ) {
    let result: AxiosResponse = await http.post('/api/vehicle/validate/image', {
      id: resourceId,
      confirmed: confirmed,
    });
    return result.data;
  }
}
