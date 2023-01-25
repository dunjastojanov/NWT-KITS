import {Component, OnInit} from '@angular/core';
import {User} from "../../../interfaces/User";
import {UserService} from "../../../services/user/user.service";
import {ToastrService} from "ngx-toastr";
import {Store} from "@ngrx/store";
import {StoreType} from "../../../shared/store/types";
import {VehicleService} from 'src/app/services/vehicle/vehicle.service';
import {multiSelectProp} from "../../../shared/utils/input/multi-select-with-icons/multi-select-with-icons.component";

@Component({
  selector: 'app-personal-information',
  templateUrl: './personal-information.component.html',
  styleUrls: ['./personal-information.component.css']
})
export class PersonalInformationComponent implements OnInit {

  data: User = {
    city: "",
    email: "",
    firstName: "",
    id: "",
    lastName: "",
    phoneNumber: "",
    profilePicture: "",
    roles: []
  };

  vehicle = {
    longitude: "",
    latitude: "",
    type: "",
    petFriendly: false,
    kidFriendly: false,
  }

  types: any = [
    {value: "LIMOUSINE", text: "Limousine"},
    {value: "HATCHBACK", text: "Hatchback"},
    {value: "CARAVAN", text: "Caravan"},
    {value: "COUPE", text: "Coupe"},
    {value: "CONVERTIBLE", text: "Convertible"},
    {value: "MINIVAN", text: "MiniVan"},
    {value: "SUV", text: "SUV"},
    {value: "PICKUP", text: "Pick Up"}
  ]

  selectedItems: string[] = [];

  multiSelectItems: multiSelectProp[] = [];

  setSelectedItems(items: string[]) {
    this.selectedItems = items;
  }

  showChangePassword: boolean = false;
  toggleChangePassword() {
    this.showChangePassword =!this.showChangePassword;
  }


  constructor(private store: Store<StoreType>, private toastr: ToastrService, private service: UserService, private vehicleService: VehicleService) {
    let loggedUserSlice = store.select('loggedUser');
    loggedUserSlice.subscribe(
      resData => {
        if (resData.user) {
          this.data = {...resData.user}

          if (this.isDriver()) {
            this.vehicleService.getVehicle().then((vehicle)=>{
              this.vehicle = vehicle;
              if (this.vehicle.kidFriendly) {
                this.selectedItems.push("Kid friendly");

              }
              if (this.vehicle.petFriendly) {
                this.selectedItems.push("Pet friendly");

              }
            })
          }
        }
      }
    )
  }

  isDriver() {
    return this.data?.roles.indexOf("DRIVER") !== -1;
  }

  ngOnInit(): void {
  }

  hasRole(role: string): boolean {
    return this.data !== null && this.data?.roles.indexOf(role) !== -1;
  }

  onSave(): void {

    if (this.data) {
      let dto = {
        firstName: this.data.firstName,
        lastName: this.data.lastName,
        phoneNumber: this.data.phoneNumber,
        city: this.data.city
      }

      if (this.hasRole("DRIVER")) {
        let dto = {
          firstName: this.data.firstName,
          lastName: this.data.lastName,
          phoneNumber: this.data.phoneNumber,
          city: this.data.city,
          kidFriendly: false,
          petFriendly: false,
          type: this.vehicle.type,
        }
        if (this.selectedItems.includes("Kid friendly")) {
          dto.kidFriendly = true;
        }
        if (this.selectedItems.includes("Pet friendly")) {
          dto.petFriendly = true;
        }
        this.service.editDriver(dto).then(() => {
          this.toastr.success("You have successfully sent the request for changing your data.")
        }).catch(() => {
          this.toastr.error("Error with updating profile")
        })
      }
      else {
        this.service.editUser(dto).then(result => {
          this.toastr.success(result.message)
        }).catch(() => {
          this.toastr.error("Error with updating profile")
        })
      }


    }


  }
}
