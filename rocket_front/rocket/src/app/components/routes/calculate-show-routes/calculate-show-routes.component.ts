import { Component, OnInit } from '@angular/core';
import { Store } from '@ngrx/store';
import { Destination } from 'src/app/interfaces/Destination';
import { StoreType } from 'src/app/shared/store/types';
import { RouteService } from '../route.service';

@Component({
  selector: 'calculate-show-routes',
  templateUrl: './calculate-show-routes.component.html',
  styleUrls: ['./calculate-show-routes.component.css']
})
export class CalculateShowRoutesComponent implements OnInit {

  destinations: Destination[] = [];
  openErrorToast = false;
  constructor(private store: Store<StoreType> ,private service: RouteService ) {
    store.select('destinations').subscribe( res => {
      this.destinations = res.destinations;
    })
   }

  ngOnInit(): void {
  }

  onShow() {
    if (this.destinations.filter(elem => elem.address !== '').length === this.destinations.length)
      this.service.setTrigger('trigger');
    else {
      this.openErrorToast = true;
    }
  }

  toggleErrorToast = () => {
    this.openErrorToast =!this.openErrorToast;
  }
}
