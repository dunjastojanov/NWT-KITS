import {Component, OnInit} from '@angular/core';
import {Store} from "@ngrx/store";
import {StoreType} from "../../../shared/store/types";

@Component({
  selector: 'app-profile-banner',
  templateUrl: './profile-banner.component.html',
  styleUrls: ['./profile-banner.component.css']
})
export class ProfileBannerComponent implements OnInit {

  title: string = "";

  constructor(private store: Store<StoreType>) {
    let loggedUserSlice = store.select('loggedUser');
    loggedUserSlice.subscribe(
      resData => {
        this.title = `${resData.user?.firstName} ${resData.user?.lastName}`;
      }
    )
  }

  ngOnInit(): void {
  }

}
