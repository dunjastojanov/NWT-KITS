import { Component, Input, OnInit } from '@angular/core';
import {ToastrService} from "ngx-toastr";
import {User} from "../../../interfaces/User";
import {StoreType} from "../../../shared/store/types";
import {Store} from "@ngrx/store";
import { RideStatus } from 'src/app/interfaces/Ride';
import { RideService } from 'src/app/services/ride/ride.service';
import {
  CurrentRideAction,
  CurrentRideActionType,
} from 'src/app/shared/store/current-ride-slice/current-ride.actions';

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
  @Input('driverId') driverId!: string | undefined;

  openReportModal = false;

  openCancelModal = false;

  role: string = '';

  user:User|null=null;

  constructor(private rideService: RideService, private toastr: ToastrService, private store: Store<StoreType>) {
    this.isFavorite = false;
    store.select("loggedUser").subscribe(result => {
      this.user = result.user;
    })
    this.store.select('loggedUser').subscribe((res) => {
      this.role = res.user!.roles[0];
    });
  }

  ngOnInit(): void {}

  hasRole(role: string): boolean {
    return this.user !== null && this.user?.roles.indexOf(role) !== -1;
  }

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

  markAsFavorite() {
    if (this.rideId !== undefined && this.rideId !== -1) {
      this.rideService.addFavorite(this.rideId.toString()).then(
        ()=> {
          this.toastr.success("Added to favorites");
        }
      ).catch((err)=>{
          this.toastr.error(err.message);
      })
    }


  }
}
