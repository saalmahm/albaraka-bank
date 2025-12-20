package com.albaraka.digital.controller;

import com.albaraka.digital.dto.document.DocumentResponse;
import com.albaraka.digital.model.entity.Document;
import com.albaraka.digital.service.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/agent")
@RequiredArgsConstructor
public class AgentDocumentController {

    private final DocumentService documentService;

    @GetMapping("/operations/{id}/documents")
    public List<DocumentResponse> listDocumentsForOperation(@PathVariable Long id) {
        List<Document> docs = documentService.getDocumentsForOperation(id);

        return docs.stream()
                .map(d -> new DocumentResponse(
                        d.getId(),
                        d.getFileName(),
                        d.getFileType(),
                        d.getUploadedAt(),
                        d.getOperation().getId()
                ))
                .toList();
    }

    @GetMapping("/documents/{documentId}/download")
    public ResponseEntity<Resource> downloadDocument(@PathVariable Long documentId) {
        Resource resource = documentService.loadDocumentFile(documentId);

        //  navigateur déterminer si inconnu.
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}