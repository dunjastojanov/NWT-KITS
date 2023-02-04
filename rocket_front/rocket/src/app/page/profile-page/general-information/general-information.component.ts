import {Component, OnInit} from '@angular/core';
import {User} from "../../../interfaces/User";
import {Store} from "@ngrx/store";
import {StoreType} from "../../../shared/store/types";

@Component({
  selector: 'app-general-information',
  templateUrl: './general-information.component.html',
  styleUrls: ['./general-information.component.css']
})
export class GeneralInformationComponent implements OnInit {

  openEditProfile: boolean = false;
  information: any = []
  user: User | null = null;

  constructor(private store: Store<StoreType>) {
    let loggedUserSlice = store.select('loggedUser');
    loggedUserSlice.subscribe(
      resData => {
        this.user = resData.user;
        this.setInformation()
      }
    )
  }

  setInformation() {
    this.information = [
      {
        title: "First name",
        info: this.user?.firstName
      },
      {
        title: "Last name",
        info: this.user?.lastName
      },
      {
        title: "City",
        info: this.user?.city
      },
      {
        title: "Phone number",
        info: this.user?.phoneNumber
      },
      {
        title: "Rocket tokens",
        info: this.user?.tokens
      }
    ]
  }

  ngOnInit(): void {
  }

  toggleEditProfile = () => {
    this.openEditProfile = !this.openEditProfile;
  }
}
