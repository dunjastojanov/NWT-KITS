import {Component, OnChanges, OnInit, SimpleChanges} from '@angular/core';
import {UserService} from "../../../services/user/user.service";
import {RideHistory} from "../../../interfaces/RideHistory";
import {RideService} from "../../../services/ride/ride.service";

@Component({
  selector: 'app-profile-history',
  templateUrl: './profile-history.component.html',
  styleUrls: ['./profile-history.component.css']
})
export class ProfileHistoryComponent implements OnInit, OnChanges {

  rideHistory: RideHistory[] = [];
  currentPage: string = '1'
  numberOfPages: number = 1;

  constructor(private service: UserService, private rideService: RideService) {
  }

  ngOnChanges(changes: SimpleChanges): void {
    this.fetchHistory();
  }

  private fetchHistory() {
    this.service.getProfileHistory(this.currentPage).then(data => {
      this.rideHistory = data.content;
      this.numberOfPages = data.totalPages;
    })
  }

  ngOnInit(): void {
    this.fetchHistory();

  }




}
