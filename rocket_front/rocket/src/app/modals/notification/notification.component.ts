import { Component, Input, OnInit } from '@angular/core';
import { SocketService } from 'src/app/services/sockets/sockets.service';
import { Notif } from '../../interfaces/Notification';
import { UserRidingStatus } from 'src/app/interfaces/Ride';
import { VehicleService } from '../../services/vehicle/vehicle.service';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'notification',
  templateUrl: './notification.component.html',
  styleUrls: ['./notification.component.css'],
})
export class NotificationComponent implements OnInit {
  @Input('open') open!: boolean;
  @Input('closeFunc') closeFunc!: () => void;
  @Input('notification') notification!: Notif;

  showReviewModal = false;

  toggleReviewModal() {
    this.showReviewModal = !this.showReviewModal;
  }

  constructor(
    private vehicleService: VehicleService,
    private toastr: ToastrService,
    private socketService: SocketService
  ) {}

  ngOnInit(): void {}

  isBasic(type: string) {
    return (
      type === 'RIDE_CANCELED' ||
      type === 'RIDE_CONFIRMED' ||
      type === 'RIDE_SCHEDULED' ||
      type === 'USER_BLOCKED'
    );
  }

  onAccept() {
    if (this.notification.type === 'DRIVER_RIDE_REQUEST') {
      this.socketService.sendResponseOnRideRequest(
        this.notification.entityId,
        this.notification.userId,
        UserRidingStatus.ACCEPTED
      );
    }
    if (this.notification.type === 'PASSENGER_RIDE_REQUEST') {
      this.socketService.sendResponseOnRideRequest(
        this.notification.entityId,
        this.notification.userId,
        UserRidingStatus.ACCEPTED
      );
    }
    if (this.notification.type === 'UPDATE_DRIVER_DATA_REQUEST') {
      this.vehicleService
        .respondDriverDataUpdateRequest(this.notification.entityId, true)
        .then((res) => {
          this.toastr.success(res);
        });
    }
    if (this.notification.type === 'UPDATE_DRIVER_PICTURE_REQUEST') {
      this.vehicleService
        .respondDriverImageUpdateRequest(this.notification.entityId, true)
        .then((res) => {
          this.toastr.success(res);
        });
    }
    if (this.notification.type === 'RIDE_REVIEW') {
      this.toggleReviewModal();
    }
  }

  onDeny() {
    if (this.notification.type === 'DRIVER_RIDE_REQUEST') {
      this.socketService.sendResponseOnRideRequest(
        this.notification.entityId,
        this.notification.userId,
        UserRidingStatus.DENIED
      );
    }
    if (this.notification.type === 'PASSENGER_RIDE_REQUEST') {
      this.socketService.sendResponseOnRideRequest(
        this.notification.entityId,
        this.notification.userId,
        UserRidingStatus.DENIED
      );
    }
    if (this.notification.type === 'UPDATE_DRIVER_DATA_REQUEST') {
      this.vehicleService
        .respondDriverDataUpdateRequest(this.notification.entityId, false)
        .then((res) => {
          this.toastr.success(res);
        });
    }
    if (this.notification.type === 'UPDATE_DRIVER_PICTURE_REQUEST') {
      this.vehicleService
        .respondDriverImageUpdateRequest(this.notification.entityId, false)
        .then((res) => {
          this.toastr.success(res);
        });
    }
    this.closeFunc();
  }

  onClose() {
    this.closeFunc();
    this.open = false;
  }
}
