import { Component, Input, OnInit } from '@angular/core';
import {RideService} from "../../../services/ride/ride.service";
import {ToastrService} from "ngx-toastr";
import {User} from "../../../interfaces/User";
import {StoreType} from "../../../shared/store/types";
import {Store} from "@ngrx/store";

@Component({
  selector: 'current-ride-buttons',
  templateUrl: './current-ride-buttons.component.html',
  styleUrls: ['./current-ride-buttons.component.css'],
})
export class CurrentRideButtonsComponent implements OnInit {
  @Input('role') role!: string;
  @Input('isFavorite') isFavorite: boolean;
  @Input('driverName') driverName!: string | null;
  @Input('rideId') rideId!: number | undefined;

  openReportModal = false;

  openCancelModal = false;

  patchingRide = false;
  dissablePatch = false;

  user:User|null=null;

  constructor(private rideService: RideService, private toastr: ToastrService, private store: Store<StoreType>) {
    this.isFavorite = false;

    store.select("loggedUser").subscribe(result => {
      this.user = result.user;
    })
  }

  ngOnInit(): void {}

  hasRole(role: string): boolean {
    return this.user !== null && this.user?.roles.indexOf(role) !== -1;
  }

  toggleReportModal() {
    this.openReportModal = !this.openReportModal;
  }

  toggleCancelModal() {
    this.openCancelModal =!this.openCancelModal;
  }

  togglePatchingRide() {
    if (!this.patchingRide) {
      this.patchingRide = true;
    } else {
      this.dissablePatch = true;
    }
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
