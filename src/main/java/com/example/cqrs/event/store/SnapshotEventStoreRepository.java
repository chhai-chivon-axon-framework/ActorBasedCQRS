package com.example.cqrs.event.store;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SnapshotEventStoreRepository extends JpaRepository<SnapshotEventStore, UUID> {
	Optional<SnapshotEventStore> findById(String id);
	Optional<SnapshotEventStore> findByAggregateId(UUID aggregateId);
}
