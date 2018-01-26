package com.example.cqrs.restcontroller;

import java.util.Collection;

public interface ReplayAPI<T> {
	Collection<T> replayAllEvents();
	T replayEventsForAggregate(String aggregateId);
}
