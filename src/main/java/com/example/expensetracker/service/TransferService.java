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
    public Transfer transfer(Long sourceAccountId, Long destinationAccountId, Double amount, String description) {
        if (sourceAccountId.equals(destinationAccountId)) {
            throw new RuntimeException("Le compte source et destination ne peuvent pas être les mêmes");
        }

        Account source = accountRepository.findById(sourceAccountId)
                .orElseThrow(() -> new RuntimeException("Compte source non trouvé"));

        Account destination = accountRepository.findById(destinationAccountId)
                .orElseThrow(() -> new RuntimeException("Compte destination non trouvé"));

        if (source.getBalance() < amount) {
            throw new RuntimeException("Solde insuffisant sur le compte source");
        }

        // Mise à jour des soldes
        source.setBalance(source.getBalance() - amount);
        destination.setBalance(destination.getBalance() + amount);

        accountRepository.save(source);
        accountRepository.save(destination);

        // Création du transfert
        Transfer transfer = new Transfer();
        transfer.setAmount(amount);
        transfer.setDate(LocalDate.now());
        transfer.setFromAccount(source);
        transfer.setToAccount(destination);
        transfer.setDescription(description);

        return transferRepository.save(transfer);
    }
}
