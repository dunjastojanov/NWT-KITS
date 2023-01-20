import { Component, OnInit } from '@angular/core';
import { Store } from '@ngrx/store';
import * as moment from 'moment';
import { ToastrService } from 'ngx-toastr';
import { RouteService } from 'src/app/components/routes/route.service';
import { sideUser } from 'src/app/interfaces/User';
import { UserService } from 'src/app/services/user/user.service';
import {
  RideInfoAction,
  RideInfoActionType,
} from 'src/app/shared/store/ride-info-slice/ride-info.actions';
import { StoreType } from 'src/app/shared/store/types';
import { RideInfo } from './ride-info.type';

@Component({
  selector: 'app-data-info',
  templateUrl: './data-info.component.html',
  styleUrls: ['./data-info.component.css'],
})
export class DataInfoComponent implements OnInit {
  ride: RideInfo = new RideInfo();
  constructor(
    private store: Store<StoreType>,
    private service: RouteService,
    private userService: UserService,
    private toastr: ToastrService
  ) {
    this.store.select('rideInfo').subscribe((resData) => {
      this.ride = resData.ride;
    });
  }

  friend: string = '';
  pals: string[] = [];
  ngOnInit(): void {
    this.service.setTrigger('nesto');
  }

  setFeatures(items: string[]) {
    this.saveRide(RideInfoActionType.UPDATE_FEATURES, items);
  }

  setSplitFair(item: boolean) {
    this.saveRide(RideInfoActionType.UPDATE_SPLIT, item);
  }

  setCurrentTime(item: boolean) {
    this.saveRide(RideInfoActionType.UPDATE_IS_NOW, item);
    if (item) {
      this.saveRide(RideInfoActionType.UPDATE_TIME, moment().format());
    }
  }

  setVehicleType(value: string): void {
    this.saveRide(RideInfoActionType.UPDATE_VEHICLE_TYPE, value);
  }

  setTime(event: any) {
    const newHours: string[] = event.target.value.split(':');
    const newDate = moment();
    newDate.set({ hour: +newHours[0], minute: +newHours[1] });
    this.saveRide(RideInfoActionType.UPDATE_IS_NOW, false);
    this.saveRide(RideInfoActionType.UPDATE_TIME, newDate.format());
  }

  setFriend(value: any) {
    this.friend = value;
  }

  async addFriend() {
    if (this.friend === '' || this.pals.includes(this.friend)) return;
    const pal = await this.userService.getRidingPal(this.friend);
    if (!pal) {
      this.toastr.error(`There is no user with email ${this.friend}`);
      return;
    }
    this.pals.push(this.friend);
    const friends: sideUser[] = [...this.ride.friends, pal];
    this.saveRide(RideInfoActionType.UPDATE_FRIENDS, friends);
  }
  removeFriend(pal: sideUser) {
    const friends: sideUser[] = this.ride.friends.filter(
      (fr) => fr.email !== pal.email
    );
    this.pals = this.pals.filter((fr) => fr !== pal.email);
    this.saveRide(RideInfoActionType.UPDATE_FRIENDS, friends);
  }

  saveRide(
    actionType: RideInfoActionType,
    payload: string | string[] | boolean | sideUser[]
  ) {
    this.store.dispatch(new RideInfoAction(actionType, payload));
  }

  showTime(): string {
    const time: string = this.ride.time!.split('T')[1];
    const timeSplit: string[] = time.split(':');
    return `${timeSplit[0]}:${timeSplit[1]}`;
  }

  featureIsSelected(feature: string): boolean {
    return this.ride.features.includes(feature);
  }
}
