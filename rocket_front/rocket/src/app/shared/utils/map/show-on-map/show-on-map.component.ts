import { AfterViewInit, Component, Input, OnInit } from '@angular/core';
import * as L from 'leaflet';
import { Destination } from 'src/app/interfaces/Destination';
import { Route } from '../map/route.type';

@Component({
  selector: 'show-on-map',
  templateUrl: './show-on-map.component.html',
  styleUrls: ['./show-on-map.component.css'],
})
export class ShowOnMapComponent implements AfterViewInit {
  @Input('dimensions') dimensions!: string;
  @Input('destinations') destinations!: Destination[];
  @Input('route') route!: Route;
  private map: any;
  constructor() {}

  ngAfterViewInit(): void {
    this.initMap();
  }

  private initMap(): void {
    this.map = L.map('map', {
      center: [45.2671, 19.8335],
      zoom: 8,
    });

    const tiles = L.tileLayer(
      'https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png',
      {
        maxZoom: 18,
        minZoom: 3,
      }
    );

    tiles.addTo(this.map);
  }
}
