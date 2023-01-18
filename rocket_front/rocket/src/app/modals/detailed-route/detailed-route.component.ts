import {Component, Input, OnInit} from '@angular/core';
import {RideService} from "../../services/ride/ride.service";
import {RideDetails} from "../../interfaces/RideDetails";
import {ToastrService} from "ngx-toastr";

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
    driver: "",
    driverProfileImage: "",
    duration: 0,
    end: "",
    passenger: "",
    price: 0,
    rating: 0,
    reviews: [],
    start: ""
  };
  private rideService!: RideService;
  numbers: number[];

  constructor(rideService: RideService, private toastr: ToastrService) {
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
    this.rideService.addFavorite(id).then(result => {
      this.toastr.success("Favorite route added");

    }).catch(error => {
        this.toastr.error(error.message);

      }
    )
  }

}
