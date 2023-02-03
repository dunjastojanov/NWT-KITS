import {Component, Input, OnInit} from '@angular/core'

@Component({
  selector: 'app-label',
  templateUrl: './label.component.html',
  styleUrls: ['./label.component.css']
})
export class LabelComponent implements OnInit {
  @Input('text') text!: string;
  @Input('icon') icon?: string;
  @Input('gap') gap: string;
  @Input('dimension') dimension: string;
  @Input('id') id?:string;

  constructor() {
    this.gap = "gap-5";
    this.dimension = "!w-6 !h-6"
  }

  ngOnInit(): void {
  }

}
