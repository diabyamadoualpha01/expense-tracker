package com.example.expensetracker.controller;

import com.example.expensetracker.model.Expense;
import com.example.expensetracker.service.ExpenseService;
import com.example.expensetracker.repository.ExpenseRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/expense")
public class ExpenseController {

    private final ExpenseService expenseService;
    private final ExpenseRepository expenseRepository;

    public ExpenseController(ExpenseService expenseService, ExpenseRepository expenseRepository) {
        this.expenseService = expenseService;
        this.expenseRepository = expenseRepository;
    }

    // POST /expense : ajouter une dépense
    @PostMapping
    public ResponseEntity<Expense> addExpense(@RequestBody ExpenseRequest request) {
        try {
            Expense expense = expenseService.addExpense(request.getName(), request.getAmount());
            return ResponseEntity.ok(expense);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // GET /expense : liste de toutes les dépenses
    @GetMapping
    public ResponseEntity<List<Expense>> getAllExpenses() {
        List<Expense> expenses = expenseRepository.findAll();
        return ResponseEntity.ok(expenses);
    }

    // GET /expense/{id} : récupérer une dépense par son ID
    @GetMapping("/{id}")
    public ResponseEntity<Expense> getExpenseById(@PathVariable Long id) {
        return expenseRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // DTO pour la requête POST
    public static class ExpenseRequest {
        private String name;
        private Double amount;

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public Double getAmount() { return amount; }
        public void setAmount(Double amount) { this.amount = amount; }
    }
}
