package com.albaraka.digital.dto.operation;

import com.albaraka.digital.model.enums.OperationType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OperationRequest {

    @NotNull
    private OperationType type;

    @NotNull
    @DecimalMin(value = "0.01", message = "Le montant doit être strictement positif")
    private BigDecimal amount;

    // Obligatoire uniquement pour TRANSFER
    private String destinationAccountNumber;
}