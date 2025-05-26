package com.example.expensetracker.service;

import com.example.expensetracker.model.Account;
import com.example.expensetracker.model.Expense;
import com.example.expensetracker.repository.AccountRepository;
import com.example.expensetracker.repository.ExpenseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final AccountRepository accountRepository;

    public ExpenseService(ExpenseRepository expenseRepository, AccountRepository accountRepository) {
        this.expenseRepository = expenseRepository;
        this.accountRepository = accountRepository;
    }

    @Transactional
    public Expense addExpense(String name, Double amount) {
        Account principal = accountRepository.findByName("principal")
                .orElseThrow(() -> new RuntimeException("Compte principal non trouvé"));

        if (principal.getBalance() < amount) {
            throw new RuntimeException("Solde insuffisant");
        }

        principal.setBalance(principal.getBalance() - amount);
        accountRepository.save(principal);

        Expense expense = new Expense();
        expense.setName(name);
        expense.setAmount(amount);
        expense.setDate(LocalDate.now());
        expense.setAccount(principal);

        return expenseRepository.save(expense);
    }
}
