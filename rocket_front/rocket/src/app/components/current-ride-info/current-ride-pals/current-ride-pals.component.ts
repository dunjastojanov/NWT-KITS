import { Component, Input, OnInit } from '@angular/core';
import { RidingPal, sideUser } from 'src/app/interfaces/User';

@Component({
  selector: 'current-ride-pals',
  templateUrl: './current-ride-pals.component.html',
  styleUrls: ['./current-ride-pals.component.css'],
})
export class CurrentRidePalsComponent implements OnInit {
  @Input('pals') ridingPals!: RidingPal[];

  constructor() {}

  ngOnInit(): void {}
}
