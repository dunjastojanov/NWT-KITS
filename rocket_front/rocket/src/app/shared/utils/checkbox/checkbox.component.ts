import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'checkbox',
  templateUrl: './checkbox.component.html',
  styleUrls: ['./checkbox.component.css']
})
export class CheckboxComponent implements OnInit {
  @Input('text') text!: string;
  @Input('checked') checked: boolean
  @Input('disabled') disabled: boolean

  constructor() {
    this.checked = false;
    this.disabled = false;
  }

  ngOnInit(): void {
  }

}
