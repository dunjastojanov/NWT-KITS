import {Component, OnInit} from '@angular/core';
import {User} from "../../../interfaces/User";
import {UserService} from "../../../services/user/user.service";
import {ToastrService} from "ngx-toastr";
import {Store} from "@ngrx/store";
import {StoreType} from "../../../shared/store/types";

@Component({
  selector: 'app-personal-information',
  templateUrl: './personal-information.component.html',
  styleUrls: ['./personal-information.component.css']
})
export class PersonalInformationComponent implements OnInit {

  data: User = {
    city: "",
    email: "",
    firstName: "",
    id: "",
    lastName: "",
    phoneNumber: "",
    profilePicture: "",
    roles: []
  };


  constructor(private store: Store<StoreType>, private toastr: ToastrService, private service: UserService) {
    let loggedUserSlice = store.select('loggedUser');
    loggedUserSlice.subscribe(
      resData => {

        if (resData.user) {
          this.data = resData.user;

        }
      }
    )
  }

  ngOnInit(): void {
  }

  onSave(): void {
    if (this.data) {
      let dto = {
        firstName: this.data.firstName,
        lastName: this.data.lastName,
        phoneNumber: this.data.phoneNumber,
        city: this.data.city
      }

      this.service.editUser(dto).then(result => {
        this.toastr.success(result.message)
      }).catch(error => {
        this.toastr.error("Error with updating profile")
      })
    }


  }
}
