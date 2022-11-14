import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'current-ride-route',
  templateUrl: './current-ride-route.component.html',
  styleUrls: ['./current-ride-route.component.css']
})
export class CurrentRideRouteComponent implements OnInit {
  @Input('estimatedTime') estimatedTime!: string;
  @Input('price') price!: string;
  @Input('endAddress') endAddress!: string;
  @Input('startAddress') startAddress!: string;

  constructor() { }

  ngOnInit(): void {
  }

}
