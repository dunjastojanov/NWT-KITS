import {Component, Input, OnInit} from '@angular/core';
import {sideUser} from 'src/app/interfaces/User';

@Component({
  selector: 'current-ride-client',
  templateUrl: './current-ride-client.component.html',
  styleUrls: ['./current-ride-client.component.css']
})
export class CurrentRideClientComponent implements OnInit {
  @Input('client') client!: sideUser;

  constructor() {
  }

  ngOnInit(): void {
  }

}
