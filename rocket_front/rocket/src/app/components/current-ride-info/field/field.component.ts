import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'field',
  templateUrl: './field.component.html',
  styleUrls: ['./field.component.css']
})
export class FieldComponent implements OnInit {
  @Input('title') title!: string;
  @Input('value') value!: string;
  @Input('subValue') subValue? : string;
  @Input('icon') titleIcon? : string;
  constructor() { }

  ngOnInit(): void {
  }

}
