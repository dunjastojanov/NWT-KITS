import {Component, OnInit} from '@angular/core';
import {Store} from "@ngrx/store";
import {StoreType} from "../../shared/store/types";
import {User} from "../../interfaces/User";

@Component({
  selector: 'app-profile-page',
  templateUrl: './profile-page.component.html',
  styleUrls: ['./profile-page.component.css']
})
export class ProfilePageComponent implements OnInit {

  profileImageLink: string = "assets/profile-placeholder.png";

  user: User | null = null;

  constructor(private store: Store<StoreType>) {
    let loggedUserSlice = store.select('loggedUser');
    loggedUserSlice.subscribe(
      resData => {
        this.profileImageLink = resData.user?.profilePicture || "assets/profile-placeholder.png";
        this.user = resData.user;
      }
    )

    this.navbarItems = [
      {routerLink: "information", path: "./assets/icons/information.png", title: "General information"},
      {routerLink: "statistics", path: "./assets/icons/statistics.png", title: "Statistics"},
      {routerLink: "history", path: "./assets/icons/history.png", title: "History"},
    ]

    if (this.hasRole("CLIENT")) {
      this.navbarItems.push(
        {routerLink: "favourites", path: "./assets/icons/favorite.png", title: "Favourite routes"},
      );
    }
  }

  hasRole(role: string): boolean {
    return this.user !== null && this.user?.roles.indexOf(role) !== -1;
  }

  navbarItems: any = []


  ngOnInit(): void {
  }

}
