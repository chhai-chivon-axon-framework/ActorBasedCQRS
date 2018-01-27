package com.example.cqrs.restcontroller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.cqrs.event.store.EventStoreEntry;
import com.example.cqrs.event.store.SnapshotEventStore;
import com.example.cqrs.service.SnapshotEventStoreService;

@RestController
@RequestMapping(SnapshotEventStoreAPI.API + SnapshotEventStoreAPI.SNAPSHOT_EVENT_STORE)
public class SnapshotEventStoreAPI {
	public static final String API = "/api";
	public static final String SNAPSHOT_EVENT_STORE = "/snapshoteventstores";
	
	private static final String DECODE = "/decode";
	private static final String OBJECTS = "/objects";
	private static final String AGGREGATE_ID = "/{aggregateId}";
	
	@Autowired
	private SnapshotEventStoreService service;
	
	@GetMapping 
    public List<SnapshotEventStore> getSnapshotEventStores() {
		return service.getAllSnapshotEventStores();
	}
	
	@GetMapping(AGGREGATE_ID) 
    public SnapshotEventStore getSnapshotEventForAggregate(@PathVariable String aggregateId) {
		return service.getSnapshotEventForAggregate(UUID.fromString(aggregateId));
	}
	
	@GetMapping(AGGREGATE_ID + DECODE) 
    public EventStoreEntry getEventEntryForAggregate(@PathVariable String aggregateId) {
		return service.getEventStoreEntryForAggregate(UUID.fromString(aggregateId));
	}
	
	@GetMapping(OBJECTS)
	public List<Object> getPayloadsOfEventStores() {
		return service.getPayloadsOfSnapshotEventStores();
	}
	
	@GetMapping(OBJECTS + AGGREGATE_ID)
	public Object getPayloadOfSnapshotEventStore(@PathVariable String aggregateId) {
		return service.getPayloadForAggregate(UUID.fromString(aggregateId));
	}
	
	@GetMapping(DECODE)
	public List<EventStoreEntry> getDecodedSnapshotEventStores() {
		return service.getDecodedSnapshotEventStores();
	}
}
