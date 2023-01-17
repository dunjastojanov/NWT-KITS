import { AfterViewInit, Component, Input } from '@angular/core';
import { Store } from '@ngrx/store';
import axios from 'axios';
import * as L from 'leaflet';
import * as lrm from 'leaflet-routing-machine';
import { RouteService } from 'src/app/components/routes/route.service';
import { Destination } from 'src/app/interfaces/Destination';
import {
  DestinationsAction,
  DestinationsActionType,
} from 'src/app/shared/store/destinations-slice/destinations.actions';
import { DestinationsStateType, StoreType } from 'src/app/shared/store/types';
import { decode, encode, LatLngTuple } from '@googlemaps/polyline-codec';
import { Route } from './route.type';
import { Observable, Subscription } from 'rxjs';

@Component({
  selector: 'app-map',
  templateUrl: './map.component.html',
  styleUrls: ['./map.component.css'],
})
export class MapComponent implements AfterViewInit {
  @Input('height') height!: string;
  private map: any;
  baseUrl = 'http://router.project-osrm.org/route/v1/car/';
  options = {
    alternatives: 'true',
    geometries: 'geojson',
  };
  bounds: L.LatLngBounds | null = null;
  estimatedDistance: number = 0;
  estimatedTime: number = 0;
  destinations: { latitude: number; longitude: number }[] = [];
  destObj: Destination[] = [];
  layerPolylines: L.LayerGroup | null = null;
  layerMarkers: L.LayerGroup | null = null;
  routes: [Route, Route?][] = [];
  openErrorToast = false;
  error: boolean = false;
  private initMap(): void {
    lrm;
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
    this.layerPolylines = L.layerGroup().addTo(this.map);
    this.layerMarkers = L.layerGroup().addTo(this.map);
  }

  ngOnDestroy() {
    this.subscription.unsubscribe();
  }

  subscription: Subscription;

  constructor(private service: RouteService, private store: Store<StoreType>) {
    this.subscription = this.store
      .select('destinations')
      .subscribe((resData) => {
        this.destObj = resData.destinations;
        this.estimatedDistance = resData.estimated_route_distance;
        this.estimatedTime = resData.estimated_route_time;
        this.routes = resData.routes;
      });

    this.store.dispatch(
      new DestinationsAction(DestinationsActionType.RESET, '')
    );

    this.service.trigger$().subscribe((x) => {
      if (x === 'trigger') {
        this.showDestinations();
      }
    });
  }

  ngAfterViewInit(): void {
    this.initMap();
  }

  showDestinations() {
    if (this.routes.length > 0) {
      return;
    }
    this.clearMap(true);
    this.destinations = [];
    this.error = false;
    if (
      this.destObj.filter((elem) => elem.address !== '').length ===
      this.destObj.length
    ) {
      let destPromises = this.destObj.map(async (elem, index, array) => {
        let outermost = index === 0 || index === array.length - 1;
        return await this.getLatLong(outermost, elem.address);
      });
      Promise.all(destPromises).then((promises) =>
        promises.map((promise) => {
          this.destinations.push(promise);
          if (this.destinations.length === promises.length) this.createRoutes();
        })
      );
    }
  }

  async createRoutes() {
    if (this.error) {
      this.toggleErrorToast();
      return;
    }
    for (let i = 0; i < this.destinations.length - 1; i++) {
      await this.osrm(this.destinations[i], this.destinations[i + 1]);
    }
    this.drawPolylines();
    this.calculateRouteInfo();
  }

  async osrm(
    start: { latitude: number; longitude: number },
    end: { latitude: number; longitude: number }
  ) {
    const url = `${this.baseUrl}${start.longitude},${start.latitude};${
      end.longitude
    },${end.latitude}?${Object.keys(this.options)
      .map((key) => `${key}=${this.options[key as keyof typeof this.options]}`)
      .join('&')}`;

    await axios.get(url).then((response) => {
      const routes = response.data['routes'];

      const mainRoute: Route = routes[0];
      mainRoute.selected = true;
      const alternativeRoute: Route = routes[1];
      if (alternativeRoute) alternativeRoute.selected = false;

      if (alternativeRoute) {
        this.store.dispatch(
          new DestinationsAction(DestinationsActionType.ADD_ROUTE, [
            mainRoute,
            alternativeRoute,
          ])
        );
      } else {
        this.store.dispatch(
          new DestinationsAction(DestinationsActionType.ADD_ROUTE, [mainRoute])
        );
      }
    });
  }

