import {Component, Input, OnInit} from '@angular/core';

@Component({
  selector: 'app-medium-title',
  templateUrl: './medium-title.component.html',
  styleUrls: ['./medium-title.component.css']
})
export class MediumTitleComponent implements OnInit {

  @Input('text') text!: String;

  constructor() {
  }

  ngOnInit(): void {
  }

}
