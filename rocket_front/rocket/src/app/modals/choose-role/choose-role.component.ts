import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { multiSelectProp } from 'src/app/shared/utils/input/multi-select-with-icons/multi-select-with-icons.component';

@Component({
  selector: 'choose-role',
  templateUrl: './choose-role.component.html',
  styleUrls: ['./choose-role.component.css'],
})
export class ChooseRoleComponent implements OnInit {
  @Input('open') open!: boolean;
  @Input('closeFunc') closeFunc!: () => void;
  @Input('userRoleItems') userRoleItems!: multiSelectProp[];
  @Output('onConfirmRole') onConfirmRole = new EventEmitter<string>();

  openErrorToast = false;

  selectedItems: string[] = [];
  constructor() {}

  ngOnInit(): void {}

  setSelectedItems(items: string[]) {
    this.selectedItems = items;
  }
  sendSelectedItems() {
    if (this.selectedItems.length != 1) {
      this.toggleErrorToast();
    } else {
      this.onConfirmRole.emit(this.selectedItems[0]);
    }
  }

  toggleErrorToast = () => {
    this.openErrorToast = !this.openErrorToast;
  };
}
