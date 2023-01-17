import { Component, Input } from '@angular/core';
import { Store } from '@ngrx/store';
import {
  DestinationsAction,
  DestinationsActionType,
} from 'src/app/shared/store/destinations-slice/destinations.actions';
import { StoreType } from 'src/app/shared/store/types';
import { InputComponent } from 'src/app/shared/utils/input/input/input.component';
import { RouteService } from '../route.service';

@Component({
  selector: 'input-destination',
  templateUrl: './input-destination.component.html',
  styleUrls: ['./input-destination.component.css'],
})
export class InputDestinationComponent extends InputComponent {
  @Input('index') index!: number;
  @Input('showBin') showBin!: boolean;
  destination: string = '';
  lastDispatched: string = this.value;
  constructor(private service: RouteService, private store: Store<StoreType>) {
    super();
  }

  override ngOnInit() {
    super.ngOnInit();
    this.service.trigger$().subscribe((x) => this.setStore());
  }

  setStore() {
    if (this.destination !== this.lastDispatched) {
      this.lastDispatched = this.destination;
      this.store.dispatch(
        new DestinationsAction(DestinationsActionType.UPDATE, {
          index: this.index,
          address: this.destination,
        })
      );
    }
  }

  onFocusOut(event: any): void {
    this.destination = event.target.value;
    if (this.destination === '') {
      this.lastDispatched = 'not empty';
    }

    this.setStore();
  }

  removeItem() {
    this.store.dispatch(
      new DestinationsAction(DestinationsActionType.REMOVE, this.index)
    );
  }

  clearInput() {
    this.value = '';
    this.destination = '';
    this.lastDispatched = 'not empty';
    this.setStore();
  }
}
