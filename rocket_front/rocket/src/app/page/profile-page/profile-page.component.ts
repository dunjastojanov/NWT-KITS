import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-profile-page',
  templateUrl: './profile-page.component.html',
  styleUrls: ['./profile-page.component.css']
})
export class ProfilePageComponent implements OnInit {

  constructor() { }

  navbarItems:any =  [
    {routerLink: "information", path: "./assets/icons/information.png", title: "General information"},
    {routerLink: "favourites", path: "./assets/icons/favorite.png", title: "Favourite routes"},
    {routerLink: "statistics", path: "./assets/icons/statistics.png", title: "Statistics"},
    {routerLink: "history", path: "./assets/icons/history.png", title: "History"},
  ]
  profileImageLink: string = "assets/fiona.jpg";

  ngOnInit(): void {
  }

}
