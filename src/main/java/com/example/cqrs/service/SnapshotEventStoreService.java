package com.example.cqrs.service;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.cqrs.common.PayloadUtils;
import com.example.cqrs.event.store.EventStoreEntry;
import com.example.cqrs.event.store.SnapshotEventStore;
import com.example.cqrs.event.store.SnapshotEventStoreRepository;

@Service
public class SnapshotEventStoreService {
private static final String PAYLOAD = "payload";
	
	@Autowired
	private SnapshotEventStoreRepository repository;	
	
	public List<SnapshotEventStore> getAllSnapshotEventStores() {
		return repository.findAll();
	}
	
	public List<EventStoreEntry> getDecodedSnapshotEventStores() {
		return mapSnapshotEventStoresToEventStoreEntries(getAllSnapshotEventStores());
	}
	
	public SnapshotEventStore getSnapshotEventForAggregate(UUID aggregateId) {
		return repository.findByAggregateId(aggregateId).orElse(null);
	}
	
	public EventStoreEntry getEventStoreEntryForAggregate(UUID aggregateId){
		return mapSnapshotEventStoreToEventStoreEntry(getSnapshotEventForAggregate(aggregateId));
	}
	
	public List<Object> getPayloadsOfSnapshotEventStores() {
		return mapSnapshotEventStoresToObjects(getAllSnapshotEventStores());
	}
	
	public Object getPayloadForAggregate(UUID aggregateId) {
		try {
			return PayloadUtils.deserialize(getSnapshotEventForAggregate(aggregateId).getPayload());
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private List<Object> mapSnapshotEventStoresToObjects(List<SnapshotEventStore> eventStores) {
		return eventStores.stream().map(SnapshotEventStore::getPayload).map(payload -> {
				try {
					return PayloadUtils.deserialize(payload);
				} catch (ClassNotFoundException | IOException e) {
					e.printStackTrace();
				}
				return payload;
				}).collect(Collectors.toList());
	}
		
	private List<EventStoreEntry> mapSnapshotEventStoresToEventStoreEntries(List<SnapshotEventStore> eventStores) {
		return eventStores.stream().map(this::mapSnapshotEventStoreToEventStoreEntry).collect(Collectors.toList());
	}

	private EventStoreEntry mapSnapshotEventStoreToEventStoreEntry(SnapshotEventStore eventStore) {
		EventStoreEntry entry = new EventStoreEntry();
		BeanUtils.copyProperties(eventStore, entry, PAYLOAD);
		try {
			entry.setPayload(PayloadUtils.deserialize(eventStore.getPayload()));
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
		return entry;
	}
}
