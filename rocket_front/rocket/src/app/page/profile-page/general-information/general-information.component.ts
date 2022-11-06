import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-general-information',
  templateUrl: './general-information.component.html',
  styleUrls: ['./general-information.component.css']
})
export class GeneralInformationComponent implements OnInit {

  openDeleteProfile: boolean = false;
  openEditProfile: boolean = false;

  information: any = [
    {
      title: "First name",
      info: "Fiona"
    },
    {
      title: "Last name",
      info: "Gallager"
    },
    {
      title: "City",
      info: "Chicago"
    },
    {
      title: "Phone number",
      info: "202-555-0117"
    }
  ]
  title: string = "First name";
  info: string = "Fiona";

  constructor() { }

  ngOnInit(): void {
  }

  toggleEditProfile = () => {
    this.openEditProfile =!this.openEditProfile;
  }

  toggleDeleteProfile = () => {
    this.openDeleteProfile =!this.openDeleteProfile;
  }

}
