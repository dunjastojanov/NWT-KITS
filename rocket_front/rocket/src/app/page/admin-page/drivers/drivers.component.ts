import {Component, OnInit} from '@angular/core';
import {UserService} from "../../../services/user/user.service";

@Component({
  selector: 'app-drivers',
  templateUrl: './drivers.component.html',
  styleUrls: ['./drivers.component.css'],
})
export class DriversComponent implements OnInit {
  openRegisterDriver: boolean = false;

  toggleOpenRegisterDriver = () => {
    this.openRegisterDriver = !this.openRegisterDriver;
  };

  get filter(): string {
    return this._filter;
  }

  set filter(value: string) {
    this._filter = value;
  }

  private _filter: string = '';

  get currentPage(): string {
    return this._currentPage;
  }

  private service: UserService;

  set currentPage(value: string) {
    this._currentPage = value;
    this.fetch();
  }

  constructor(service: UserService) {
    this.service = service;
    this.fetch();
  }

  fetch() {
    this.service.getDrivers(this.filter, (+this.currentPage - 1).toString(), 4).then(response => {
      this.drivers = response.content;
      this.numberOfPages = response.totalPages;
    })
  }

  drivers = [];
  private _currentPage: string = "1";
  numberOfPages: number = 1;


  ngOnInit(): void {
  }
}
