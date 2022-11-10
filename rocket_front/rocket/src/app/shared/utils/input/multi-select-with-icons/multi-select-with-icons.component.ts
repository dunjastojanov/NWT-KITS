import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'multi-select',
  templateUrl: './multi-select-with-icons.component.html',
  styleUrls: ['./multi-select-with-icons.component.css']
})
export class MultiSelectWithIconsComponent implements OnInit {

  constructor() { }

  items: Array<any> = [
    {
      path: "assets/icons/pets.png", title: "Pet friendly", selected: false
    },
    {
      path: "assets/icons/kids.png", title: "Kid friendly", selected: false
    }
  ]

  ngOnInit(): void {
  }

  toggleSelected(item: any) {
    item.selected = !item.selected;
  }
}
