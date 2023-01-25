import {Injectable} from '@angular/core';
import {AxiosResponse} from "axios";
import {http} from "../../shared/api/axios-wrapper";

@Injectable({
  providedIn: 'root'
})
export class PaypalService {

  constructor() {
  }

  async triggerPaymentExecution(
    paymentId: string,
    payerId: string
  ): Promise<string> {
    let data = {
      payerId: payerId,
      paymentId: paymentId,
    };
    let link: AxiosResponse<string> | void = await http
      .post<string, Object>('/api/payment/confirm', data);
    return link.data;
  }

  async triggerPaymentCreation(amount: number) {
    await http.post<string, number>('/api/payment/create', amount)
  }
}
