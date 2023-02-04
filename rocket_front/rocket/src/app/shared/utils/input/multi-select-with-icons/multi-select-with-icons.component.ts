import {Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges} from '@angular/core';

type item = { path: string; title: string; selected: boolean };
export type multiSelectProp = {
  path: string;
  title: string;
  selected?: boolean;
};

@Component({
  selector: 'multi-select',
  templateUrl: './multi-select-with-icons.component.html',
  styleUrls: ['./multi-select-with-icons.component.css'],
})
export class MultiSelectWithIconsComponent implements OnInit, OnChanges {
  @Input('itemProps') itemProps!: multiSelectProp[];
  @Input('allowMultiple') allowMultipleChoices: boolean;

  @Input('selected') selected: string[] = [];
  @Output() selectedItems = new EventEmitter<string[]>();
  items: item[];

  constructor() {
    this.allowMultipleChoices = true;
    this.items = [];
  }

  ngOnChanges(changes: SimpleChanges): void {
    this.items.map(item => {
      if (this.selected.indexOf(item.title) !== -1) {
        item.selected = true;
      }})
  }

  ngOnInit(): void {
    this.items = this.itemProps.map((item) => {
      return {
        path: item.path,
        title: item.title,
        selected: item.selected ? item.selected : false,
      };
    });
  }



  toggleSelected(item: item) {
    item.selected = !item.selected;
    if (!this.allowMultipleChoices) {
      this.items = this.items.map((i) =>
        item.selected && i.title !== item.title ? {...i, selected: false} : i
      );
    }
    this.selected = this.items
      .filter((item) => item.selected)
      .map((item) => item.title);
    this.selectedItems.emit(this.selected);
  }
}
