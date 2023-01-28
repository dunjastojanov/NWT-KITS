import {Component, OnInit} from '@angular/core';
import {User} from "../../interfaces/User";
import {VehicleService} from "../../services/vehicle/vehicle.service";
import {ToastrService} from "ngx-toastr";
import {UserService} from "../../services/user/user.service";

@Component({
  selector: 'status-toggle',
  template: `
    <label class="relative inline-flex items-center cursor-pointer">
      <input type="checkbox" value="" class="sr-only peer" [checked]="this.status" (change)="statusToggle()">
      <div
        class="w-11 h-6 bg-gray-200 rounded-full peer dark:bg-gray-700 peer-checked:after:translate-x-full peer-checked:after:border-white after:content-[''] after:absolute after:top-0.5 after:left-[2px] after:bg-white after:border-gray-300 after:border after:rounded-full after:h-5 after:w-5 after:transition-all dark:border-gray-600 peer-checked:bg-primary-medium"></div>
    </label>
  `,
  styles: []
})
export class StatusToggleComponent implements OnInit {
  status: boolean = false;

  user: User | null = null;

  statusToggle() {
    let status: 'ACTIVE' | 'INACTIVE' | 'DRIVING';
    if (this.status) {
      status = "INACTIVE";
    } else {
      status = "ACTIVE";
    }
    this.vehicleService.changeStatus(status).then(result => {
      this.status = result === 'ACTIVE';
      this.toastr.success("Status updated");
      if (this.user) {
        this.user.status = status;
      }
    })
  }

  constructor(private userService: UserService, private vehicleService: VehicleService, private toastr: ToastrService) {
    userService.getUser().then(user => {
      if (user) {
        if (this.hasRole(user, "DRIVER")) {
          this.status = user?.status !== "INACTIVE";
          this.user = user;
        }
      }
    })
  }

  hasRole(user: User | null, role: string): boolean {
    return user !== null && user?.roles.indexOf(role) !== -1;
  }

  ngOnInit(): void {
  }

}
