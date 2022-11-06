import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {
  @Input('open') open!: boolean;
  @Input('closeFunc') closeFunc!: () => void
  @Input('openLoginModal') openLoginModal!: () => void
  
  currentPage = 1;

  constructor() { }

  ngOnInit(): void {
  }

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
}
