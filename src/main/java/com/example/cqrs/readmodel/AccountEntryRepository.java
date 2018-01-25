package com.example.cqrs.readmodel;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountEntryRepository extends JpaRepository<AccountEntry, String> {
	Optional<AccountEntry> findByAccountNumber(String accountNumber);
}
