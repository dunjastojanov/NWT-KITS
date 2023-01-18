import {Component, OnInit} from '@angular/core';
import {Store} from "@ngrx/store";
import {StoreType} from "../../shared/store/types";

@Component({
  selector: 'app-profile-page',
  templateUrl: './profile-page.component.html',
  styleUrls: ['./profile-page.component.css']
})
export class ProfilePageComponent implements OnInit {

  profileImageLink: string = "assets/profile-placeholder.png";

  constructor(private store: Store<StoreType>) {
    let loggedUserSlice = store.select('loggedUser');
    loggedUserSlice.subscribe(
      resData => {
        this.profileImageLink = resData.user?.profilePicture || "assets/profile-placeholder.png";
      }
    )
  }

  navbarItems: any = [
    {routerLink: "information", path: "./assets/icons/information.png", title: "General information"},
    {routerLink: "favourites", path: "./assets/icons/favorite.png", title: "Favourite routes"},
    {routerLink: "statistics", path: "./assets/icons/statistics.png", title: "Statistics"},
    {routerLink: "history", path: "./assets/icons/history.png", title: "History"},
  ]


  ngOnInit(): void {
  }

}
