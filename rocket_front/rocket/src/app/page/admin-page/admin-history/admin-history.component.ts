import {Component, OnInit} from '@angular/core';
import {RideHistory} from "../../../interfaces/RideHistory";
import {AdminService} from "../../../services/admin/admin.service";
import {ToastrService} from "ngx-toastr";

@Component({
  selector: 'app-admin-history',
  templateUrl: './admin-history.component.html',
  styleUrls: ['./admin-history.component.css']
})
export class AdminHistoryComponent implements OnInit {
  _currentPage: string = '1'
  get currentPage(): string {
    return this._currentPage;
  }

  set currentPage(value: string) {
    if (this._currentPage !== value) {
      this._currentPage = value;
      this.fetchHistory();
    }
  }

  rideHistory: RideHistory[] = [];

  numberOfPages: number = 1;

  email: string = "";

  constructor(private service: AdminService, private toastr: ToastrService) {
  }

  private fetchHistory() {
    this.service.getHistory(this._currentPage).then(data => {
      this.rideHistory = data.content;
      this.numberOfPages = data.totalPages;
    })
  }

  ngOnInit(): void {
    this.fetchHistory();

  }

  fetch() {
    this.service.getHistoryForEmail(this._currentPage, this.email).then(data => {
      this.rideHistory = data.content;
      this.numberOfPages = data.totalPages;
    }).catch(err => {
        this.toastr.error(err.message);
      }
    )
  }
}
