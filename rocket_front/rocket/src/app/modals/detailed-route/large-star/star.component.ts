import {Component, Input, OnInit} from '@angular/core';

@Component({
  selector: 'star',
  templateUrl: './star.component.html',
  styleUrls: ['./star.component.css']
})
export class StarComponent implements OnInit {

  @Input() rating!: number;
  @Input() number!: number;
  @Input() size!: "small" | "large";

  constructor() {
  }

  ngOnInit(): void {
  }

}
