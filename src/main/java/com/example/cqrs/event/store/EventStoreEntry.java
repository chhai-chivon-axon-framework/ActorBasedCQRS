package com.example.cqrs.event.store;

import java.util.UUID;

public class EventStoreEntry {
	
	private UUID id;
	private Object payload;
	private String payloadType;
	
	public EventStoreEntry() { }

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public Object getPayload() {
		return payload;
	}

	public void setPayload(Object payload) {
		this.payload = payload;
	}

	public String getPayloadType() {
		return payloadType;
	}

	public void setPayloadType(String payloadType) {
		this.payloadType = payloadType;
	}	
	
}
