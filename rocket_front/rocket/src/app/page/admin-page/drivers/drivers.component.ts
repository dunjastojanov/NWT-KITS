import {Component, OnInit} from '@angular/core';
import {User} from "../../../interfaces/User";


@Component({
  selector: 'app-drivers',
  templateUrl: './drivers.component.html',
  styleUrls: ['./drivers.component.css']
})
export class DriversComponent implements OnInit {

  constructor() {
  }

  drivers: Array<User> = [{
    firstName: "Kevin",
    lastName: "Ball",
    phoneNumber: "235-47851-96",
    city: "Chicago",
    status: "active"
    }, ...Array(7).fill(
    {
      firstName: "Kevin",
      lastName: "Ball",
      phoneNumber: "235-47851-96",
      city: "Chicago",
      status: "inactive"
    }
  )]


  ngOnInit(): void {
  }

}
