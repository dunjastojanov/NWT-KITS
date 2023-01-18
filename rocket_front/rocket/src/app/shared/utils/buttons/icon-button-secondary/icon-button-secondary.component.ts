import {Component, Input, OnInit} from '@angular/core';

@Component({
  selector: 'icon-button-secondary',
  templateUrl: './icon-button-secondary.component.html',
  styleUrls: ['./icon-button-secondary.component.css']
})
export class IconButtonSecondaryComponent implements OnInit {

  @Input('path') path!: String;


  constructor() {
  }

  ngOnInit(): void {
  }

}
