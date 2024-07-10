package com.bank.trasfer.domain.repository;

import com.bank.trasfer.domain.TransferEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TransferRepository extends JpaRepository<TransferEntity, UUID> {

}
