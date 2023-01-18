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
    {name: '1. Route', link: 'route'},
    {name: '2. Time', link: 'time'},
    {name: '3. Friends', link: 'friends'},
    {name: '4. Info', link: 'info'},
    {name: '5. Confirm', link: 'confirm'},
  ];

  ngOnInit(): void {
  }
}
