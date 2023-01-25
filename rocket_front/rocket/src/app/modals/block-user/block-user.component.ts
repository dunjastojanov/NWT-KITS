import {Component, Input, OnInit} from '@angular/core';
import {User} from "../../interfaces/User";
import {UserService} from "../../services/user/user.service";
import {ToastrService} from "ngx-toastr";

@Component({
  selector: 'block-user',
  templateUrl: './block-user.component.html',
  styleUrls: ['./block-user.component.css']
})
export class BlockUserComponent implements OnInit {


  @Input('open') open!: boolean;
  @Input('closeFunc') closeFunc!: () => void;
  @Input('user') user!: User;

  message: string = "";


  constructor(private service: UserService, private toastr: ToastrService) {
    this.service = service;
    this.toastr = toastr;
  }


  onBlock() {
    this.service.blockUser(this.user.email, this.message).then((result) => {
      this.toastr.success(result);
    });
  }

  ngOnInit(): void {
  }

}
