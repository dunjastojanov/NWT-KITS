import {Component, Input, OnInit} from '@angular/core';

@Component({
  selector: 'app-filter',
  templateUrl: './filter.component.html',
  styleUrls: ['./filter.component.css']
})
export class FilterComponent implements OnInit {

  constructor() { }

  @Input('open') open!: boolean;
  @Input('closeFunc') closeFunc!: () => void;

  ngOnInit(): void {
  }

}
