package com.example.cqrs.service;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.cqrs.common.PayloadUtils;
import com.example.cqrs.event.store.EventStore;
import com.example.cqrs.event.store.EventStoreEntry;
import com.example.cqrs.event.store.EventStoreRepository;

@Service
public class EventStoreService {
	
	private static final String PAYLOAD = "payload";
	
	@Autowired
	private EventStoreRepository repository;	
	
	public List<EventStore> getAllEventStores() {
		return repository.findAll();
	}
	
	public List<EventStoreEntry> getDecodedEventStores() {
		return mapEventStoresToEventStoreEntries(getAllEventStores());
	}
	
	public List<EventStore> getEventsForAggregate(UUID aggregateId) {
		return repository.findByAggregateId(aggregateId);
	}
	
	public List<EventStoreEntry> getEventEntriesForAggregate(UUID aggregateId){
		return mapEventStoresToEventStoreEntries(getEventsForAggregate(aggregateId));
	}
	
	public List<Object> getPayloadsOfEventStores() {
		return mapEventStoresToObjects(getAllEventStores());
	}
	
	public List<Object> getPayloadsForAggregate(UUID aggregateId) {
		return getEventsForAggregate(aggregateId).stream()
			.map(EventStore::getPayload)
			.map(payload -> {
				try {
					return PayloadUtils.deserialize(payload);
				} catch (ClassNotFoundException | IOException e) {
					e.printStackTrace();
				}
				return payload;
			}).collect(Collectors.toList());
	}
	
	private List<Object> mapEventStoresToObjects(List<EventStore> eventStores) {
		return eventStores.stream().map(EventStore::getPayload).map(payload -> {
				try {
					return PayloadUtils.deserialize(payload);
				} catch (ClassNotFoundException | IOException e) {
					e.printStackTrace();
				}
				return payload;
				}).collect(Collectors.toList());
	}
		
	private List<EventStoreEntry> mapEventStoresToEventStoreEntries(List<EventStore> eventStores) {
		return eventStores.stream().map(this::mapEventStoreToEventStoreEntry).collect(Collectors.toList());
	}

	private EventStoreEntry mapEventStoreToEventStoreEntry(EventStore eventStore) {
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
