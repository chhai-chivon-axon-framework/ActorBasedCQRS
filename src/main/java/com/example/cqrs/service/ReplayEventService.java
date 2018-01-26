package com.example.cqrs.service;

import org.springframework.stereotype.Service;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;

@Service
public class ReplayEventService {
	
	ActorSystem system = ActorSystem.create("ReplayEventSystem");
	ActorRef persistentActor;

}
