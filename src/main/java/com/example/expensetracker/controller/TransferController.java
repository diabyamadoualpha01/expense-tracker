package com.example.expensetracker.controller;
import com.example.expensetracker.repository.TransferRepository;
import com.example.expensetracker.model.Transfer;
import com.example.expensetracker.service.TransferService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/transfer")
public class TransferController {

    private final TransferService transferService;
    private final TransferRepository transferRepository;

    public TransferController(TransferService transferService, TransferRepository transferRepository) {
        this.transferService = transferService;
        this.transferRepository = transferRepository;
    }

    // POST /transfer
    @PostMapping
    public ResponseEntity<?> createTransfer(@RequestBody TransferRequest request) {
        try {
            Transfer transfer = transferService.transfer(
                    request.getSourceAccountId(),
                    request.getDestinationAccountId(),
                    request.getAmount(),
                    request.getDescription()
            );
            return ResponseEntity.ok(transfer);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // GET /transfer : tous les transferts
    @GetMapping
    public ResponseEntity<List<Transfer>> getAllTransfers() {
        return ResponseEntity.ok(transferRepository.findAll());
    }

    // GET /transfer/{id} : transfert par ID
    @GetMapping("/{id}")
    public ResponseEntity<Transfer> getTransferById(@PathVariable Long id) {
        return transferRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // DTO pour POST /transfer
    public static class TransferRequest {
        private Long sourceAccountId;
        private Long destinationAccountId;
        private Double amount;
        private String description;

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
