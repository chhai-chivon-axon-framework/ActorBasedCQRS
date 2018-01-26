package com.example.cqrs.common;

public interface Mapper <SOURCE, DESTINATION> {
	DESTINATION map(SOURCE source);
}
