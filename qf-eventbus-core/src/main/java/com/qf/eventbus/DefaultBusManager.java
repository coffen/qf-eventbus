package com.qf.eventbus;

public class DefaultBusManager extends AbstractBusManager {
	
	private final String innerChannel = "_bus";
	
	public DefaultBusManager() {
		registerInnerEvent();
	}

	@Override
	public <T> void onEvent(ActionData<T> data) {
		
	}
	
	private void registerInnerEvent() {
//		mainSignaler.buildChannel("innerChannel");
//		
//		mainSignaler.register(ChannelCreateEvent.class, "innerChannel");
//		mainSignaler.register(RegisteEvent.class, innerChannel);
//		mainSignaler.register(UnregisteEvent.class, innerChannel);
//		mainSignaler.register(SubscribeEvent.class, innerChannel);
//		mainSignaler.register(UnsubscribeEvent.class, innerChannel);
	}

}
