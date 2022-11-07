import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-vertical-navbar',
  templateUrl: './vertical-navbar.component.html',
  styleUrls: ['./vertical-navbar.component.css']
})
export class VerticalNavbarComponent implements OnInit {

  constructor() { }

  navbarItems:any=[
    {routerLink: "information", path: "./assets/icons/information.png", title: "General information"},
    {routerLink: "favourites", path: "./assets/icons/favorite.png", title: "Favourite routes"},
    {routerLink: "statistics", path: "./assets/icons/statistics.png", title: "Statistics"},
    {routerLink: "history", path: "./assets/icons/history.png", title: "History"},
  ]

  ngOnInit(): void {
  }

}
