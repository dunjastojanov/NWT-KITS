import {Component, Input, OnInit} from '@angular/core';

@Component({
  selector: 'app-title',
  templateUrl: './title.component.html',
  styleUrls: ['./title.component.css']
})
export class TitleComponent implements OnInit {
  @Input('header') header?: String;
  @Input('title') title!: String;
  @Input('subtitle') subtitle?: String;

  constructor() {
  }

  ngOnInit(): void {
  }

}
