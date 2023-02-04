import {Component, OnInit} from '@angular/core';
import {ToastrService} from 'ngx-toastr';
import {RideService} from "../../../services/ride/ride.service";
import {Store} from "@ngrx/store";
import {StoreType} from "../../../shared/store/types";
import {CurrentRideAction, CurrentRideActionType} from "../../../shared/store/current-ride-slice/current-ride.actions";
import {Router} from "@angular/router";

interface FavoriteRoute {
  start: string;
  end: string;
  id: string;
}

@Component({
  selector: 'app-favourite-routes',
  templateUrl: './favourite-routes.component.html',
  styleUrls: ['./favourite-routes.component.css']
})
export class FavouriteRoutesComponent implements OnInit {

  favouriteRoutes: FavoriteRoute[] = []

  focusId: string = '-1'

  constructor(private rideService: RideService,
              private toastr: ToastrService,
              private store: Store<StoreType>,
              private router: Router) {

  }

  setFocus(id: string): void {
    if (this.focusId === id) {
      this.focusId = '-1';
    } else {
      this.focusId = id;
    }
  }

  ngOnInit(): void {

    this.rideService.getFavourites().then(res => {
      this.favouriteRoutes = res;
    })
  }

  deleteFavourite(id: string): void {
    this.rideService.deleteFavourite(id).then(res => {
      window.location.reload();
    })
  }

  bookNow(id: string) {
    this.rideService.bookExisting(id).then(id => {
      if (id === null) {
        this.toastr.error("You already booked a ride.");
        return;
      }


      if (id) {
        this.rideService.getCurrentRide(id).then(currentRide =>{
          if (currentRide) {
            this.store.dispatch(new CurrentRideAction(CurrentRideActionType.SET, currentRide))
            this.router.navigateByUrl("/ride/current").then(r =>
              this.toastr.success("You have booked a ride with you favourite route parameters.")
            );
          }
        })
      }
    })
  }

  showMap(id: string): void {
    this.router.navigate(
      ['/map'],
      { queryParams: { id: id } }
    );
  }
}
