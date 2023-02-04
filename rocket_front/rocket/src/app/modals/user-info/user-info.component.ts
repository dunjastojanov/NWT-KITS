import {Component, Input, OnInit} from '@angular/core';
import {User} from "../../interfaces/User";
import {UserService} from "../../services/user/user.service";
import {ToastrService} from "ngx-toastr";

@Component({
  selector: 'app-user-info',
  templateUrl: './user-info.component.html',
  styleUrls: ['./user-info.component.css']
})
export class UserInfoComponent implements OnInit {


  @Input('open') open!: boolean;
  @Input('closeFunc') closeFunc!: () => void;
  @Input('user') user!: User;

  showBlock: boolean = false;

  toggleBlock() {
    this.showBlock =!this.showBlock;
  }

  private service: UserService;
  private toastr: ToastrService;

  constructor(service: UserService, toastr: ToastrService) {
    this.service = service;
    this.toastr = toastr;
  }

  information: any = []

  onBlock() {
    this.toggleBlock()
  }

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

  onClose() {
    this.closeFunc()
    this.open = false;
  }
}
