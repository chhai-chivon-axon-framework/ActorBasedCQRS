package com.example.cqrs.event.store;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventStoreRepository extends JpaRepository<EventStore, UUID>{
	Optional<EventStore> findById(String id);
	Optional<EventStore> findTop1ByAggregateIdOrderBySequenceDesc(UUID aggregateId);
	List<EventStore> findByAggregateId(UUID aggregateId);
}
