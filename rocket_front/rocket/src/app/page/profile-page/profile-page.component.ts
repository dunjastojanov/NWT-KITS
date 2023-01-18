import { Component, OnInit } from '@angular/core';
import {UserService} from "../../services/user/user.service";

@Component({
  selector: 'app-profile-page',
  templateUrl: './profile-page.component.html',
  styleUrls: ['./profile-page.component.css']
})
export class ProfilePageComponent implements OnInit {

  profileImageLink: string = "assets/profile-placeholder.png";
  constructor(private service: UserService) {
    service.getUser().then(res=> {
      this.profileImageLink = res?.profilePicture || "assets/profile-placeholder.png";
    })
  }

  navbarItems:any =  [
    {routerLink: "information", path: "./assets/icons/information.png", title: "General information"},
    {routerLink: "favourites", path: "./assets/icons/favorite.png", title: "Favourite routes"},
    {routerLink: "statistics", path: "./assets/icons/statistics.png", title: "Statistics"},
    {routerLink: "history", path: "./assets/icons/history.png", title: "History"},
  ]


  ngOnInit(): void {
  }

}
