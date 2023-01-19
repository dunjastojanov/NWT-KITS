import {Component, OnInit} from '@angular/core';
import {UserService} from "../../../services/user/user.service";
import {Report} from "../../../interfaces/Report";
import {SelectItem} from "../../../interfaces/SelectItem";

@Component({
  selector: 'app-profile-statistics',
  templateUrl: './profile-statistics.component.html',
  styleUrls: ['./profile-statistics.component.css']
})
export class ProfileStatisticsComponent implements OnInit {

  reportData: Report = {average: 0, data: {datasets: [], labels: []}, total: 0};
  startDate: string = new Date().toISOString().slice(0, 10).replace("2023", "2020");
  endDate: string = new Date().toISOString().slice(0, 10).replace("2023", "2027");
  type: string = "kilometers";

  constructor(private service: UserService) {
  }

  items: SelectItem[] = [
    {value: "kilometers", text: "Number of kilometers per day"},
    {value: "rides", text: "Number of rides per day"},
    {value: "money", text: "Amount of money per day"}
  ]

  ngOnInit(): void {
    this.onShow();
  }

  onShow() {
    this.service.getUserStatistics({
      startDate: this.startDate + " 00:00",
      endDate: this.endDate + " 23:59"
    }, this.type).then(result => {
      this.reportData = result;
    });
  }

}
