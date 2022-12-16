import { Component, OnInit, Input } from '@angular/core';
import { RegisterService } from './register.service';

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

  constructor(private service: RegisterService) {}

  ngOnInit(): void {}

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
      } else {
        this.errorMessage = 'Korisnik sa unetim mailom vec postoji';
        this.toggleErrorToast();
      }
    } else {
      this.errorMessage = 'Nevalidni podaci';
      this.toggleErrorToast();
    }
  };
  valid = (): boolean => {
    if (!/^.+[@].+[\.].+$/.test(this.email)) {
      console.log('email');
      return false;
    }
    if (
      !/^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])[0-9a-zA-Z]{8,20}$/.test(this.password)
    ) {
      console.log('password');
      return false;
    }
    if (this.password !== this.confirmPassword) {
      console.log('confirm pass');
      return false;
    }
    if (!/^[A-Za-z0-9]*$/.test(this.firstName)) {
      console.log('first name');
      return false;
    }
    if (!/^[A-Za-z0-9]*$/.test(this.lastName)) {
      console.log('last name');
      return false;
    }
    if (!/^[A-Za-z\s]*$/.test(this.city)) {
      console.log('city');
      return false;
    }
    if (!/^06[0-9]{7,8}$/.test(this.phone)) {
      console.log('phone');
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
