import {Component, Input, OnInit} from '@angular/core';
import {RideService} from "../../services/ride/ride.service";
import {ToastrService} from "ngx-toastr";

@Component({
  selector: 'review',
  templateUrl: './review.component.html',
  styleUrls: ['./review.component.css']
})
export class ReviewComponent implements OnInit {

  driverRating: number = 0;
  vehicleRating: number = 0;
  description: string = '';
  private rideService: RideService;

  constructor(rideService: RideService, private toastr: ToastrService) {
    this.rideService = rideService;
  }

  @Input('open') open!: boolean;
  @Input('rideId') rideId!: string;
  @Input('closeFunc') closeFunc!: () => void;

  onRatingSet(rating: number, type: 'driver' | 'vehicle'): void {
    if (type === 'driver') {
      this.driverRating = rating;

    } else if (type === 'vehicle') {
      this.vehicleRating = rating;

    }
  }

  ngOnInit(): void {
  }

  addReview() {
    let dto = {
      rideId: this.rideId,
      driverRating: this.driverRating,
      vehicleRating: this.vehicleRating,
      description: this.description
    }
    this.rideService.addReview(dto).then(response => {
        this.toastr.success('Review added successfully');
        window.location.reload();
      }
    )
  }
}
