import {Component, Input, OnChanges, OnInit, SimpleChanges} from '@angular/core';
import Chart from 'chart.js/auto';
import {ReportData} from "../../../interfaces/Report";

@Component({
  selector: 'line-chart',
  templateUrl: './line-chart.component.html',
  styleUrls: ['./line-chart.component.css']
})
export class LineChartComponent implements OnInit, OnChanges {

  constructor() {
  }

  ngOnInit(): void {
  }

  ngOnChanges(changes: SimpleChanges) {
    this.createChart();
  }

  public chart: any;
  @Input("type") public type!: string;
  @Input("data") data!: ReportData;
  @Input("average") public average!: number;
  @Input("total") public total!: number;
  @Input("unit") public unit!: string;

  createChart() {

    if (this.chart !== undefined) {
      this.chart.destroy();
    }

    this.chart = new Chart("stats", {
      type: 'line',

      data: {// values on X-Axis
        labels: [...this.data.labels],
        datasets: [
          ...this.data.datasets
        ]
      },
      options: {
        aspectRatio: 2.5
      }

    });
  }

  getAverage():string {
    if (this.unit === "KM") {
      return (+this.average/1000).toFixed(1);
    }
    return this.average.toString();
  }

  getTotal():string {
    if (this.unit === "KM") {
      return (+this.total/1000).toFixed(1);
    }
    return this.total.toString();
  }

}
