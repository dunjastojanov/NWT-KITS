import { Component, Input, OnInit } from '@angular/core';
import { Store } from '@ngrx/store';
import { CurrentRide } from 'src/app/interfaces/Ride';
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
}
