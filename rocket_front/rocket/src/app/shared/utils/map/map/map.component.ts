import { AfterViewInit, Component, Input } from '@angular/core';
import { Store } from '@ngrx/store';
import axios from 'axios';
import * as L from 'leaflet'
import * as lrm from 'leaflet-routing-machine'
import { RouteService } from 'src/app/components/routes/route.service';
import { Destination } from 'src/app/interfaces/Destination';
import { DestinationsAction, DestinationsActionType } from 'src/app/shared/store/destinations-slice/destinations.actions';
import { StoreType } from 'src/app/shared/store/types';
@Component({
  selector: 'app-map',
  templateUrl: './map.component.html',
  styleUrls: ['./map.component.css']
})
export class MapComponent implements AfterViewInit {
  @Input('height') height!: string
  private map: any;
  estimatedDistance: number = 0;
  estimatedTime: number = 0;
  chosenRouteCoordinates: L.LatLng[] = [];
  destinations: L.Routing.Waypoint[] = [];
  destObj: Destination[] = []
  routing: L.Routing.Control | null = null;
  openErrorToast = false;
  error:boolean = false;
  private initMap(): void {
    lrm;
    this.map = L.map('map', {
      center: [45.2671, 19.8335],
      zoom: 8
    });

    const tiles = L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      maxZoom: 18,
      minZoom: 3,
    });

    tiles.addTo(this.map);
  }

  constructor(private service: RouteService, private store: Store<StoreType>) { 
    this.store.select('destinations').subscribe(
      resData => {
        this.destObj = resData.destinations;
      }
    )  
  }

  ngAfterViewInit(): void {
    this.service.trigger$().subscribe(x => this.showDestinations())
    this.initMap();
  }

  showDestinations() {
    this.destinations = []
    this.error = false;
    if (this.destObj.filter(elem => elem.address !== '').length === this.destObj.length) {
      let destPromises = this.destObj.map(async elem=> await this.getLatLong(elem.address))
      Promise.all(destPromises).then(promises => promises.map(promise => {
        this.destinations.push(promise)
        if (this.destinations.length === promises.length)
          this.addRouting();
      }));
    }
  }

  addRouting() {
    if (this.routing) {
      this.map.removeControl(this.routing);
    }
    if (this.error) {
      this.openErrorToast = true;
      this.estimatedDistance = 0;
      this.estimatedTime = 0;
      this.setDistanceSlice();
      return;
    }
    this.routing = L.Routing.control({
      plan: new L.Routing.Plan(this.destinations, {addWaypoints: false, draggableWaypoints: false, createMarker: this.createMarker}),
      autoRoute: true,
      showAlternatives: true,
      lineOptions: { styles: [{ color: 'black', opacity: 0.15, weight: 13 }, { color: 'white', opacity: 0.8, weight: 10 }, { color: '#0A3F68', opacity: 1, weight: 6 }], extendToWaypoints: true, missingRouteTolerance: 0, addWaypoints: false },
      altLineOptions: { styles: [{ color: 'black', opacity: 0.15, weight: 9 }, { color: 'white', opacity: 0.8, weight: 6 }, { color: '#E1A901', opacity: 1, weight: 4 }], extendToWaypoints: true, missingRouteTolerance: 0 },
      show: false
    })
    .on('routeselected', (e) => {
      var route = e.route;
      this.chosenRouteCoordinates = route.coordinates;
      this.estimatedDistance = route.summary.totalDistance;
      this.estimatedTime = route.summary.totalTime;
      this.setDistanceSlice();
    })

    this.routing.addTo(this.map);
  }

  setDistanceSlice() {
    this.store.dispatch(new DestinationsAction(DestinationsActionType.ADD_ROUTE_DISTANCE, this.estimatedDistance));
    this.store.dispatch(new DestinationsAction(DestinationsActionType.ADD_ROUTE_TIME, this.estimatedTime));
  }
  async getLatLong(address: string): Promise<L.Routing.Waypoint> {
    let params = `q=${address}&format=json&addressdetails=1&polygon_geojson=0`;
    let longitude: number = 45.2671;
    let latitude: number = 19.8335;
    let name:string = '';
    const queryStr = new URLSearchParams(params).toString();
    let result = await axios.get("https://nominatim.openstreetmap.org/search?" + queryStr, { headers: { 'Access-Control-Allow-Origin': '*' } })
    if (result.data.length === 0) {
      this.error = true;
    } else {
      longitude = result.data[0].lon;
      latitude = result.data[0].lat;
      name = result.data[0].display_name;
    }
    return new L.Routing.Waypoint(new L.LatLng(latitude, longitude), name, {allowUTurn: true}); ;
  }

  createMarker(i: number, wp:L.Routing.Waypoint, nWps: number): L.Marker {
    if (i === 0 || i === nWps - 1) {
        return L.marker(wp.latLng, {icon: L.icon({iconUrl:"http://localhost:4200/assets/icons/pin-fill.png", iconSize:[32,32]}), title: wp.name});
    } else {
        return L.marker(wp.latLng, {icon: L.icon({iconUrl:"http://localhost:4200/assets/icons/pin-fill-black.png", iconSize:[23,23]}), title: wp.name });
    }
  }

  toggleErrorToast = () => {
    this.openErrorToast =!this.openErrorToast;
  }
}