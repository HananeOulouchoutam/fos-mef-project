import { Component, Input, OnInit } from '@angular/core';
import {
  ApexChart,
  ApexXAxis,
  ApexYAxis,
  ApexTitleSubtitle,
  ApexAxisChartSeries,
} from 'ng-apexcharts';

export type ChartOptions = {
  series: ApexAxisChartSeries;
  chart: ApexChart;
  xaxis: ApexXAxis;
  yaxis: ApexYAxis;
  title: ApexTitleSubtitle;
};

@Component({
  selector: 'app-line-chart',
  standalone: false,
  templateUrl: './line-chart.component.html',
  styleUrl: './line-chart.component.css',
})
export class LineChartComponent implements OnInit {
  @Input() chartData: number[] = [];
  @Input() chartCategories: string[] = [];

  public chartOptions: ChartOptions;

  constructor() {
    this.chartOptions = {
      series: [
        {
          name: 'Sales',
          data: [],
        },
      ],
      chart: {
        type: 'line',
        height: 350,
      },
      title: {
        text: 'Monthly Sales',
      },
      xaxis: {
        categories: [],
      },
      yaxis: {
        title: {
          text: 'Amount',
        },
      },
    };
  }

  ngOnInit(): void {
    if (this.chartData.length > 0 && this.chartCategories.length > 0) {
      this.chartOptions.series[0].data = this.chartData;
      this.chartOptions.xaxis.categories = this.chartCategories;
    }
  }
}
