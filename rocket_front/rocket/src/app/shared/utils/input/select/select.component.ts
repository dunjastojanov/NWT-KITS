import {Component, Input, OnInit} from '@angular/core';

@Component({
  selector: 'app-select',
  templateUrl: './select.component.html',
  styleUrls: ['./select.component.css']
})
export class SelectComponent implements OnInit {

  @Input('text') text!: string;
  @Input('placeholder') placeholder?: string;
  @Input('items') items!: any;


  constructor() { }

  ngOnInit(): void {
  }

}
