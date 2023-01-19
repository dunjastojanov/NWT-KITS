import {Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges} from '@angular/core';

@Component({
  selector: 'pagination',
  templateUrl: './pagination.component.html',
  styleUrls: ['./pagination.component.css']
})
export class PaginationComponent implements OnInit, OnChanges {
  constructor() {
  }

  ngOnChanges(changes: SimpleChanges): void {
    this.pages = [...Array(this.numberOfPages).keys()].map(i => (i + 1).toString());
  }

  @Input() numberOfPages!: number;
  @Input() currentPage!: string;
  @Output() currentPageChange = new EventEmitter<string>();
  @Output() numberOfPagesChange = new EventEmitter<number>();
  pages: string[] = []

  ngOnInit(): void {
  }

  onPageChange(page: string): void {
    this.currentPage = page;
    this.currentPageChange.emit(page);
  }

}
