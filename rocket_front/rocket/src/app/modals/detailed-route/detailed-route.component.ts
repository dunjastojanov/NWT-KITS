import {Component, Input, OnInit} from '@angular/core';
import {RideService} from "../../services/ride/ride.service";
import {RideDetails} from "../../interfaces/RideDetails";
import {ToastrService} from "ngx-toastr";
import {CurrentRideAction, CurrentRideActionType} from "../../shared/store/current-ride-slice/current-ride.actions";
import {Router} from "@angular/router";
import {StoreType} from "../../shared/store/types";
import {Store} from "@ngrx/store";

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
  ride: RideDetails = {
    id: "",
    date: "",
    driver: "",
    driverProfileImage: "",
    duration: 0,
    end: "",
    passengers: [],
    routeLocation: "",
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

  isAbleToAddReview(): boolean {
    return Math.abs(new Date(this.ride.date).getDate() - new Date().getDate()) / (1000 * 60 * 60 * 24) <= 3;
  }

  book(id: string) {
    this.rideService.bookExisting(id).then(id => {
      if (id) {
        this.rideService.getCurrentRide(id).then(currentRide => {
          if (currentRide) {
            this.store.dispatch(new CurrentRideAction(CurrentRideActionType.SET, currentRide))
            this.router.navigateByUrl("/ride/current").then(r =>
              this.toastr.success("You have booked a ride with you favourite route parameters.")
            );
          }
        })
      }
    })
  }
}
