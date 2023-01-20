import {Injectable} from '@angular/core';
import {AxiosResponse} from "axios";
import {http} from "../../shared/api/axios-wrapper";

@Injectable({
  providedIn: 'root'
})
export class NotificationService {

  constructor() { }

  async getNotification(): Promise<any> {
    let result: AxiosResponse = await http.get(
      '/api/notification'
    );
    return result.data;
  }

  async setRead(id: string) {
    let result: AxiosResponse = await http.put(
      '/api/notification/' + id
    );
    console.log(result.data);
    return result.data;
  }
}
