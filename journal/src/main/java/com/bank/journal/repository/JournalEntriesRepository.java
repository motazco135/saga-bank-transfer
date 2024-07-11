package com.bank.journal.repository;

import com.bank.journal.domain.JournalEntriesEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JournalEntriesRepository extends JpaRepository<JournalEntriesEntity,Long> {
}
