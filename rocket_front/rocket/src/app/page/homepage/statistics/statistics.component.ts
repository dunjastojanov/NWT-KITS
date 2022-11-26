import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'statistics',
  templateUrl: './statistics.component.html',
  styleUrls: ['./statistics.component.css']
})
export class StatisticsComponent implements OnInit {
  vehiclesNum : number = 0;
  tripsNum: number = 0;
  peopleNum: number = 0;
  clientsNum: number = 0;
  vehiclesNumMax : number = 32002;
  tripsNumMax: number = 26642;
  peopleNumMax: number = 13183;
  clientsNumMax: number = 65258;
  constructor() { }

  ngOnInit(): void {
  }
  ngAfterViewInit(): void {
    setInterval(() => {
      if (document.scrollingElement!.scrollTop <= 2056) {
        this.vehiclesNum = 0;
        this.tripsNum = 0;
        this.peopleNum = 0;
        this.clientsNum = 0;
      }
        
      if (this.vehiclesNumMax - this.vehiclesNum > 200) {
        this.vehiclesNum += 200;
      }
      else if (this.vehiclesNumMax - this.vehiclesNum > 0) {
        this.vehiclesNum++;
      }
      if (this.tripsNumMax - this.tripsNum > 200) {
        this.tripsNum += 200;
      }
      else if (this.tripsNumMax - this.tripsNum > 0) {
        this.tripsNum++;
      }
      if (this.peopleNumMax - this.peopleNum > 200) {
        this.peopleNum += 200;
      }
      else if (this.peopleNumMax - this.peopleNum > 0) {
        this.peopleNum++;
      }
      if (this.clientsNumMax - this.clientsNum > 200) {
        this.clientsNum += 200;
      }
      else if (this.clientsNumMax - this.clientsNum > 0) {
        this.clientsNum++;
      }
    }, 5);
  }

  numberWithCommas(num: number) {
    return num.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
}
}
