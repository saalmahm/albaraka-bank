package com.albaraka.digital.dto.operation;

import com.albaraka.digital.model.enums.OperationStatus;
import com.albaraka.digital.model.enums.OperationType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class OperationResponse {

    private Long id;
    private OperationType type;
    private BigDecimal amount;
    private OperationStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime executedAt;
    private String accountSourceNumber;
    private String accountDestinationNumber;
}