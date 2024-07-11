package com.bank.account.config;

import com.bank.account.domain.AccountEntity;
import com.bank.account.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class DataLoader implements ApplicationRunner {

    private final AccountRepository accountRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        AccountEntity entityOne = new AccountEntity();
        entityOne.setAccountNumber("A1");
        entityOne.setCustomerName("Motaz");
        entityOne.setAvailableBalance(100);
        accountRepository.save(entityOne);

        AccountEntity entityTwo = new AccountEntity();
        entityTwo.setAccountNumber("A2");
        entityTwo.setCustomerName("Mohammed");
        entityTwo.setAvailableBalance(100);
        accountRepository.save(entityTwo);
    }
}
