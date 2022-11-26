import { Component, OnInit } from '@angular/core';
import { CurrentRide } from 'src/app/interfaces/Ride';

@Component({
  selector: 'current-ride-page',
  templateUrl: './current-ride-page.component.html',
  styleUrls: ['./current-ride-page.component.css']
})
export class CurrentRidePageComponent implements OnInit {
  currentRide : CurrentRide | null;

  constructor() { 
    this.currentRide = null;
  }

  ngOnInit(): void {
    this.getCurrentRide();
  }

  getCurrentRide(): void {
    //this.currentRide = this.rideService.getCurrentRide();
    this.currentRide = {
      rideId: "1", 
      //client: {id: "1", firstName: "Client", lastName:"Cl", email:"clclclc@gmail.com", profilePicture:"profpic"},
      ridingPals: [ {id: "1", firstName: "One", lastName:"Pal", email:"palone@gmail.com", profilePicture:"profpic"}, {id: "1", firstName: "Tu", lastName:"Pal", email:"paltwo@gmail.com", profilePicture:"profpic"} ],
      isSplitFair: true,
      driver: {id: "1", firstName: "Best", lastName:"Driver", profilePicture:"profpic", email:'bestDriver@gmail.com'},
      destinations:[{index:0, address:"Adresa broj 1"}, {index:1, address:"Adresa broj 2"}],
      price: 1150,
      estimatedTime: "1h 15min",
      vehicleLocation: {longitude: 40.151515615, latitude: 42.156516515},
      minutesToCome: 7,
      isRouteFavorite: false
    }
  }
}
