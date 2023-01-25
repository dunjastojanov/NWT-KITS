import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {UserService} from "../../services/user/user.service";
import {ToastrService} from "ngx-toastr";

@Component({
  selector: 'app-registration-verification',
  templateUrl: './registration-verification.component.html',
  styleUrls: ['./registration-verification.component.css']
})
export class RegistrationVerificationComponent implements OnInit {
  title: string = "";
  bodyText: string = "";
  successful: boolean = false;

  constructor(private route: ActivatedRoute, private userService: UserService, private toastService: ToastrService) {
  }

  ngOnInit(): void {
    this.route.queryParams.subscribe((params) => {
      const tokenForAccountVerification = params['token'];
      this.userService.verifyRegistration(tokenForAccountVerification).then(
        result => {
          if (result === "Successful registration verification") {
            this.successful = true;
            this.title = result;
            this.bodyText = "You can now log in and order an ride"
          }
        }).catch(err => {
        this.bodyText = "It is possible that your token was expired, please try again with different email"
        this.successful = false;
        this.title = "Unsuccessful registration verification";
      });
      setTimeout(()=>{
        window.location.href="http://localhost:4200/"
      },10000)
    });
  }


}
