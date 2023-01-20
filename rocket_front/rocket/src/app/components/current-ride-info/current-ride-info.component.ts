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

  constructor(private store: Store<StoreType>) {
    this.store.select('currentRide').subscribe((resData) => {
      this.currentRide = resData.currentRide;
    });
  }

  ngOnInit(): void {}

  setNull() {
    this.store.dispatch(new CurrentRideAction(CurrentRideActionType.REMOVE));
  }

  public convertTime(): string {
    if (this.currentRide!.time) {
      const date = this.currentRide!.time.split('T')[0];
      const time = this.currentRide!.time.split('T')[1].split('+')[0];
      return `${date} ${time}`;
    }
    return 'No time selected';
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
    if (this.currentRide!.vehicle)
      return `${
        VehiclePrices[this.currentRide!.vehicle.type] + priceFixed
      } rsd.`;
    return `From ${priceFixed + 120} to ${priceFixed + 600} rsd`;
  }

  setStatus() {
    if (!this.currentRide!.driver) return UserRidingStatus.WAITING;
    return null;
  }
}
