import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-favourite-routes',
  templateUrl: './favourite-routes.component.html',
  styleUrls: ['./favourite-routes.component.css']
})
export class FavouriteRoutesComponent implements OnInit {

  favouriteRoutes: any = [
    {
      start: "Bulevar Oslobodjenja bb",
      end: "Bulevar Evrope 127",
      focus: false
    },
    {
      start: "Bulevar Oslobodjenja bb",
      end: "Bulevar Evrope 127",
      focus: true
    },
    {
      start: "Bulevar Oslobodjenja bb",
      end: "Bulevar Evrope 127",
      focus: false
    },
    {
      start: "Bulevar Oslobodjenja bb",
      end: "Bulevar Evrope 127",
      focus: false
    },
  ]

  constructor() { }

  ngOnInit(): void {
  }

}
