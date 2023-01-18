import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {SelectItem} from "../../../../interfaces/SelectItem";

@Component({
  selector: 'app-select',
  templateUrl: './select.component.html',
  styleUrls: ['./select.component.css']
})
export class SelectComponent implements OnInit {

  @Input('text') text!: string;
  @Input('placeholder') placeholder?: string;
  @Input('items') items!: SelectItem[];
  @Input('value') value!: string;
  @Input() prop!: string | "kilometers"|"rides"|"money";
  @Output() propChange = new EventEmitter<string>();

  constructor() { }

  ngOnInit(): void {
  }

  onInputChange(event: any) {
    this.prop = event.target.value;
    this.propChange.emit(event.target.value);
  }

}
