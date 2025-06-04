import {
  Component,
  Input,
  OnChanges,
  SimpleChanges,
  ViewChild,
} from '@angular/core';
import {
  ApexNonAxisChartSeries,
  ApexResponsive,
  ApexChart,
  ApexPlotOptions,
  ApexGrid,
  ChartComponent,
} from 'ng-apexcharts';

export type ChartOptions = {
  series: ApexNonAxisChartSeries;
  chart: ApexChart;
  responsive: ApexResponsive[];
  plotOptions: ApexPlotOptions;
  grid: ApexGrid;
  labels: string[];
  colors: string[];
};

@Component({
  selector: 'app-overall-progress',
  standalone: false,
  templateUrl: './overall-progress.component.html',
  styleUrl: './overall-progress.component.css',
})
export class OverallProgressComponent implements OnChanges {
  @ViewChild('chart') chart!: ChartComponent;

  @Input() series: number[] = [];
  @Input() labels: string[] = [];

  public chartOptions: ChartOptions = {
    series: [],
    chart: {
      width: 400,
      height: 400,
      type: 'donut',
    },
    labels: [],
    colors: ['rgba(0, 102, 153, 0.6)', 'rgba(51, 204, 204, 0.6)', 'rgba(255, 102, 0, 0.6)'],
    plotOptions: {
      pie: {
        startAngle: -90,
        endAngle: 90,
        offsetY: 10,
        donut: {
          labels: {
            show: false, 
          },
        },
      },
    },
    grid: {
      padding: {
        bottom: -80,
      },
    },
    responsive: [
      {
        breakpoint: 480,
        options: {
          chart: {
            width: 200,
          },
          legend: {
            position: 'bottom',
          },
        },
      },
    ],
  };

  ngOnChanges(changes: SimpleChanges) {
    if (changes['series'] || changes['labels']) {
      this.chartOptions.series = this.series || [];
      this.chartOptions.labels = this.labels || [];
    }
  }
}
