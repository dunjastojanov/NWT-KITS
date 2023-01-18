import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';

type item = { path: string; title: string; selected: boolean };
export type multiSelectProp = { path: string; title: string };

@Component({
  selector: 'multi-select',
  templateUrl: './multi-select-with-icons.component.html',
  styleUrls: ['./multi-select-with-icons.component.css'],
})
export class MultiSelectWithIconsComponent implements OnInit {
  @Input('itemProps') itemProps!: multiSelectProp[];
  @Input('allowMultiple') allowMultipleChoices: boolean;
  @Output() selectedItems = new EventEmitter<string[]>();
  items: item[];

  constructor() {
    this.allowMultipleChoices = true;
    this.items = [];
  }

  ngOnInit(): void {
    this.items = this.itemProps.map((item) => {
      return {path: item.path, title: item.title, selected: false};
    });
  }

  toggleSelected(item: item) {
    item.selected = !item.selected;
    if (!this.allowMultipleChoices) {
      this.items = this.items.map((i) =>
        item.selected && i.title !== item.title ? {...i, selected: false} : i
      );
    }
    let selectedItems: string[] = this.items
      .filter((item) => item.selected)
      .map((item) => item.title);
    this.selectedItems.emit(selectedItems);
  }
}
