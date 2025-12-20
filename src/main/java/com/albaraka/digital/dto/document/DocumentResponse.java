package com.albaraka.digital.dto.document;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class DocumentResponse {

    private Long id;
    private String fileName;
    private String fileType;
    private LocalDateTime uploadedAt;
    private Long operationId;
}