import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {RideHistory} from "../../../interfaces/RideHistory";

@Component({
  selector: 'history-table',
  templateUrl: './history-table.component.html',
  styleUrls: ['./history-table.component.css']
})
export class HistoryTableComponent implements OnInit {

  constructor() {
    this._currentPage = this.page;
  }

  headers: ('start'| 'end'| 'driver'| 'duration'| 'price')[] = [
    'start', 'end', 'driver', 'duration', 'price'
  ]

  @Input('rideHistory') rideHistory!: RideHistory[];

  sortBy: 'start'| 'end'| 'driver'| 'duration'| 'price' = 'start';
  @Input() page!: string;

  private _currentPage: string = '1';

  get currentPage(): string {
    return this._currentPage;
  }

  set currentPage(value: string) {
    if (this._currentPage !== value) {
      this._currentPage = value;
      this.pageChange.emit(this._currentPage);
    }

  }

  @Input() numberOfPages!: number;
  @Output() pageChange = new EventEmitter<string>();
  @Output() numberOfPagesChange = new EventEmitter<number>();

  ngOnInit(): void {
  }

}
