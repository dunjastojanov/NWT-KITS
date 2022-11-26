package com.uber.rocket.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EvaluationDTO {
    @NotBlank(message = "Id of request is mandatory")
    public Long id;
    @NotBlank(message = "Confirmation choice must be provided")
    public boolean confirmed;
}
