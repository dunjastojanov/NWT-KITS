import { Component, OnInit } from '@angular/core';
import {User} from "../../../interfaces/User";

@Component({
  selector: 'app-clients',
  templateUrl: './clients.component.html',
  styleUrls: ['./clients.component.css']
})
export class ClientsComponent implements OnInit {

  constructor() { }

  clients: Array<User> = [
    {
      firstName: "Fiona",
      lastName: "Gallager",
      phoneNumber: "235-47851-96",
      city: "Chicago",
      role: "client",
      profileImage: "assets/fiona.jpg"
    }, ...Array(7).fill({
      firstName: "Fiona",
      lastName: "Gallager",
      phoneNumber: "235-47851-96",
      city: "Chicago",
      role: "client",
      profileImage: "assets/fiona.jpg"
    })
  ];

  ngOnInit(): void {
  }

}
