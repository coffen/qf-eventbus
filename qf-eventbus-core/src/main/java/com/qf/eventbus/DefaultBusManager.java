package com.qf.eventbus;

import com.qf.eventbus.event.ChannelCreateEvent;
import com.qf.eventbus.event.RegisteEvent;
import com.qf.eventbus.event.SubscribeEvent;
import com.qf.eventbus.event.UnregisteEvent;
import com.qf.eventbus.event.UnsubscribeEvent;

public class DefaultBusManager extends AbstractBusManager {
	
	private final String innerChannel = "_bus";
	
	public DefaultBusManager() {
		registerInnerEvent();
	}

	@Override
	public <T> void onEvent(ActionData<T> data) {
		
	}
	
	private void registerInnerEvent() {
		mainSignaler.buildChannel("innerChannel");
		mainSignaler.register(ChannelCreateEvent.class, "innerChannel");
		mainSignaler.register(RegisteEvent.class, innerChannel);
		mainSignaler.register(UnregisteEvent.class, innerChannel);
		mainSignaler.register(SubscribeEvent.class, innerChannel);
		mainSignaler.register(UnsubscribeEvent.class, innerChannel);
	}

}
