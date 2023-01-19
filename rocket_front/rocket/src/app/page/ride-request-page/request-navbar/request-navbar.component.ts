import {Component, OnInit} from '@angular/core';
import {NavigationEnd, Router} from '@angular/router';

@Component({
  selector: 'request-navbar',
  templateUrl: './request-navbar.component.html',
  styleUrls: ['./request-navbar.component.css'],
})
export class RequestNavbarComponent implements OnInit {
  currentPath: string = '';

  constructor(private router: Router) {
    this.router.events.subscribe((event) => {
      if (event instanceof NavigationEnd) {
        this.currentPath = event.url.split('/')[3];
      }
    });
  }

  states: { name: string; link: string }[] = [
    { name: '1. Route', link: 'route' },
    { name: '2. Info', link: 'info' },
    { name: '3. Confirm', link: 'confirm' },
    { name: '4. Loby', link: 'loby' },
  ];

  ngOnInit(): void {
  }
}
