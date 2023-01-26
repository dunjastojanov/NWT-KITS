import { AfterViewInit, Component, Input } from '@angular/core';
import * as L from 'leaflet';
import { Destination } from 'src/app/interfaces/Destination';
import { decode } from '@googlemaps/polyline-codec';
import { Vehicle } from 'src/app/interfaces/Vehicle';

@Component({
  selector: 'show-on-map',
  templateUrl: './show-on-map.component.html',
  styleUrls: ['./show-on-map.component.css'],
})
export class ShowOnMapComponent implements AfterViewInit {
  @Input('dimensions') dimensions!: string;
  @Input('destinations') destinations!: Destination[];
  @Input('route') route!: string | null;
  @Input('id') id!: string;
  @Input('vehicle') vehicle?: Vehicle;
  private mapShow: any;
  bounds: L.LatLngBounds | null = null;
  constructor() {}

  ngAfterViewInit(): void {
    this.initMap();
    this.showOnMap();
  }

  private initMap(): void {
    this.mapShow = L.map(this.id, {
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

    tiles.addTo(this.mapShow);
  }

  private showOnMap() {
    if (this.route && this.destinations) {
      this.drawPolyline();
      this.drawMarkers();
    }
    if (this.vehicle) {
      this.drawVehicle();
    }
  }

  private drawPolyline() {
    const routes = this.route!.split(' ').slice(0, -1);
    for (let i = 0; i < routes.length; i++) {
      const coordinates = decode(routes[i]);
      const mainRoutePolyline = L.polyline(coordinates, {
        color: '#E1A901',
        weight: 4,
      });
      if (i === 0) this.bounds = mainRoutePolyline.getBounds();
      else this.bounds!.extend(mainRoutePolyline.getBounds());
      mainRoutePolyline.addTo(this.mapShow);
    }
    this.mapShow.fitBounds(this.bounds);
  }

  private drawMarkers() {
    this.destinations.map(async (elem, index, array) => {
      let outermost = index === 0 || index === array.length - 1;
      this.createMarker(
        outermost,
        new L.LatLng(elem.latitude!, elem.longitude!),
        elem.address
      );
    });
  }

  private drawVehicle() {
    const latLng = new L.LatLng(
      this.vehicle!.latitude!,
      this.vehicle!.longitude!
    );
    console.log(this.vehicle);

    L.marker(latLng, {
      icon: L.icon({
        iconUrl: 'http://localhost:4200/assets/icons/car-pin.png',
        iconSize: [32, 32],
      }),
    }).addTo(this.mapShow);
  }

  private createMarker(outermost: boolean, latLng: L.LatLng, name: string) {
    if (outermost) {
      L.marker(latLng, {
        icon: L.icon({
          iconUrl: 'http://localhost:4200/assets/icons/pin-fill.png',
          iconSize: [32, 32],
        }),
        title: name,
      }).addTo(this.mapShow);
    } else {
      L.marker(latLng, {
        icon: L.icon({
          iconUrl: 'http://localhost:4200/assets/icons/pin-fill-black.png',
          iconSize: [23, 23],
        }),
        title: name,
      }).addTo(this.mapShow);
    }
  }
}
