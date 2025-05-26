package com.example.expensetracker.config;

import com.example.expensetracker.model.Account;
import com.example.expensetracker.repository.AccountRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer {

    private final AccountRepository accountRepository;

    public DataInitializer(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @PostConstruct
    public void init() {
        if(accountRepository.findByName("principal").isEmpty()) {
            Account principal = new Account();
            principal.setName("principal");
            principal.setBalance(1000.0); // solde initial, modifie si besoin
            accountRepository.save(principal);
        }
        if(accountRepository.findByName("savings").isEmpty()) {
            Account savings = new Account();
            savings.setName("savings");
            savings.setBalance(0.0);
            accountRepository.save(savings);
        }
    }
}
