import {Component, Input, OnInit} from '@angular/core';
import {CurrentRide} from 'src/app/interfaces/Ride';

@Component({
  selector: 'current-ride-info',
  templateUrl: './current-ride-info.component.html',
  styleUrls: ['./current-ride-info.component.css']
})
export class CurrentRideInfoComponent implements OnInit {
  @Input('ride') currentRide!: CurrentRide;

  constructor() {
  }

  ngOnInit(): void {

  }

}
