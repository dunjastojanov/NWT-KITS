import {Component, Input, OnInit} from '@angular/core';

@Component({
  selector: 'primary-button',
  templateUrl: './primary.component.html',
  styleUrls: ['./primary.component.css']
})
export class PrimaryButtonComponent implements OnInit {
  @Input('text') text!: String;
  @Input('idParam') id?: String;

  constructor() {
    console.log(this.id)
  }

  ngOnInit(): void {
  }
}

export class PrimaryComponent {
}
