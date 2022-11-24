package com.uber.rocket.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Report {

    private double average;
    private double total;
    private ReportData data;

    public void addDataset(Dataset dataset) {
        if (data == null) {
            data = new ReportData();
        }
        data.addDataset(dataset);
    }

    public void addLabel(String label) {
        if (data == null) {
            data = new ReportData();
        }
        data.addLabel(label);
    }
}
