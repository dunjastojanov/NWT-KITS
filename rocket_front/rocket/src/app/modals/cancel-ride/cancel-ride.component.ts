import { Component, Input, OnInit } from '@angular/core';
import { UserService } from '../../services/user/user.service';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'cancel-ride',
  templateUrl: './cancel-ride.component.html',
  styleUrls: ['./cancel-ride.component.css'],
})
export class CancelRideComponent implements OnInit {
  @Input('open') open!: boolean;
  @Input('closeFunc') closeFunc!: () => void;
  @Input('rideId') rideId!: number | undefined;

  message: string = '';

  constructor(private service: UserService, private toastr: ToastrService) {
    this.service = service;
    this.toastr = toastr;
  }

  onCancel() {
    if (this.rideId) {
      this.service
        .cancelRide(this.rideId.toString(), this.message)
        .then((result) => {
          this.toastr.success(result);
        })
        .catch((err) => {
          this.toastr.error(err.message);
        });
    }
  }

  closeModal() {
    this.closeFunc();
    this.open = false;
  }
  ngOnInit(): void {}
}
