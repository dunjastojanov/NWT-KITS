import {Component, Input, OnInit} from '@angular/core';

@Component({
  selector: 'view-more-button',
  templateUrl: './view-more-button.component.html',
  styleUrls: ['./view-more-button.component.css']
})
export class ViewMoreButtonComponent implements OnInit {

  constructor() {
    console.log(this.id)
  }

  @Input() id!: string;
  openDetailedRoute: boolean = false;

  toggleDetailedRoute = () => {
    this.openDetailedRoute = !this.openDetailedRoute;
  }

  ngOnInit(): void {
  }

}
