import {Component, Input, OnInit} from '@angular/core';
import {User} from "../../interfaces/User";
import {Store} from "@ngrx/store";
import {StoreType} from "../../shared/store/types";

@Component({
  selector: 'app-edit-profile',
  templateUrl: './edit-profile.component.html',
  styleUrls: ['./edit-profile.component.css']
})
export class EditProfileComponent implements OnInit {

  @Input('open') open!: boolean;
  @Input('closeFunc') closeFunc!: () => void;

  showPersonalInformation: boolean = true;
  showPaymentInformation: boolean = false;
  showProfilePicture: boolean = false;

  user: User | null = null;

  constructor(private store: Store<StoreType>) {
    store.select('loggedUser').subscribe(
      resData => {
        this.user = resData.user;
      }
    )
  }

  ngOnInit(): void {
  }

  togglePersonalInformation() {
    this.showPersonalInformation = true;
    this.showPaymentInformation = false;
    this.showProfilePicture = false;
  }

  togglePaymentInformation() {
    this.showPersonalInformation = false;
    this.showPaymentInformation = true;
    this.showProfilePicture = false;
  }

  toggleProfilePicture() {
    this.showPersonalInformation = false;
    this.showPaymentInformation = false;
    this.showProfilePicture = true;
  }

}
