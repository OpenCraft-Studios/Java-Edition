package org.lwjgl.input;

import java.awt.Event;

import lombok.Getter;

public final class CustomEvent {

	@Getter
	private int eventType;
	
	@Getter
	private Event event;

	public CustomEvent(int eventType, Event event) {
		this.eventType = eventType;
		this.event = event;
	}
	
}
