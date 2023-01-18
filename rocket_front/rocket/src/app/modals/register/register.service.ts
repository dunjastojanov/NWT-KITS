import {Injectable} from '@angular/core';
import {http} from 'src/app/shared/api/axios-wrapper';

@Injectable({
  providedIn: 'root',
})
export class RegisterService {
  constructor() {
  }

  async registerUser(data: any): Promise<boolean> {
    try {
      await http.post<string>('/api/user', data);
      return true;
    } catch (err) {
      return false;
    }
  }
}
