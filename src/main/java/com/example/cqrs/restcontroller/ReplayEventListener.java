package com.example.cqrs.restcontroller;

public interface ReplayEventListener<DOMAIN, EVENT> {
	DOMAIN applyEvent(EVENT event);
}
