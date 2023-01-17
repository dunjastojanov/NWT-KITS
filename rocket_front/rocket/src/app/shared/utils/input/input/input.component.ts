import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'app-input',
  templateUrl: './input.component.html',
  styleUrls: ['./input.component.css'],
})
export class InputComponent implements OnInit {
  @Input('text') text: string;
  @Input('type') type: string;
  @Input('name') name!: string;
  @Input('value') value: string;
  @Input('marginBottom') marginBottom: string;

  @Input() prop!: string;
  @Output() propChange = new EventEmitter<string>();

  constructor() {
    this.value = '';
    this.type = 'text';
    this.text = 'Choose destination';
    this.marginBottom = 'mb-6';
  }

  ngOnInit(): void {
  }

  onInputChange(event: any) {
    this.prop = event.target.value;
    this.propChange.emit(event.target.value);
  }
}
