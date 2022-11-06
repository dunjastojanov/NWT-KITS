import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'secondary-button',
  templateUrl: './secondary.component.html',
  styleUrls: ['./secondary.component.css']
})
export class SecondaryComponent implements OnInit {
  @Input('text') text!: String;
  
  constructor() { }

  ngOnInit(): void {
  }

}
