package com.uber.rocket.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReportData {
    private List<Dataset> datasets;
    private List<String> labels;

    public void addDataset(Dataset dataset) {
        if (datasets == null) {
            datasets = new ArrayList<>();
        }
        datasets.add(dataset);
    }

    public void addLabel(String label) {
        if (labels == null) {
            labels = new ArrayList<>();
        }
        labels.add(label);
    }

}
