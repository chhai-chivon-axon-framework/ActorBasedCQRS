package com.example.cqrs.restcontroller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.cqrs.common.BankAccountReplayEventListener;
import com.example.cqrs.event.store.EventStoreEntry;
import com.example.cqrs.readmodel.AccountEntry;
import com.example.cqrs.readmodel.AccountEntryRepository;
import com.example.cqrs.service.EventStoreService;

@RestController
@RequestMapping(BankAccountReplayAPI.API + BankAccountReplayAPI.REPLAY)
public class BankAccountReplayAPI implements ReplayAPI<AccountEntry> {
	
	public static final Logger logger = LoggerFactory.getLogger(BankAccountReplayAPI.class);

	public static final String API = "/api";
	public static final String REPLAY = "/replay/accounts";
	
	private static final String REPLAY_STARTED = "Replaying Event Started";
	private static final String REPLAY_DONE = "Replaying Event Done";
	private static final String AGGREGATE_ID = "/{aggregateId}";

	@Autowired
	private AccountEntryRepository repository;
	
	@Autowired
	private EventStoreService eventStoreService;
	
	@Autowired
	private BankAccountReplayEventListener replayEventListener;

	@Override
	@GetMapping
	public Collection<AccountEntry> replayAllEvents() {
		logger.info(REPLAY_STARTED);
		if (repository.findAll().size() > 0) {
			repository.deleteAll();
		}
		List<AccountEntry> accountEntries = new ArrayList<>(); 
		List<EventStoreEntry> events = eventStoreService.getDecodedEventStores();
		List<UUID> uuids = events.stream().map(EventStoreEntry::getAggregateId).collect(Collectors.toList());
		for(UUID uuid : uuids) {
			AccountEntry accountEntry = null; 
			List<EventStoreEntry> eventEntries = eventStoreService.getEventEntriesForAggregate(uuid);
			for(EventStoreEntry event: eventEntries) {
				accountEntry = replayEventListener.applyEvent(event.getPayload());
			}
			if (accountEntry != null) {
				accountEntries.add(accountEntry);
			}
		}
		logger.info(REPLAY_DONE);
		return accountEntries;
	}

	@Override
	@GetMapping(AGGREGATE_ID)
	public AccountEntry replayEventsForAggregate(@PathVariable String aggregateId) {
		logger.info(REPLAY_STARTED);
		repository.findById(UUID.fromString(aggregateId)).ifPresent(value -> {
			repository.delete(value);
		});
		
		AccountEntry accountEntry = null; 
		List<EventStoreEntry> events = eventStoreService.getEventEntriesForAggregate(UUID.fromString(aggregateId));
		for(EventStoreEntry event: events) {
			accountEntry = replayEventListener.applyEvent(event.getPayload());
		}
		logger.info(REPLAY_DONE);
		return accountEntry;
	}

}