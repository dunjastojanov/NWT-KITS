import { AfterViewInit, Component, Input, OnChanges } from '@angular/core';
import * as L from 'leaflet';
import { Destination } from 'src/app/interfaces/Destination';
import { decode } from '@googlemaps/polyline-codec';
import { Vehicle } from 'src/app/interfaces/Vehicle';
import axios from 'axios';
import { baseUrl } from '../map/route.type';
import {
  CurrentRideAction,
  CurrentRideActionType,
} from 'src/app/shared/store/current-ride-slice/current-ride.actions';
import { StoreType } from 'src/app/shared/store/types';
import { Store } from '@ngrx/store';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'show-on-map',
  templateUrl: './show-on-map.component.html',
  styleUrls: ['./show-on-map.component.css'],
})
export class ShowOnMapComponent implements AfterViewInit, OnChanges {
  @Input('dimensions') dimensions!: string;
  @Input('destinations') destinations!: Destination[];
  @Input('route') route!: string | null;
  @Input('id') id!: string;
  @Input('vehicle') vehicle?: Vehicle;
  @Input('updateVehicleTime') updateVehicleTime?: boolean;
  private mapShow: any;
  layerPolylines: L.LayerGroup | null = null;
  layerVehicle: L.LayerGroup | null = null;
  constructor(private store: Store<StoreType>, private toastr: ToastrService) {}

  async ngAfterViewInit() {
    this.initMap();
    await this.showOnMap();
  }

  async ngOnChanges() {
    if (this.layerPolylines || this.layerVehicle) {
      this.clearMap();
      await this.showOnMap();
    }
  }

  private initMap(): void {
    this.mapShow = L.map(this.id, {
      center: [45.2671, 19.8335],
      zoom: 11,
    });

    const tiles = L.tileLayer(
      'https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png',
      {
        maxZoom: 18,
        minZoom: 3,
      }
    );

    tiles.addTo(this.mapShow);
    this.layerPolylines = L.layerGroup().addTo(this.mapShow);
    this.layerVehicle = L.layerGroup().addTo(this.mapShow);
  }

  private async showOnMap() {
    if (this.route && this.destinations) {
      this.drawPolyline();
      this.drawMarkers();
    }
    if (this.vehicle) {
      await this.drawVehicle();
    }
  }

  private clearMap() {
    this.layerPolylines?.clearLayers();
    this.layerVehicle?.clearLayers();
  }

  private drawPolyline() {
    //const routes = this.route!.split(' ').slice(0, -1);
    // for (let i = 0; i < routes.length; i++) {
    //   const coordinates = decode(routes[i]);
    const coordinates = decode(this.route!);
    const mainRoutePolyline = L.polyline(coordinates, {
      color: '#E1A901',
      weight: 4,
    });
    // if (i === 0) this.bounds = mainRoutePolyline.getBounds();
    // else this.bounds!.extend(mainRoutePolyline.getBounds());

    mainRoutePolyline.addTo(this.layerPolylines!);

    this.mapShow.fitBounds(mainRoutePolyline.getBounds());
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

  private async drawVehicle() {
    const latLng = new L.LatLng(
      this.vehicle!.latitude!,
      this.vehicle!.longitude!
    );
    L.marker(latLng, {
      icon: L.icon({
        iconUrl: 'http://localhost:4200/assets/icons/car-pin.png',
        iconSize: [32, 32],
      }),
    }).addTo(this.layerVehicle!);

    if (this.updateVehicleTime) await this.loadTimeToArrive();
    this.checkArrivedToDestinations();
  }

  checkArrivedToDestinations() {
    const distanceStart = this.haversineDistance(
      this.vehicle!.latitude!,
      this.vehicle!.longitude!,
      this.destinations[0].latitude!,
      this.destinations[0].longitude!,
      6371 * 1000
    );
    if (distanceStart < 25)
      this.toastr.success('Vehicle has arrived to first destination.');
    else {
      const distanceEnd = this.haversineDistance(
        this.vehicle!.latitude!,
        this.vehicle!.longitude!,
        this.destinations[this.destinations.length - 1].latitude!,
        this.destinations[this.destinations.length - 1].longitude!,
        6371 * 1000
      );
      if (distanceEnd < 25)
        this.toastr.success('Vehicle has arrived to final destination.');
    }
  }

  haversineDistance(
    lat1: number,
    lon1: number,
    lat2: number,
    lon2: number,
    radius: number = 6371
  ) {
    const dLat = this.toRadians(lat2 - lat1);
    const dLon = this.toRadians(lon2 - lon1);
    lat1 = this.toRadians(lat1);
    lat2 = this.toRadians(lat2);

    const a =
      Math.sin(dLat / 2) * Math.sin(dLat / 2) +
      Math.cos(lat1) * Math.cos(lat2) * Math.sin(dLon / 2) * Math.sin(dLon / 2);
    const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

    return radius * c;
  }

  toRadians(degrees: number) {
    return degrees * (Math.PI / 180);
  }

  async loadTimeToArrive() {
    await axios
      .get(
        `${baseUrl}${this.vehicle!.longitude!},${this.vehicle!.latitude!};${
          this.destinations[0].longitude
        },${this.destinations[0].latitude}`
      )
      .then((response) => {
        const duration = response.data.routes[0].duration;
        this.store.dispatch(
          new CurrentRideAction(
            CurrentRideActionType.SET_TIME_FOR_VEHICLE_TO_COME,
            duration
          )
        );
      });
  }

  private createMarker(outermost: boolean, latLng: L.LatLng, name: string) {
    if (outermost) {
      L.marker(latLng, {
        icon: L.icon({
          iconUrl: 'http://localhost:4200/assets/icons/pin-fill.png',
          iconSize: [32, 32],
        }),
        title: name,
      }).addTo(this.layerPolylines!);
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
