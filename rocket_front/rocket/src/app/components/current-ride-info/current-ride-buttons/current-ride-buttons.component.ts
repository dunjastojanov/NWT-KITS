import { Component, Input, OnInit } from '@angular/core';

@Component({
  selector: 'current-ride-buttons',
  templateUrl: './current-ride-buttons.component.html',
  styleUrls: ['./current-ride-buttons.component.css'],
})
export class CurrentRideButtonsComponent implements OnInit {
  @Input('role') role!: string;
  @Input('isFavorite') isFavorite: boolean;
  @Input('driverName') driverName!: string | null;
  @Input('rideId') rideId!: number | undefined;

  openReportModal = false;

  openCancelModal = false;

  patchingRide = false;
  dissablePatch = false;

  constructor() {
    this.isFavorite = false;
  }

  ngOnInit(): void {}

  toggleReportModal() {
    this.openReportModal = !this.openReportModal;
  }

  toggleCancelModal() {
    this.openCancelModal =!this.openCancelModal;
  }

  togglePatchingRide() {
    if (!this.patchingRide) {
      this.patchingRide = true;
    } else {
      this.dissablePatch = true;
    }
  }
}
