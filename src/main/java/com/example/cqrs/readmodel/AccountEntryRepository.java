package com.example.cqrs.readmodel;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountEntryRepository extends JpaRepository<AccountEntry, UUID> {
	Optional<AccountEntry> findById(UUID id);
	Optional<AccountEntry> findByAccountNumber(String accountNumber);
	Optional<AccountEntry> findByAccountNumberAndAccountName(String accountNumber, String accountName);
}
