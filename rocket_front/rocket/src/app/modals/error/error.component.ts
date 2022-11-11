import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'toast-error',
  templateUrl: './error.component.html',
  styleUrls: ['./error.component.css']
})
export class ErrorComponent implements OnInit {
  @Input('message') message!: string
  @Input('open') open!: boolean;
  @Input('closeFunc') closeFunc!: () => void;
  
  constructor() { }

  ngOnInit(): void {
  }

}
