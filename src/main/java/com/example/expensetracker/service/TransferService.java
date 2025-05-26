package com.example.expensetracker.service;

import com.example.expensetracker.model.Account;
import com.example.expensetracker.model.Transfer;
import com.example.expensetracker.repository.AccountRepository;
import com.example.expensetracker.repository.TransferRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
public class TransferService {

    private final AccountRepository accountRepository;
    private final TransferRepository transferRepository;

    public TransferService(AccountRepository accountRepository, TransferRepository transferRepository) {
        this.accountRepository = accountRepository;
        this.transferRepository = transferRepository;
    }

    @Transactional
    public Transfer transferToSavings(Double amount) {
        Account principal = accountRepository.findByName("principal")
                .orElseThrow(() -> new RuntimeException("Compte principal non trouvé"));
        Account savings = accountRepository.findByName("savings")
                .orElseThrow(() -> new RuntimeException("Compte savings non trouvé"));

        if (principal.getBalance() < amount) {
            throw new RuntimeException("Solde insuffisant");
        }

        principal.setBalance(principal.getBalance() - amount);
        savings.setBalance(savings.getBalance() + amount);

        accountRepository.save(principal);
        accountRepository.save(savings);

        Transfer transfer = new Transfer();
        transfer.setAmount(amount);
        transfer.setDate(LocalDate.now());
        transfer.setFromAccount(principal);
        transfer.setToAccount(savings);

        return transferRepository.save(transfer);
    }
}
