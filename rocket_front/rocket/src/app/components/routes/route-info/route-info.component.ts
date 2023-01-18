import { Component, OnInit } from '@angular/core';
import { Store } from '@ngrx/store';
import { StoreType } from 'src/app/shared/store/types';

@Component({
  selector: 'route-info',
  templateUrl: './route-info.component.html',
  styleUrls: ['./route-info.component.css'],
})
export class RouteInfoComponent implements OnInit {
  distance: number = 0;
  time: number = 0;
  price: number = 0;
  constructor(private store: Store<StoreType>) {
    store.select('destinations').subscribe((res) => {
      this.distance = res.estimated_route_distance;
      this.time = res.estimated_route_time;
      this.price = res.estimated_price;
    });
  }

  ngOnInit(): void {}

  show(): boolean {
    return this.distance > 0 && this.time > 0;
  }

  distanceOutput(): string {
    return `${(this.distance / 1000).toFixed(1)} km`;
  }

  timeOutput(): string {
    let minutes: number = Math.floor(this.time / 60);
    return `${minutes} min ${(this.time - minutes * 60).toFixed(0)} s`;
  }

  priceOutput(): string {
    return `${this.price.toFixed(1)} €`;
  }
}
