import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'app-title',
  template: `<div class="flex justify-center">
    <h1 class="title">{{text}}</h1>
    </div>`,
  styleUrls: ['./title.component.css']
})
export class TitleComponent implements OnInit {
  @Input('text') text!: String;

  constructor() { }

  ngOnInit(): void {
  }

}
