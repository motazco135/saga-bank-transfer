package com.bank.account.repository;

import com.bank.account.domain.AccountEntity;
import lombok.extern.java.Log;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<AccountEntity, Log> {
    Optional<AccountEntity> findByAccountNumber(String accountNumber);
}

