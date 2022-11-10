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

  openRegisterDriver: boolean = false;

  toggleOpenRegisterDriver = () => {
    this.openRegisterDriver =!this.openRegisterDriver;
  }

  drivers: Array<User> = [{
    firstName: "Kevin",
    lastName: "Ball",
    phoneNumber: "235-47851-96",
    city: "Chicago",
    status: "active",
    role: "driver",
    profileImage: "assets/kev.webp"
    }, ...Array(7).fill(
    {
      firstName: "Kevin",
      lastName: "Ball",
      phoneNumber: "235-47851-96",
      city: "Chicago",
      status: "inactive",
      role: "driver",
      profileImage: "assets/kev.webp"
    }
  )]


  ngOnInit(): void {
  }

}
