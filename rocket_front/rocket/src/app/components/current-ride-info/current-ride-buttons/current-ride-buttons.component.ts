import { Component, Input, OnInit } from '@angular/core';

@Component({
  selector: 'current-ride-buttons',
  templateUrl: './current-ride-buttons.component.html',
  styleUrls: ['./current-ride-buttons.component.css']
})
export class CurrentRideButtonsComponent implements OnInit {
  @Input('user') user!: boolean
  @Input('isFavorite') isFavorite: boolean
  @Input('driverName') driverName?: string;

  openReportModal = false;

  patchingRide = false;
  dissablePatch = false;

  constructor() {
    this.isFavorite = false;
   }

  ngOnInit(): void {
  }

  toggleReportModal() {
    this.openReportModal = !this.openReportModal;
  }

  togglePatchingRide() {
    if (!this.patchingRide) {
      this.patchingRide = true;
    } else {
      this.dissablePatch = true;
    }
  }
}
