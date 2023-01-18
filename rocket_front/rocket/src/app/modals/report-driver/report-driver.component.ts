import {Component, Input, OnInit} from '@angular/core';

@Component({
  selector: 'report-driver',
  templateUrl: './report-driver.component.html',
  styleUrls: ['./report-driver.component.css']
})
export class ReportDriverComponent implements OnInit {
  @Input('open') open!: boolean;
  @Input('driver') driver!: string;
  @Input('closeFunc') closeFunc!: () => void;

  constructor() {
  }

  ngOnInit(): void {
  }

  closeModal() {
    this.closeFunc();
    this.open = false;
  }
}
