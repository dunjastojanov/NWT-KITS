import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'location-card',
  templateUrl: './location-card.component.html',
  styleUrls: ['./location-card.component.css']
})
export class LocationCardComponent implements OnInit {
  @Input('city') city!: String;
  @Input('text') text!: String;

  constructor() { }

  ngOnInit(): void {
  }

}
