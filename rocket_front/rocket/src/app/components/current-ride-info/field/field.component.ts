import { Component, Input, OnInit } from '@angular/core';
import { UserRidingStatus } from 'src/app/interfaces/Ride';

@Component({
  selector: 'field',
  templateUrl: './field.component.html',
  styleUrls: ['./field.component.css'],
})
export class FieldComponent implements OnInit {
  @Input('title') title!: string;
  @Input('value') value!: string;
  @Input('subValue') subValue?: string;
  @Input('icon') titleIcon?: string;
  @Input('status') status?: UserRidingStatus | null;

  constructor() {}

  ngOnInit(): void {}
}
