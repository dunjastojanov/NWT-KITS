import {Component, Input, OnInit} from '@angular/core';

@Component({
  selector: 'app-vertical-navbar',
  templateUrl: './vertical-navbar.component.html',
  styleUrls: ['./vertical-navbar.component.css']
})
export class VerticalNavbarComponent implements OnInit {

  constructor() {
  }

  @Input("navbarItems") navbarItems: any;

  @Input("profileImageLink") profileImageLink: string = "assets/profile-placeholder.png";

  ngOnInit(): void {
  }

}
