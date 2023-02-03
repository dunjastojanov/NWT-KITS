import {Component, Input, OnInit} from '@angular/core';
import {RegisterService} from './register.service';
import {ToastrService} from "ngx-toastr";

@Component({
  selector: 'register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css'],
})
export class RegisterComponent implements OnInit {
  @Input('open') open!: boolean;
  @Input('closeFunc') closeFunc!: () => void;
  @Input('openLoginModal') openLoginModal!: () => void;

  currentPage = 1;
  email = '';
  password = '';
  confirmPassword = '';
  firstName = '';
  lastName = '';
  city = '';
  phone = '';

  openErrorToast = false;
  openSuccessToast = false;
  errorMessage = '';

  constructor(private service: RegisterService, private toastr: ToastrService) {
  }

  ngOnInit(): void {
  }

  nextPage = () => {
    this.currentPage++;
  };
  previousPage = () => {
    this.currentPage--;
  };

  closeModal = () => {
    this.currentPage = 1;
    this.closeFunc();
  };

  onSignUp = async () => {
    if (this.valid()) {
      let data = {
        email: this.email,
        password: this.password,
        firstName: this.firstName,
        lastName: this.lastName,
        city: this.city,
        phoneNumber: this.phone,
      };
      let success: boolean = await this.service.registerUser(data);
      if (success) {
        this.toggleSuccessToast();
        this.toastr.success("Registration successful!")
      } else {
        this.errorMessage = 'User with this email already registered.';
        this.toastr.error(this.errorMessage)
        this.toggleErrorToast();
      }
    } else {
      this.toastr.error(this.errorMessage)
      this.toggleErrorToast();
    }
  };
  valid = (): boolean => {
    if (!/^.+[@].+[\.].+$/.test(this.email)) {
      this.errorMessage =
        'Invalid format of email. Please enter email in format: username@domain.com.';
      return false;
    }
    if (
      !/^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])[0-9a-zA-Z]{8,20}$/.test(this.password)
    ) {
      this.errorMessage =
        'Invalid format of password. Password must be at least 8 characters long and contain at least one capital letter and one number.';
      return false;
    }
    if (this.password !== this.confirmPassword) {
      this.errorMessage =
        'The password and confirmation password you have entered do not match. Please double-check your password and try again.';
      return false;
    }
    if (!/^[A-Za-z\s]+$/.test(this.firstName)) {
      this.errorMessage = 'First name must contain all letters.';
      return false;
    }
    if (!/^[A-Za-z\s]+$/.test(this.lastName)) {
      this.errorMessage = 'Last name must contain all letters.';
      return false;
    }
    if (!/^[A-Za-z\s]+$/.test(this.city)) {
      this.errorMessage = 'City must contain all letters.';
      return false;
    }
    if (!/^06[0-9]{7,8}$/.test(this.phone)) {
      this.errorMessage =
        "Phone number must contain only numbers and must start with '06'.";
      return false;
    }
    return true;
  };

  toggleErrorToast = () => {
    this.openErrorToast = !this.openErrorToast;
  };

  toggleSuccessToast = () => {
    this.openSuccessToast = !this.openSuccessToast;
  };
}
