import {Component, Input, OnInit} from '@angular/core';
import {RideService} from "../../services/ride/ride.service";
import {ToastrService} from "ngx-toastr";

@Component({
  selector: 'report-driver',
  templateUrl: './report-driver.component.html',
  styleUrls: ['./report-driver.component.css']
})
export class ReportDriverComponent implements OnInit {
  @Input('open') open!: boolean;
  @Input('driver') driver!: string;
  @Input('driverId') driverId!: string | undefined;
  @Input('closeFunc') closeFunc!: () => void;

  constructor(private rideService: RideService, private toastr: ToastrService) {

  }

  ngOnInit(): void {
  }

  closeModal() {
    this.closeFunc();
    this.open = false;
  }

  report() {
    if (this.driverId) {
      this.rideService.report(this.driverId).then(() => {
        this.toastr.success("You have successfully reported the driver.")
      }).catch(() => {
        this.toastr.success("Error with reporting the driver.")

      })
    }
  }
}
