import {Component, OnInit} from '@angular/core';
import {RideService} from "../../../services/ride/ride.service";

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
  private rideService: RideService;

  focusId: string = '-1'

  constructor(rideService: RideService) {
    this.rideService = rideService;

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

}
