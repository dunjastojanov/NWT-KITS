import { Component, OnInit } from '@angular/core';
import { NavigationEnd, Router } from '@angular/router';
import { Store } from '@ngrx/store';
import { CurrentRide } from 'src/app/interfaces/Ride';
import { StoreType } from 'src/app/shared/store/types';

@Component({
  selector: 'request-navbar',
  templateUrl: './request-navbar.component.html',
  styleUrls: ['./request-navbar.component.css'],
})
export class RequestNavbarComponent implements OnInit {
  currentPath: string = '';
  currentRide: CurrentRide | null = null;
  constructor(private router: Router, private store: Store<StoreType>) {
    this.router.events.subscribe((event) => {
      if (event instanceof NavigationEnd) {
        this.currentPath = event.url.split('/')[3];
      }
    });
    this.store.select('currentRide').subscribe((resData) => {
      this.currentRide = resData.currentRide;
    });
  }

  states: { name: string; link: string }[] = [
    { name: '1. Route', link: 'route' },
    { name: '2. Info', link: 'info' },
    { name: '3. Confirm', link: 'confirm' },
    { name: '4. Loby', link: 'loby' },
  ];

  ngOnInit(): void {}
}
