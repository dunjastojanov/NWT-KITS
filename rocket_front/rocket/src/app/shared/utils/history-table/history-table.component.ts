import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {RideHistory} from "../../../interfaces/RideHistory";

@Component({
  selector: 'history-table',
  templateUrl: './history-table.component.html',
  styleUrls: ['./history-table.component.css']
})
export class HistoryTableComponent implements OnInit {
  constructor() {
  }
  headers: string[] = [
    'start', 'end', 'driver', 'duration', 'price'
  ]

  @Input('rideHistory') rideHistory!: RideHistory[];
  @Input() currentPage!: string;
  @Input() numberOfPages!: number;
  @Output() currentPageChange = new EventEmitter<string>();

  ngOnInit(): void {}

}
