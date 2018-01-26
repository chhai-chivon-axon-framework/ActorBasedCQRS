package com.example.cqrs.restcontroller;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.cqrs.common.EventStoreMapper;
import com.example.cqrs.common.PayloadUtils;
import com.example.cqrs.event.store.EventStore;
import com.example.cqrs.event.store.EventStoreEntry;
import com.example.cqrs.event.store.EventStoreRepository;

@RestController
@RequestMapping(EventStoreRestController.API + EventStoreRestController.EVENT_STORE)
public class EventStoreRestController {
	
	public static final String API = "/api";
	public static final String EVENT_STORE = "/eventstores";
	public static final String DECODE = "/decode";
	public static final String OBJECTS = "/objects";
	
	EventStoreMapper mapper = new EventStoreMapper();
	
	@Autowired
	private EventStoreRepository eventRepository;
	
	@GetMapping 
    public List<EventStore> getEventStores() {
		return eventRepository.findAll();
	}
	
	@GetMapping(OBJECTS)
	public List<Object> getPayloadsOfEventStores() {
		return eventRepository.findAll().stream()
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
	
	@GetMapping(DECODE)
	public List<EventStoreEntry> getDecodedEventStores() {
		return eventRepository.findAll().stream()
			.map(e -> mapper.map(e)).collect(Collectors.toList());
	}
}
