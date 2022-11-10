import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'app-vertical-navbar',
  templateUrl: './vertical-navbar.component.html',
  styleUrls: ['./vertical-navbar.component.css']
})
export class VerticalNavbarComponent implements OnInit {

  constructor() { }

  @Input("navbarItems") navbarItems:any;

  @Input("profileImageLink") profileImageLink:string = "assets/fiona.jpg";

  ngOnInit(): void {
  }

}
