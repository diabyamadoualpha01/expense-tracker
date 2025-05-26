package com.example.expensetracker.controller;

import com.example.expensetracker.model.Transfer;
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
    public ResponseEntity<Transfer> transfer(@RequestBody TransferRequest request) {
        try {
            Transfer transfer = transferService.transfer(
                    request.getSourceAccountId(),
                    request.getDestinationAccountId(),
                    request.getAmount(),
                    request.getDescription()
            );
            return ResponseEntity.ok(transfer);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    public static class TransferRequest {
        private Long sourceAccountId;
        private Long destinationAccountId;
        private Double amount;
        private String description;

        // getters/setters
        public Long getSourceAccountId() { return sourceAccountId; }
        public void setSourceAccountId(Long sourceAccountId) { this.sourceAccountId = sourceAccountId; }

        public Long getDestinationAccountId() { return destinationAccountId; }
        public void setDestinationAccountId(Long destinationAccountId) { this.destinationAccountId = destinationAccountId; }

        public Double getAmount() { return amount; }
        public void setAmount(Double amount) { this.amount = amount; }

        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
    }
}
