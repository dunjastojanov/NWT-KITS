import { Component, Input, OnInit } from '@angular/core';

@Component({
  selector: 'current-ride-buttons',
  templateUrl: './current-ride-buttons.component.html',
  styleUrls: ['./current-ride-buttons.component.css']
})
export class CurrentRideButtonsComponent implements OnInit {
  @Input('user') user!: boolean
  constructor() { }

  ngOnInit(): void {
  }

}
