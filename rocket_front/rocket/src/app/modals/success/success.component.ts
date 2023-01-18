import {Component, Input, OnInit} from '@angular/core';

@Component({
  selector: 'toast-success',
  templateUrl: './success.component.html',
  styleUrls: ['./success.component.css']
})
export class SuccessComponent implements OnInit {
  @Input('message') message!: string
  @Input('open') open!: boolean;
  @Input('closeFunc') closeFunc!: () => void;

  constructor() {
  }

  ngOnInit(): void {
  }

}
