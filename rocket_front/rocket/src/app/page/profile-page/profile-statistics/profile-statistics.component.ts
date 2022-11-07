import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-profile-statistics',
  templateUrl: './profile-statistics.component.html',
  styleUrls: ['./profile-statistics.component.css']
})
export class ProfileStatisticsComponent implements OnInit {

  constructor() { }

  items: any = [
    {value: "kilometers", text: "Number of kilometers per day"},
    {value: "rides", text: "Number of rides per day"},
  ]

  ngOnInit(): void {
  }

}
