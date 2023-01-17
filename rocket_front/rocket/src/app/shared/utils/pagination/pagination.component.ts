import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';

@Component({
  selector: 'pagination',
  templateUrl: './pagination.component.html',
  styleUrls: ['./pagination.component.css']
})
export class PaginationComponent implements OnInit {

  constructor() { }

  @Input() numberOfPages!: number;

  @Input() currentPage!: string;
  @Output() currentPageChange = new EventEmitter<string>();
  pages: any = [...Array(this.numberOfPages).keys()].slice(1)

  ngOnInit(): void {
  }

  onPageChange(page: string): void {
    this.currentPage = page;
    this.currentPageChange.emit(page);
  }

}
