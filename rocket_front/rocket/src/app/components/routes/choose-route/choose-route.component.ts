import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {Store} from '@ngrx/store';
import {Destination} from 'src/app/interfaces/Destination';
import {
  DestinationsAction,
  DestinationsActionType,
} from 'src/app/shared/store/destinations-slice/destinations.actions';
import {StoreType} from 'src/app/shared/store/types';
import {CdkDragDrop} from '@angular/cdk/drag-drop';

@Component({
  selector: 'choose-route',
  templateUrl: './choose-route.component.html',
  styleUrls: ['./choose-route.component.css'],
})
export class ChooseRouteComponent implements OnInit {
  @ViewChild('newInput', {read: ElementRef}) newInput!: ElementRef;
  destinations: Array<Destination> = [];

  constructor(private store: Store<StoreType>) {
    store.select('destinations').subscribe((resData) => {
      this.destinations = resData.destinations;
    });
  }

  addNewComponent() {
    this.newInput.nativeElement.hidden = false;
    this.store.dispatch(new DestinationsAction(DestinationsActionType.ADD, ''));
    this.newInput.nativeElement.hidden = true;
  }

  ngOnInit(): void {
  }

  drop(event: CdkDragDrop<Destination[]>) {
    const droppingTo = event.currentIndex;
    const droppingFrom = event.container.data.indexOf(event.item.data);
    this.store.dispatch(
      new DestinationsAction(DestinationsActionType.SWITCH, {
        firstIndex: droppingFrom,
        secondIndex: droppingTo,
      })
    );
  }
}
