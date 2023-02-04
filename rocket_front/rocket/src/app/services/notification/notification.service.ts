import { Injectable } from '@angular/core';
import { Store } from '@ngrx/store';
import { AxiosResponse } from 'axios';

import { StoreType } from 'src/app/shared/store/types';
import { http } from '../../shared/api/axios-wrapper';

@Injectable({
  providedIn: 'root',
})
export class NotificationService {
  constructor(private store: Store<StoreType>) {}

  async setRead(id: string) {
    let result: AxiosResponse = await http.put('/api/notification/' + id);
    return result.data;
  }

  async getNotification(): Promise<any> {
    let result: AxiosResponse = await http.get('/api/notification');
    return result.data;
  }
}
