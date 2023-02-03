import {Component, Input, OnInit} from '@angular/core';

@Component({
  selector: 'button-icon',
  templateUrl: './button-icon.component.html',
  styleUrls: ['./button-icon.component.css']
})
export class ButtonIconComponent implements OnInit {
  @Input('icon') icon!: string;
  @Input('text') hoverText?: string;
  @Input('fillButton') fillButton: boolean;
  @Input('dimension') dimension: string;
  @Input('groupHoverDimension') groupHoverDimension: string;
  @Input('id') id?: string;

  constructor() {
    this.fillButton = false;
    this.dimension = "w-12 h-12";
    this.groupHoverDimension = "group-hover:!w-10 group-hover:!h-10";
  }

  ngOnInit(): void {
  }
}
