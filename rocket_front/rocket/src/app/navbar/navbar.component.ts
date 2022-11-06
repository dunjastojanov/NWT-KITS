import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent implements OnInit {
  showLoginModal = false;
  showRegisterModal = false;
  constructor() { }

  ngOnInit(): void {
  }

  toggleLogin = (): void => {
    this.showRegisterModal = false;
    this.showLoginModal = !this.showLoginModal;
  }

  toggleRegister = (): void => {
    this.showLoginModal = false;
    this.showRegisterModal =!this.showRegisterModal;
  }
}
