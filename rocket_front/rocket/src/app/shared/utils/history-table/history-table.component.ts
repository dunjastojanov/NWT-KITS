import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-history-table',
  templateUrl: './history-table.component.html',
  styleUrls: ['./history-table.component.css']
})
export class HistoryTableComponent implements OnInit {

  constructor() { }

  openDetailedRoute: boolean = false;

  toggleDetailedRoute = () => {
    this.openDetailedRoute =!this.openDetailedRoute;
  }

  headers: any = [
    'start', 'end','driver', 'duration', 'price'
  ]

  data: any = Array(8).fill(
    { start: "Bulevar Oslobodjenja bb",
      end: "Bulevar Evrope 127",
      driver: "Kevin Ball",
      car: "Mercedes A160",
      duration: "7 min",
      price: "682 din"
    }
  )



  ngOnInit(): void {
  }

}
