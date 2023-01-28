import { Component, Input, OnInit } from '@angular/core';
import { Store } from '@ngrx/store';
import { CurrentRide, UserRidingStatus } from 'src/app/interfaces/Ride';
import { VehiclePrices } from 'src/app/interfaces/VehiclesPrices';
import {
  CurrentRideAction,
  CurrentRideActionType,
} from 'src/app/shared/store/current-ride-slice/current-ride.actions';
import { StoreType } from 'src/app/shared/store/types';

@Component({
  selector: 'current-ride-info',
  templateUrl: './current-ride-info.component.html',
  styleUrls: ['./current-ride-info.component.css'],
})
export class CurrentRideInfoComponent implements OnInit {
  currentRide: CurrentRide | null = null;
  timeForVehicleToArive: number = 0;
  constructor(private store: Store<StoreType>) {
    this.store.select('currentRide').subscribe((resData) => {
      this.currentRide = resData.currentRide;
      this.timeForVehicleToArive = resData.timeForVehicleToArive;
    });
  }

  ngOnInit(): void {}

  public convertTime(): string {
    if (this.currentRide!.time) {
      if (typeof this.currentRide!.time === 'string') {
        const date = this.currentRide!.time.split('T')[0];
        const time = this.currentRide!.time.split('T')[1].split('+')[0];
        return `${date} ${time}`;
      } else {
        const convertedDate = this.currentRide!.time as Date;
        return `${convertedDate.getFullYear()}-${
          convertedDate.getMonth() + 1
        }-${convertedDate.getDate()} ${convertedDate.getHours()}:${convertedDate.getMinutes()}`;
      }
    }
    return 'No time selected';
  }

  convertTimeForVehicleToArive(): string {
    let minutes: number = Math.floor(this.timeForVehicleToArive / 60);
    return `${minutes} min ${(
      this.timeForVehicleToArive -
      minutes * 60
    ).toFixed(0)} s`;
  }

  convertDistance(): string {
    return `${(this.currentRide!.estimatedDistance / 1000).toFixed(1)} km`;
  }

  convertDuration(): string {
    let minutes: number = Math.floor(this.currentRide!.estimatedTime / 60);
    return `${minutes} min ${(
      this.currentRide!.estimatedTime -
      minutes * 60
    ).toFixed(0)} s`;
  }

  convertPrice(): string {
    const priceFixed = +this.currentRide!.price.toFixed(1);
    return `${priceFixed} rsd.`;
  }

  setStatus() {
    if (!this.currentRide!.driver) return UserRidingStatus.WAITING;
    return null;
  }
}
