import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-admin-statistics',
  templateUrl: './admin-statistics.component.html',
  styleUrls: ['./admin-statistics.component.css']
})
export class AdminStatisticsComponent implements OnInit {

  constructor() { }

  types: any = [
    {value: "kilometers", text: "Number of kilometers per day"},
    {value: "rides", text: "Number of rides per day"},
  ]

  users: any = [
    {value: "client", text: "Client"},
    {value: "driver", text: "Driver"},
  ]

  ngOnInit(): void {
  }

}
