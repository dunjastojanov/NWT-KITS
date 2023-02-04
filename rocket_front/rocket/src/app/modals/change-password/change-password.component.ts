import {Component, Input, OnInit} from '@angular/core';
import {UserService} from "../../services/user/user.service";
import {ToastrService} from "ngx-toastr";

@Component({
  selector: 'change-password',
  templateUrl: './change-password.component.html',
  styleUrls: ['./change-password.component.css']
})
export class ChangePasswordComponent implements OnInit {

  constructor(private userService: UserService, private toastr: ToastrService) { }

  @Input('open') open!: boolean;
  @Input('closeFunc') closeFunc!: () => void;

  oldPassword: string = "";
  newPassword: string = "";


  onSubmit = () => {
    let dto = {
      oldPassword: this.oldPassword,
      newPassword: this.newPassword
    }

    this.userService.changePassword(dto)
     .then(() => {
       this.toastr.success("You have successfully changed your password");

     }).catch(err => {
       this.toastr.error(err);
    })


  }


  ngOnInit(): void {
  }

  onClose() {
    this.closeFunc()
    this.open = false;
  }
}
