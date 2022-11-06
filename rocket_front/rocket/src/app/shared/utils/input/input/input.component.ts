import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'app-input',
  templateUrl: './input.component.html',
  styleUrls: ['./input.component.css']
})
export class InputComponent implements OnInit {
  @Input('text') text!: string;
  @Input('type') type: string;

  constructor() {
    this.type = "text"
   }

  ngOnInit(): void {
  }

}
