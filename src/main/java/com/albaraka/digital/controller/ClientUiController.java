package com.albaraka.digital.controller;

import com.albaraka.digital.dto.operation.OperationRequest;
import com.albaraka.digital.service.DocumentService;
import com.albaraka.digital.service.OperationQueryService;
import com.albaraka.digital.service.OperationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping("/client")
@RequiredArgsConstructor
public class ClientUiController {

    private final OperationQueryService operationQueryService;
    private final OperationService operationService;
    private final DocumentService documentService;

    @GetMapping("/home")
    public String clientHome(@RequestParam(defaultValue = "0") int page,
                             @RequestParam(defaultValue = "10") int size,
                             Model model) {
        Page<?> operations = operationQueryService.getCurrentClientOperations(page, size);
        model.addAttribute("operationsPage", operations);
        model.addAttribute("operationRequest", new OperationRequest());
        return "client-home";
    }

    @PostMapping("/operations")
    public String createOperation(@ModelAttribute OperationRequest operationRequest) {
        // Valide l’opération pour le client courant (logique existante dans OperationService)
        operationService.createClientOperation(operationRequest);
        return "redirect:/client/home";
    }

    @PostMapping("/operations/{id}/justificatif")
    public String uploadJustificatif(@PathVariable Long id,
                                     @RequestParam("file") MultipartFile file) throws IOException {
        documentService.uploadJustificatif(id, file);
        return "redirect:/client/home";
    }
}