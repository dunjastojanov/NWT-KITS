import {Component, OnInit} from '@angular/core';
import {User} from "../../../interfaces/User";
import {UserService} from "../../../services/user/user.service";

@Component({
  selector: 'app-general-information',
  templateUrl: './general-information.component.html',
  styleUrls: ['./general-information.component.css']
})
export class GeneralInformationComponent implements OnInit {

  openEditProfile: boolean = false;
  information: any = []
  user: User | null = null;
  constructor(private service: UserService) {
    this.setUser().then(() => {
      this.setInformation();

    });

  }
  async setUser() {
    this.user = await this.service.getUser();
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
      }
    ]
  }
  ngOnInit(): void {
  }

  toggleEditProfile = () => {
    this.openEditProfile =!this.openEditProfile;
  }
}
