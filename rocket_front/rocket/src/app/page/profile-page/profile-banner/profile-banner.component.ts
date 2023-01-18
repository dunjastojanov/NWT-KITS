import { Component, OnInit } from '@angular/core';
import {User} from "../../../interfaces/User";
import {UserService} from "../../../services/user/user.service";

@Component({
  selector: 'app-profile-banner',
  templateUrl: './profile-banner.component.html',
  styleUrls: ['./profile-banner.component.css']
})
export class ProfileBannerComponent implements OnInit {

  title: string = "";
  constructor(private service: UserService) {
    this.service.getUser().then((res) => {
      if (res) {
        this.title = `${res.firstName} ${res.lastName}`;

      }

    });

  }

  ngOnInit(): void {
  }

}
