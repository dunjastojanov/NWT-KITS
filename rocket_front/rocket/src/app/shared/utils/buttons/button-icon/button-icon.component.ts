import { Component, Input, OnInit } from '@angular/core';

@Component({
  selector: 'button-icon',
  templateUrl: './button-icon.component.html',
  styleUrls: ['./button-icon.component.css']
})
export class ButtonIconComponent implements OnInit {
  @Input('icon') icon!: string;
  @Input('text') text: string;
  constructor() {
      this.text = '';
   }

  ngOnInit(): void {
  }

}
