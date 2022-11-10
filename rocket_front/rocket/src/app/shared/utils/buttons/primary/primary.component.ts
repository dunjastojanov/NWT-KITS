import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'primary-button',
  templateUrl: './primary.component.html',
  styleUrls: ['./primary.component.css']
})
export class PrimaryButtonComponent implements OnInit {
  @Input('text') text!: String;

  constructor() { }

  ngOnInit(): void {
  }
}

export class PrimaryComponent {
}
