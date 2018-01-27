package com.example.cqrs.common;

public interface ReplayEventListener<DOMAIN, EVENT> {
	DOMAIN applyEvent(EVENT event);
}
