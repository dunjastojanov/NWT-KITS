import {Component, OnInit} from '@angular/core';

@Component({
  selector: 'app-admin-page',
  templateUrl: './admin-page.component.html',
  styleUrls: ['./admin-page.component.css']
})
export class AdminPageComponent implements OnInit {
  navbarItems: any = [
    {routerLink: "information", path: "./assets/icons/information.png", title: "Personal information"},
    {routerLink: "drivers", path: "./assets/icons/wheel.png", title: "Drivers"},
    {routerLink: "clients", path: "./assets/icons/user.png", title: "Clients"},
    {routerLink: "history", path: "./assets/icons/history.png", title: "History"}
  ]
  profileImageLink:string = "assets/svetlana.jpg";

  constructor() { }

  ngOnInit(): void {
  }

}
