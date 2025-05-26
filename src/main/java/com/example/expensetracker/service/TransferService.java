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
    public Transfer transfer(Long sourceId, Long destId, Double amount, String description) {
        if (sourceId.equals(destId)) {
            throw new IllegalArgumentException("Le compte source et destination ne peuvent pas être les mêmes");
        }

        Account source = accountRepository.findById(sourceId)
                .orElseThrow(() -> new IllegalArgumentException("Compte source non trouvé"));
        Account destination = accountRepository.findById(destId)
                .orElseThrow(() -> new IllegalArgumentException("Compte destination non trouvé"));

        if (source.getBalance() < amount) {
            throw new IllegalArgumentException("Solde insuffisant sur le compte source");
        }

        source.setBalance(source.getBalance() - amount);
        destination.setBalance(destination.getBalance() + amount);

        accountRepository.save(source);
        accountRepository.save(destination);

        Transfer transfer = new Transfer();
        transfer.setAmount(amount);
        transfer.setDate(LocalDate.now());
        transfer.setFromAccount(source);
        transfer.setToAccount(destination);
        transfer.setDescription(description);

        return transferRepository.save(transfer);
    }
}
