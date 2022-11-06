import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'pagination',
  templateUrl: './pagination.component.html',
  styleUrls: ['./pagination.component.css']
})
export class PaginationComponent implements OnInit {

  constructor() { }

  numberOfPages: number = 7;

  pages: any = [...Array(this.numberOfPages+1).keys()].slice(1)

  ngOnInit(): void {
  }

}
