package com.example.cqrs.common;

import java.io.IOException;

import org.springframework.beans.BeanUtils;

import com.example.cqrs.event.store.EventStore;
import com.example.cqrs.event.store.EventStoreEntry;

public class EventStoreMapper implements Mapper<EventStore, EventStoreEntry> {

	@Override
	public EventStoreEntry map(EventStore source) {
		EventStoreEntry entry = new EventStoreEntry();
		BeanUtils.copyProperties(source, entry, "payload");
		try {
			entry.setPayload(PayloadUtils.deserialize(source.getPayload()));
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
		return entry;
	}

}
