import { Component, Input, OnInit } from '@angular/core';
import { Store } from '@ngrx/store';
import { RideStatus } from 'src/app/interfaces/Ride';
import { RideService } from 'src/app/services/ride/ride.service';
import {
  CurrentRideAction,
  CurrentRideActionType,
} from 'src/app/shared/store/current-ride-slice/current-ride.actions';
import { StoreType } from 'src/app/shared/store/types';

@Component({
  selector: 'current-ride-buttons',
  templateUrl: './current-ride-buttons.component.html',
  styleUrls: ['./current-ride-buttons.component.css'],
})
export class CurrentRideButtonsComponent implements OnInit {
  @Input('isFavorite') isFavorite: boolean;
  @Input('driverName') driverName!: string | null;
  @Input('rideId') rideId?: number;
  @Input('rideStatus') rideStatus!: RideStatus;

  openReportModal = false;

  openCancelModal = false;

  role: string = '';

  constructor(
    private store: Store<StoreType>,
    private rideService: RideService
  ) {
    this.isFavorite = false;
    this.store.select('loggedUser').subscribe((res) => {
      this.role = res.user!.roles[0];
    });
  }

  ngOnInit(): void {}

  toggleReportModal() {
    this.openReportModal = !this.openReportModal;
  }

  toggleCancelModal() {
    this.openCancelModal = !this.openCancelModal;
  }

  async startRide() {
    await this.rideService.changeRideStatus(this.rideId!, RideStatus.STARTED);
  }

  async endRide() {
    await this.rideService.changeRideStatus(this.rideId!, RideStatus.ENDED);
  }
}
