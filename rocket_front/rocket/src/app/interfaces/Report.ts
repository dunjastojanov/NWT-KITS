export interface Report {
  average: number;
  total: number;
  data: ReportData;
}

export interface ReportData {
  datasets: ReportDataset[];
  labels: string[];


}

export interface ReportDataset {
  backgroundColor: string;
  data: number[];
  label: string;
}

