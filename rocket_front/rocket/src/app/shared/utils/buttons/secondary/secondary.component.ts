import {Component, Input, OnInit} from '@angular/core';

@Component({
  selector: 'secondary-button',
  templateUrl: './secondary.component.html',
  styleUrls: ['./secondary.component.css']
})
export class SecondaryComponent implements OnInit {
  @Input('text') text!: String;
  @Input('disabled') disabled: boolean;
  @Input('id') id?: string;

  constructor() {
    this.disabled = false;
  }

  ngOnInit(): void {
  }

}
