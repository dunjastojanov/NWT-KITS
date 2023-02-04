import { Component, OnInit } from '@angular/core';
import { Store } from '@ngrx/store';
import { CurrentRide } from 'src/app/interfaces/Ride';
import { StoreType } from 'src/app/shared/store/types';

@Component({
  selector: 'app-loby',
  templateUrl: './loby.component.html',
  styleUrls: ['./loby.component.css'],
})
export class LobyComponent implements OnInit {
  currentRide: CurrentRide | null = null;

  constructor(private store: Store<StoreType>) {
    this.store.select('currentRide').subscribe((resData) => {
      this.currentRide = resData.currentRide;
    });
  }
  ngOnInit(): void {}
}
