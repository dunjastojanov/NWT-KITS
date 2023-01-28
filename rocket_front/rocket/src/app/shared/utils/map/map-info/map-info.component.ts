import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'map-info',
  templateUrl: './map-info.component.html',
  styleUrls: ['./map-info.component.css'],
})
export class MapInfoComponent implements OnInit {
  @Input('showVehicles') showVehicles?: boolean;
  constructor() {}

  ngOnInit(): void {}
}
