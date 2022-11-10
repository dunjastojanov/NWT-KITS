import {Component, Input, OnInit} from '@angular/core';

@Component({
  selector: 'app-register-driver',
  templateUrl: './register-driver.component.html',
  styleUrls: ['./register-driver.component.css']
})
export class RegisterDriverComponent implements OnInit {

  @Input('open') open!: boolean;
  @Input('closeFunc') closeFunc!: () => void


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

  constructor() { }

  ngOnInit(): void {
  }

}
