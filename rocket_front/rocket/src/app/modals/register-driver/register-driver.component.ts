import {Component, Input, OnInit} from '@angular/core';
import {UserService} from "../../services/user/user.service";
import {ToastrService} from "ngx-toastr";

@Component({
  selector: 'app-register-driver',
  templateUrl: './register-driver.component.html',
  styleUrls: ['./register-driver.component.css']
})
export class RegisterDriverComponent implements OnInit {

  @Input('open') open!: boolean;
  @Input('closeFunc') closeFunc!: () => void

  selectedItems: string[] = [];

  setSelectedItems(items: string[]) {
    this.selectedItems = items;
  }

  driver = {
    email: '',
    firstName: '',
    lastName: '',
    city: '',
    vehicleType: 'limousine',
    phoneNumber: '',
    kidFriendly: false,
    petFriendly: false
  }

  types: any = [
    {value: "limousine", text: "Limousine"},
    {value: "hatchback", text: "Hatchback"},
    {value: "caravan", text: "Caravan"},
    {value: "coupe", text: "Coupe"},
    {value: "convertible", text: "Convertible"},
    {value: "minivan", text: "MiniVan"},
    {value: "suv", text: "SUV"},
    {value: "pickup", text: "Pick Up"}
  ]

  currentPage = 1;

  nextPage = () => {
    this.currentPage++;
  }
  previousPage = () => {
    this.currentPage--;
  }

  closeModal = () => {
    this.currentPage = 1;
    this.closeFunc();
  }

  constructor(private service: UserService, private toastr: ToastrService) {
  }

  ngOnInit(): void {
  }

  save() {
    if (this.selectedItems.includes("Kid friendly")) {
      this.driver.kidFriendly = true;
    }
    if (this.selectedItems.includes("Pet friendly")) {
      this.driver.petFriendly = true;
    }
    let dto = this.driver;
    this.service.registerDriver(dto).then(() => {
      this.toastr.success("Driver Saved");
    })
  }
}
