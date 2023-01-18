import { Component, OnInit } from '@angular/core';
import { Store } from '@ngrx/store';
import { RouteService } from 'src/app/components/routes/route.service';
import { Destination } from 'src/app/interfaces/Destination';
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
  selectedRoute: Route[] = [];
  constructor(private store: Store<StoreType>, private service: RouteService) {
    this.store.select('destinations').subscribe((resData) => {
      this.destinations = resData.destinations;
      this.estimated_price = resData.estimated_price;
      this.estimated_distance = resData.estimated_route_distance;
      this.estimated_time = resData.estimated_route_time;
      this.selectedRoute = resData.routes.map((mainAlt) => {
        const one = mainAlt[0];
        const two = mainAlt[1];
        if (one && one.selected) return one;
        return two!;
      });
    });
    this.store.select('rideInfo').subscribe((resData) => {
      this.rideInfo = resData.ride;
    });
  }

  ngOnInit(): void {
    this.service.setTrigger('nesto');
  }
}
