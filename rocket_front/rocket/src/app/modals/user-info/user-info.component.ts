import {Component, Input, OnInit} from '@angular/core';
import {User} from "../../interfaces/User";

@Component({
  selector: 'app-user-info',
  templateUrl: './user-info.component.html',
  styleUrls: ['./user-info.component.css']
})
export class UserInfoComponent implements OnInit {

  constructor() { }

  @Input('open') open!: boolean;
  @Input('closeFunc') closeFunc!: () => void;
  @Input('user') user!:User;

  information: any = []

  ngOnInit(): void {
    this.information = [{
      title: "First name",
      info: this.user.firstName
    },
      {
        title: "Last name",
        info: this.user.lastName
      },
      {
        title: "City",
        info: this.user.city
      },
      {
        title: "Phone number",
        info: this.user.phoneNumber
      }]

  }

}
