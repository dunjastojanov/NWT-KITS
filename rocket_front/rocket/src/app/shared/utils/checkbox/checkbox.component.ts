import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'checkbox',
  templateUrl: './checkbox.component.html',
  styleUrls: ['./checkbox.component.css'],
})
export class CheckboxComponent implements OnInit {
  @Input('text') text!: string;
  @Input('checked') checked: boolean;
  @Input('disabled') disabled: boolean;
  @Output() checkedEmitter = new EventEmitter<boolean>();
  constructor() {
    this.checked = false;
    this.disabled = false;
  }

  ngOnInit(): void {}

  emit() {
    this.checkedEmitter.emit(this.checked);
  }
}