  drawPolylines() {
    /*this.store.dispatch(
      new DestinationsAction(DestinationsActionType.ADD_ROUTE_DISTANCE, 0)
    );
    this.store.dispatch(
      new DestinationsAction(DestinationsActionType.ADD_ROUTE_TIME, 0)
    );*/
    for (let i = 0; i < this.routes.length; i++) {
      let oneRoute: Route = this.routes[i][0];
      let oneCoordinates = this.getCoordinates(oneRoute);
      if (oneRoute.selected) {
        this.drawMainPolyline(oneCoordinates);
      } else {
        this.drawAlternativePolyline(oneCoordinates, this.routes[i]);
      }

      const polyline = this.routes[i][1];
      if (polyline) {
        let secondRoute: Route = polyline;
        let secondCoordinates = this.getCoordinates(secondRoute);
        if (secondRoute.selected) {
          this.drawMainPolyline(secondCoordinates);
        } else {
          this.drawAlternativePolyline(secondCoordinates, this.routes[i]);
        }
      }
    }
    this.map.fitBounds(this.bounds);
  }

  getCoordinates(route: { geometry: { coordinates: [number, number][] } }) {
    const routeCoordinates = route.geometry.coordinates;
    const routeCoordinatesSwitched = routeCoordinates.map(
      (coordinate: [number, number]) => [coordinate[1], coordinate[0]]
    );
    return routeCoordinatesSwitched;
  }

  drawMainPolyline(coordinates: any) {
    const mainRoutePolyline = L.polyline(coordinates, {
      color: '#E1A901',
      weight: 4,
    });
    this.bounds = mainRoutePolyline.getBounds();
    mainRoutePolyline.addTo(this.layerPolylines!);
  }
  drawAlternativePolyline(
    coordinates: any,
    betweenTwoAdressesRoutes: [Route, Route?]
  ) {
    const alternativeRoutePolyline = L.polyline(coordinates, {
      color: '#0D4F83',
      weight: 2,
    });

    alternativeRoutePolyline.addEventListener('click', (event) => {
      this.clearMap(false);
      this.store.dispatch(
        new DestinationsAction(
          DestinationsActionType.SWITCH_ALTERNATIVE,
          betweenTwoAdressesRoutes
        )
      );
      this.drawPolylines();
      this.calculateRouteInfo();
    });
    alternativeRoutePolyline.addTo(this.layerPolylines!);
    this.bounds!.extend(alternativeRoutePolyline.getBounds());
  }

  calculateRouteInfo() {
    let distance = this.estimatedDistance;
    let time = this.estimatedTime;
    for (const route of this.routes) {
      let mainRoute = route[0].selected ? route[0] : route[1]!;
      distance += mainRoute.distance;
      time += mainRoute.duration;
    }
    this.setRouteInfoSlice(distance, time);
  }
  clearMap(clearMarkers: boolean) {
    if (!this.layerPolylines) {
      return;
    }
    this.layerPolylines!.clearLayers();
    if (clearMarkers && this.layerMarkers) this.layerMarkers!.clearLayers();
  }

  setRouteInfoSlice(distance: number, time: number) {
    this.store.dispatch(
      new DestinationsAction(
        DestinationsActionType.ADD_ROUTE_DISTANCE,
        distance
      )
    );
    this.store.dispatch(
      new DestinationsAction(DestinationsActionType.ADD_ROUTE_TIME, time)
    );
  }
  async getLatLong(
    outermost: boolean,
    address: string
  ): Promise<{ latitude: number; longitude: number }> {
    let params = `q=${address}&format=json&addressdetails=1&polygon_geojson=0`;
    let longitude: number = 45.2671;
    let latitude: number = 19.8335;
    const queryStr = new URLSearchParams(params).toString();
    let result = await axios.get(
      'https://nominatim.openstreetmap.org/search?' + queryStr,
      { headers: { 'Access-Control-Allow-Origin': '*' } }
    );
    if (result.data.length === 0) {
      this.error = true;
    } else {
      longitude = result.data[0].lon;
      latitude = result.data[0].lat;
      this.createMarker(outermost, new L.LatLng(latitude, longitude), address);
    }
    return { latitude, longitude };
  }

  createMarker(outermost: boolean, latLng: L.LatLng, name: string) {
    if (outermost) {
      L.marker(latLng, {
        icon: L.icon({
          iconUrl: 'http://localhost:4200/assets/icons/pin-fill.png',
          iconSize: [32, 32],
        }),
        title: name,
      }).addTo(this.layerMarkers!);
    } else {
      L.marker(latLng, {
        icon: L.icon({
          iconUrl: 'http://localhost:4200/assets/icons/pin-fill-black.png',
          iconSize: [23, 23],
        }),
        title: name,
      }).addTo(this.layerMarkers!);
    }
  }

  toggleErrorToast = () => {
    this.openErrorToast = !this.openErrorToast;
  };
}
