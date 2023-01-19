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
    console.log(result);
    return result.data;
  }
}
