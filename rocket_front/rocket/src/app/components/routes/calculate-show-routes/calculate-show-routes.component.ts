import { Component, OnInit } from '@angular/core';
import { RouteService } from '../route.service';

@Component({
  selector: 'calculate-show-routes',
  templateUrl: './calculate-show-routes.component.html',
  styleUrls: ['./calculate-show-routes.component.css']
})
export class CalculateShowRoutesComponent implements OnInit {

  constructor(private service: RouteService ) { }

  ngOnInit(): void {
  }

  onShow() {
      this.service.setTrigger('trigger');
  }
}
