package com.example.expensetracker.controller;

import com.example.expensetracker.service.TransferService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transfer")
public class TransferController {

    private final TransferService transferService;

    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }

    @PostMapping
    public ResponseEntity<?> transfer(@RequestBody TransferRequest request) {
        var transfer = transferService.transfer(
                request.sourceAccountId(),
                request.destinationAccountId(),
                request.amount(),
                request.description()
        );
        return ResponseEntity.ok(transfer);
    }

    // DTO record (Java 17+)
    public record TransferRequest(
            Long sourceAccountId,
            Long destinationAccountId,
            Double amount,
            String description
    ) {}
}
