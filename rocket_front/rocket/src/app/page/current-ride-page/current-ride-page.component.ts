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
      ridingPals: [ {id: "1", firstName: "One", lastName:"Pal", email:"palone@gmail.com"}, {id: "1", firstName: "Tu", lastName:"Pal", email:"paltwo@gmail.com"} ],
      isSplitFair: true,
      driver: {id: "1", firstName: "Best", lastName:"Driver", profilePicture:"profpic"},
      startAddress: "Adresa broj 1",
      endAddress: "Adresa broj 2",
      price: 1150,
      estimatedTime: "1h 15min",
      vehicleLocation: {longitude: 40.151515615, latitude: 42.156516515},
      minutesToCome: 7,
      isRouteFavorite: false
    }
  }
}
