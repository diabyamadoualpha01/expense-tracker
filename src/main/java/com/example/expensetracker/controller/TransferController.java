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
            Transfer transfer = transferService.transferToSavings(request.getAmount());
            return ResponseEntity.ok(transfer);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    public static class TransferRequest {
        private Double amount;

        // getters/setters
        public Double getAmount() { return amount; }
        public void setAmount(Double amount) { this.amount = amount; }
    }
}
