import {AfterViewInit, Component, OnChanges, OnInit} from '@angular/core';
import {Destination} from "../../interfaces/Destination";
import * as L from "leaflet";
import {decode} from "@googlemaps/polyline-codec";
import {ActivatedRoute} from "@angular/router";
import {RideService} from "../../services/ride/ride.service";

@Component({
  selector: 'map-page',
  templateUrl: './map-page.component.html',
  styleUrls: ['./map-page.component.css']
})
export class MapPageComponent implements OnChanges, OnInit, AfterViewInit {
  destinations: Destination[] = [];
  route: string | null = null;

  private mapShow: any;
  layerPolylines: L.LayerGroup | null = null;
  layerVehicle: L.LayerGroup | null = null;

  constructor(private activatedRoute: ActivatedRoute, private service:RideService) {
  }

  ngOnInit(): void {
    this.activatedRoute.queryParams.subscribe(params => {
      let id = params["id"]
      this.service.getMap(id).then(map => {
        this.destinations = map.destinations;
        this.route = map.route;
        console.log(this.destinations)
        console.log(this.route)
        this.showOnMap();
      })
    });
  }

  ngAfterViewInit(): void {
    this.initMap();
    this.showOnMap();
  }

  ngOnChanges(): void {
    if (this.layerPolylines || this.layerVehicle) {
      this.clearMap();
      this.showOnMap();
    }
  }

  private initMap(): void {
    this.mapShow = L.map("routeId", {
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

  private showOnMap() {
    if (this.route && this.destinations) {
      this.drawPolyline();
      this.drawMarkers();
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

    // this.mapShow.fitBounds(mainRoutePolyline.getBounds());
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
