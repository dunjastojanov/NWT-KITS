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
public class Dataset {
    private String label;
    private String backgroundColor;
    private List<Double> data;

    public void addData(Double value) {
        if (data == null) {
            data = new ArrayList<>();
        }
        data.add(value);
    }
}
