import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'app-detailed-route',
  templateUrl: './detailed-route.component.html',
  styleUrls: ['./detailed-route.component.css']
})
export class DetailedRouteComponent implements OnInit {

  @Input('open') open!: boolean;
  @Input('closeFunc') closeFunc!: () => void;

  constructor() { }

  route = { start: "Bulevar Oslobodjenja bb",
    end: "Bulevar Evrope 127",
    driver: "Kevin Ball",
    passenger: "Fiona Gallager",
    car: "Mercedes A160",
    duration: "7 min",
    price: "682 din"
  }

  ngOnInit(): void {
  }

}
