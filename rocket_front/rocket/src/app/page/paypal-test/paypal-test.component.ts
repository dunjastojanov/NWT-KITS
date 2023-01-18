import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {api} from "../../shared/api/api";
import axios from "axios";

@Component({
  selector: 'app-paypal-test',
  templateUrl: './paypal-test.component.html',
  styleUrls: ['./paypal-test.component.css']
})
export class PaypalTestComponent implements OnInit {

  constructor(private route: ActivatedRoute) {
  }

  ngOnInit() {
    this.route.queryParams.subscribe(params => {
      console.log(params)
      let paymentId = params['paymentId'];
      console.log(paymentId)
      let payerId = params['PayerID'];
      console.log(payerId)
      const data = {
        "paymentId": paymentId,
        "payerId": payerId
      }
      if (paymentId && payerId) {
        axios.post("http://localhost:8443/api/payment/confirm", data).then(value => {
          console.log(value.data);
        })
      }
    });
  }


  trigger() {
    axios.get("http://localhost:8443/api/payment/create").then(value => {
      console.log(value.data)
      window.location.href = value.data
    }).catch(reason => {
      console.log("joooj")
    })
  }

  // openDialog() {
  //   let dialog=dialog.open( <ime_komponente>,{
  //     height: '400px',
  //     width: '600px',
  //   });
  // }
}
