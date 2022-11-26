import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'icon',
  templateUrl: './icon.component.html',
  styleUrls: ['./icon.component.css']
})
export class IconComponent implements OnInit {
  @Input('path') path!: string;
  @Input('bounce') bounce: boolean;
  constructor() { 
    this.bounce = true;
  }

  ngOnInit(): void {
  }

}
