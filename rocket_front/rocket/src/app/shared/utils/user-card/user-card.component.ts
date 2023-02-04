import {Component, Input, OnInit} from '@angular/core';
import {User} from "../../../interfaces/User";

@Component({
  selector: 'user-card',
  templateUrl: './user-card.component.html',
  styleUrls: ['./user-card.component.css']
})
export class UserCardComponent implements OnInit {

  @Input("user") user!: User;

  openInfo: boolean = false;

  toggleOpenInfo = () => {
    this.openInfo = !this.openInfo;
  }

  constructor() {
  }


  ngOnInit(): void {
  }

}
