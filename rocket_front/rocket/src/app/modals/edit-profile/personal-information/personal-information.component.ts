import {Component, OnInit} from '@angular/core';
import {User} from "../../../interfaces/User";
import {UserService} from "../../../services/user/user.service";
import {ToastrService} from "ngx-toastr";

@Component({
  selector: 'app-personal-information',
  templateUrl: './personal-information.component.html',
  styleUrls: ['./personal-information.component.css']
})
export class PersonalInformationComponent implements OnInit {

  user: User | null = null;
  data: User = {city: "", email: "", firstName: "", id: "", lastName: "", phoneNumber: "", profilePicture: "", roles: []};
  constructor(private service: UserService, private toastr: ToastrService) {
    this.setUser().then(() => {
      if (this.user !== null) {
        this.data = this.user;
      }
    });
  }
  async setUser():Promise<void> {
    this.user = await this.service.getUser();
  }

  ngOnInit(): void {
  }

  onSave(): void
  {
    let dto = {
      firstName: this.data.firstName,
      lastName: this.data.lastName,
      phoneNumber: this.data.phoneNumber,
      city: this.data.city
    }

    this.service.editUser(dto).then(result=> {
      this.toastr.success(result.message)
    }).catch(error=> {
      console.error(error)
      this.toastr.error("Error with updating profile")
    })

  }
}
