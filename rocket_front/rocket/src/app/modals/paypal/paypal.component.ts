import {Component, Input, OnInit} from '@angular/core';
import {AxiosResponse} from "axios";
import {http} from "../../shared/api/axios-wrapper";

@Component({
  selector: 'paypal',
  templateUrl: './paypal.component.html',
  styleUrls: ['./paypal.component.css']
})
export class PaypalComponent implements OnInit {
  @Input('open') open!: boolean;
  @Input('closeFunc') closeFunc!: () => void;

  constructor() {
  }

  ngOnInit(): void {
  }

  async triggerPaymentCreation(amount: number): Promise<any> {
    try {
      let link: AxiosResponse<any> | void = await http.post<string, number>('/api/payment/create', amount).then(
        value => {
          this.closeFunc()
          window.location.href = value.data;
        }
      );
      return true;
    } catch (err) {
      return false;
    }
  }

  onClose() {
    this.closeFunc()
    this.open = false;
  }
}
