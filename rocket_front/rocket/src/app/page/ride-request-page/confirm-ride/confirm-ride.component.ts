import { Component, OnInit } from '@angular/core';
import { Store } from '@ngrx/store';
import { RouteService } from 'src/app/components/routes/route.service';
import { Destination } from 'src/app/interfaces/Destination';
import { VehiclePrices } from 'src/app/interfaces/VehiclesPrices';
import { StoreType } from 'src/app/shared/store/types';
import { Route } from 'src/app/shared/utils/map/map/route.type';
import { RideInfo } from '../data-info/ride-info.type';

@Component({
  selector: 'app-confirm-ride',
  templateUrl: './confirm-ride.component.html',
  styleUrls: ['./confirm-ride.component.css'],
})
export class ConfirmRideComponent implements OnInit {
  rideInfo: RideInfo = new RideInfo();
  destinations: Destination[] = [];
  estimated_price: number = 0;
  estimated_distance: number = 0;
  estimated_time: number = 0;
  routes: Route[] = [];
  selectedRoute: Route | null = null;
  constructor(private store: Store<StoreType>, private service: RouteService) {
    this.store.select('destinations').subscribe((resData) => {
      this.destinations = resData.destinations;
      this.estimated_price = resData.estimated_price;
      this.estimated_distance = resData.estimated_route_distance;
      this.estimated_time = resData.estimated_route_time;
      this.routes = resData.routes.map((mainAlt) => {
        const one = mainAlt[0];
        const two = mainAlt[1];
        if (one && one.selected) return one;
        return two!;
      });
      this.selectedRoute = this.routes[0];
    });
    this.store.select('rideInfo').subscribe((resData) => {
      this.rideInfo = resData.ride;
    });
  }

  ngOnInit(): void {
    this.service.setTrigger('nesto');
  }
  public convertTime(): string {
    if (this.rideInfo.time) {
      const date = this.rideInfo.time.split('T')[0];
      const time = this.rideInfo.time.split('T')[1].split('+')[0];
      return `${date} ${time}`;
    }
    return 'No time selected';
  }
  public haveDestinations(): boolean {
    return (
      this.destinations.filter((elem) => elem.address !== '').length ===
      this.destinations.length
    );
  }
  convertDistance(): string {
    return `${(this.estimated_distance / 1000).toFixed(1)} km`;
  }

  convertDuration(): string {
    let minutes: number = Math.floor(this.estimated_time / 60);
    return `${minutes} min ${(this.estimated_time - minutes * 60).toFixed(
      0
    )} s`;
  }

  convertPrice(): string {
    const priceFixed = +this.estimated_price.toFixed(1);
    if (this.rideInfo.vehicle)
      return `${VehiclePrices[this.rideInfo.vehicle] + priceFixed} rsd.`;
    return `${priceFixed} rsd`;
  }
}
