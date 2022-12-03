import {Component, OnInit} from '@angular/core';
import {User} from "../../interfaces/User";
import {CurrentRide} from "../../interfaces/Ride";
import {NotificationType, ScheduledRideNotification, UpdateDriverNotification} from "../../interfaces/Notification";

@Component({
  selector: 'notification-link',
  templateUrl: './notification-link.component.html',
  styleUrls: ['./notification-link.component.css']
})
export class NotificationLinkComponent implements OnInit {
  isOpen: boolean = false;
  updateDriverNotifications: UpdateDriverNotification[] = [];
  scheduledRideNotifications: ScheduledRideNotification[] = [];
  constructor() {

    let driver: User = {
      city: "Chicago",
      email: "driver@gmail.com",
      firstName: "Kevin",
      id: "4",
      lastName: "Ball",
      profilePicture: "assets/kev.webp",
      role: "driver",
      status: "active",
      phoneNumber: "0684597415"};

    this.updateDriverNotifications.push( {
      date: new Date(),
      driver: driver,
      imageLink: driver.profilePicture,
      read: false,
      text: "Driver " + driver.firstName + " " + driver.lastName + " wants to change their data.",
      type: NotificationType.UpdateDriver
    })


    let ride: CurrentRide =  {
      destinations: [{
        address: "Bulevar Cara Lazara 45", index: 0
      }, {
        address: "Bulevar Kneza Milosa 191", index: 0
      }],
      driver: driver,
      estimatedTime: "25 min",
      minutesToCome: 15,
      price: 400,
      rideId: "",
      ridingPals: [],
      vehicleLocation: {latitude: 0, longitude: 0}

    }

    this.scheduledRideNotifications.push({
      date: new Date(),
      imageLink: driver.profilePicture,
      read: false,
      ride: ride,
      text: "You have a ride scheduled in " + ride.minutesToCome + " minutes",
      type: NotificationType.ScheduledRide
    })


  }
  ngOnInit(): void {
  }

  toggle() {
    this.isOpen = !this.isOpen;
  }

}
