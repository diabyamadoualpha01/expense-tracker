package com.example.expensetracker.controller;

import com.example.expensetracker.model.Account;
import com.example.expensetracker.repository.AccountRepository;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountRepository accountRepository;

    public AccountController(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    // GET /accounts : liste de tous les comptes
    @GetMapping
    public List<Account> getAccounts() {
        return accountRepository.findAll();
    }

    // GET /accounts/{id} : récupérer un compte par ID
    @GetMapping("/{id}")
    public ResponseEntity<Account> getAccountById(@PathVariable Long id) {
        return accountRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST /accounts : créer un compte
    @PostMapping
    public ResponseEntity<Account> createAccount(@RequestBody Account newAccount) {
        Account saved = accountRepository.save(newAccount);
        return ResponseEntity.status(201).body(saved); // 201 Created
    }

    // PUT /accounts/{id} : mettre à jour un compte existant
    @PutMapping("/{id}")
    public ResponseEntity<Account> updateAccount(@PathVariable Long id, @RequestBody Account updatedAccount) {
        return accountRepository.findById(id)
                .map(account -> {
                    account.setName(updatedAccount.getName());
                    account.setBalance(updatedAccount.getBalance());
                    Account saved = accountRepository.save(account);
                    return ResponseEntity.ok(saved); // 200 OK
                })
                .orElseGet(() -> ResponseEntity.notFound().build()); // 404
    }

    // DELETE /accounts/{id} : supprimer un compte
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable Long id) {
        if (accountRepository.existsById(id)) {
            accountRepository.deleteById(id);
            return ResponseEntity.noContent().build(); // 204 No Content
        } else {
            return ResponseEntity.notFound().build(); // 404
        }
    }
}
