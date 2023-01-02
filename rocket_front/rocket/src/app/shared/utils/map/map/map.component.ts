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
import { StoreType } from 'src/app/shared/store/types';
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
  chosenRouteCoordinates: L.LatLng[] = [];
  destinations: { latitude: number; longitude: number }[] = [];
  destObj: Destination[] = [];
  layerPolylines: L.LayerGroup | null = null;
  layerMarkers: L.LayerGroup | null = null;
  routes: [
    {
      distance: number;
      duration: number;
      geometry: { coordinates: [number, number][] };
    },
    {
      distance: number;
      duration: number;
      geometry: { coordinates: [number, number][] };
    }?
  ][] = [];
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

  constructor(private service: RouteService, private store: Store<StoreType>) {
    this.store.select('destinations').subscribe((resData) => {
      this.destObj = resData.destinations;
    });
  }

  ngAfterViewInit(): void {
    this.service.trigger$().subscribe((x) => this.showDestinations());
    this.initMap();
  }

  showDestinations() {
    this.clearMap(true);
    this.destinations = [];
    this.routes = [];
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

      const mainRoute: {
        distance: number;
        duration: number;
        geometry: { coordinates: [number, number][] };
      } = routes[0];
      const alternativeRoute: {
        distance: number;
        duration: number;
        geometry: { coordinates: [number, number][] };
      } = routes[1];

      alternativeRoute
        ? this.routes.push([mainRoute, alternativeRoute])
        : this.routes.push([mainRoute]);
    });
  }

  drawPolylines() {
    this.estimatedDistance = 0;
    this.estimatedTime = 0;
    for (const polyline of this.routes) {
      let mainRoute: { geometry: { coordinates: [number, number][] } } =
        polyline[0];
      let mainCoordinates = this.getCoordinates(mainRoute);
      this.drawMainPolyline(mainCoordinates);

      if (polyline[1]) {
        let alternativeRoute: {
          geometry: { coordinates: [number, number][] };
        } = polyline[1];
        let alternativeCoordinates = this.getCoordinates(alternativeRoute);
        this.drawAlternativePolyline(alternativeCoordinates, polyline);
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
    routes: [
      { geometry: { coordinates: [number, number][] } },
      { geometry: { coordinates: [number, number][] } }?
    ]
  ) {
    const alternativeRoutePolyline = L.polyline(coordinates, {
      color: '#0D4F83',
      weight: 2,
    });

    alternativeRoutePolyline.addEventListener('click', (event) => {
      this.clearMap(false);
      this.routes = this.routes.map((elem) => {
        if (elem[0].geometry.coordinates === routes[0].geometry.coordinates) {
          return [elem[1]!, elem[0]];
        } else return [elem[0], elem[1]!];
      });
      this.drawPolylines();
      this.calculateRouteInfo();
    });
    alternativeRoutePolyline.addTo(this.layerPolylines!);
    this.bounds!.extend(alternativeRoutePolyline.getBounds());
  }

  calculateRouteInfo() {
    for (const polyline of this.routes) {
      let mainRoute = polyline[0];
      this.estimatedDistance += mainRoute.distance;
      this.estimatedTime += mainRoute.duration;
    }
    this.setRouteInfoSlice();
  }
  clearMap(clearMarkers: boolean) {
    if (!this.layerPolylines) {
      return;
    }
    this.layerPolylines!.clearLayers();
    if (clearMarkers && this.layerMarkers) this.layerMarkers!.clearLayers();
  }

  setRouteInfoSlice() {
    this.store.dispatch(
      new DestinationsAction(
        DestinationsActionType.ADD_ROUTE_DISTANCE,
        this.estimatedDistance
      )
    );
    this.store.dispatch(
      new DestinationsAction(
        DestinationsActionType.ADD_ROUTE_TIME,
        this.estimatedTime
      )
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
