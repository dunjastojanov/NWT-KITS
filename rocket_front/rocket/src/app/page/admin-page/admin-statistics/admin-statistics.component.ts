import {Component, OnInit} from '@angular/core';
import {Report} from "../../../interfaces/Report";
import {SelectItem} from "../../../interfaces/SelectItem";
import {AdminService} from "../../../services/admin/admin.service";

@Component({
  selector: 'app-admin-statistics',
  templateUrl: './admin-statistics.component.html',
  styleUrls: ['./admin-statistics.component.css']
})
export class AdminStatisticsComponent implements OnInit {

  reportData: Report = {average: 0, data: {datasets: [], labels: []}, total: 0};
  startDate: string = new Date().toISOString().slice(0, 10).replace("2023", "2020");
  endDate: string = new Date().toISOString().slice(0, 10).replace("2023", "2027");
  type: string = "kilometers";

  constructor(private service: AdminService) {
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
    this.service.getStatistics({
      startDate: this.startDate + " 00:00",
      endDate: this.endDate + " 23:59"
    }, this.type).then(result => {
      this.reportData = result;
    });
  }

  users: any = [
    {value: "client", text: "Client"},
    {value: "driver", text: "Driver"},
  ]


}
