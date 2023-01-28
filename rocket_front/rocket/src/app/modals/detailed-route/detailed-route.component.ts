import {Component, Input, OnInit} from '@angular/core';
import {RideService} from "../../services/ride/ride.service";
import {RideDetails} from "../../interfaces/RideDetails";
import {ToastrService} from "ngx-toastr";
import {CurrentRideAction, CurrentRideActionType} from "../../shared/store/current-ride-slice/current-ride.actions";
import {Router} from "@angular/router";
import {StoreType} from "../../shared/store/types";
import {Store} from "@ngrx/store";
import {CurrentRide} from "../../interfaces/Ride";
import {User} from "../../interfaces/User";

@Component({
  selector: 'detailed-route',
  templateUrl: './detailed-route.component.html',
  styleUrls: ['./detailed-route.component.css']
})
export class DetailedRouteComponent implements OnInit {

  openReview: boolean = false;

  toggleOpenReview = () => {
    this.openReview = !this.openReview;
  }

  @Input('open') open!: boolean;
  @Input('closeFunc') closeFunc!: () => void;
  @Input() id!: string;
  user: User|null = null;
  ride: RideDetails = {
    id: "",
    date: "",
    driver: "",
    driverProfileImage: "",
    duration: 0,
    end: "",
    passengers: [],
    price: 0,
    rating: 0,
    reviews: [],
    start: ""
  };
  private rideService!: RideService;
  numbers: number[];

  constructor(rideService: RideService, private toastr: ToastrService,
              private store: Store<StoreType>, private router: Router
  ) {
    this.rideService = rideService;
    this.numbers = [1, 2, 3, 4, 5];

    store.select('loggedUser').subscribe(
      resData => {this.user = resData.user;}
    )
  }


  ngOnInit(): void {
    this.rideService.getRide(this.id).then(
      data => {
        this.ride = data;
      }
    )
  }

  addFavorite(id: string): void {
    this.rideService.addFavorite(id).then(() => {
      this.toastr.success("Favorite route added");

    }).catch(error => {
        this.toastr.error(error.message);

      }
    )
  }

  hasRole(role: string): boolean {
    return this.user !== null && this.user?.roles.indexOf(role) !== -1;
  }

  isAbleToAddReview(): boolean {
    return this.hasRole("CLIENT") &&
    Math.abs(new Date(this.ride.date).getDate() - new Date().getDate()) / (1000 * 60 * 60 * 24) <= 3;
  }

  async book(id: string) {
    let rideId = await this.rideService.bookExisting(id);
    let driverFound: boolean | null = false;
    if (rideId) {
      driverFound = await this.rideService.findDriver(rideId);
    }

    if (rideId && driverFound) {
      const currentRide: CurrentRide | null = await this.rideService.getCurrentRide(rideId);

      if (currentRide) {
        this.store.dispatch(new CurrentRideAction(CurrentRideActionType.SET, currentRide))
        this.router.navigateByUrl("/ride/current").then(r =>
          this.toastr.success("You have booked a ride with you favourite route parameters.")
        );
      }
    }

  }
}
