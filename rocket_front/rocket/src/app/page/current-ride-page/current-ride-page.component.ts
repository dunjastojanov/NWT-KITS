import { Component, OnInit } from '@angular/core';
import { Store } from '@ngrx/store';
import { CurrentRide } from 'src/app/interfaces/Ride';
import { StoreType } from 'src/app/shared/store/types';

@Component({
  selector: 'current-ride-page',
  templateUrl: './current-ride-page.component.html',
  styleUrls: ['./current-ride-page.component.css'],
})
export class CurrentRidePageComponent implements OnInit {
  currentRide: CurrentRide | null = null;
  role: string = '';

  constructor(private store: Store<StoreType>) {
    this.store.select('currentRide').subscribe((resData) => {
      this.currentRide = resData.currentRide;
    });
    this.store.select('loggedUser').subscribe((res) => {
      this.role = res.user!.roles[0];
    });
  }

  ngOnInit(): void {}
}
