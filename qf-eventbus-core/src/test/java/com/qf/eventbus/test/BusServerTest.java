package com.qf.eventbus.test;

import com.qf.eventbus.ActionData;
import com.qf.eventbus.BusServer;
import com.qf.eventbus.BusSignaler;
import com.qf.eventbus.ChannelHandler;
import com.qf.eventbus.Listener;

public class BusServerTest {
	
	public static void main(String[] args) {
		final BusSignaler s1 = BusServer.buildSignaler();
		String channel = "test";
		boolean created = s1.buildChannel(channel);
		ChannelHandler<?> handler = null;
		if (created) {
			handler = s1.getChannelHandler(channel);
			handler.open();
		}
		s1.register(channel, TestEvent.class);
		s1.subscribe(channel, new Listener() {			
			public <T> void onEvent(ActionData<T> data) {
				System.out.println(s1.getSignalerId() + ":" + data.getData());
			}
		});
		final BusSignaler s2 = BusServer.buildSignaler();
		s2.subscribe(channel, new Listener() {			
			public <T> void onEvent(ActionData<T> data) {
				System.out.println(s2.getSignalerId() + ":" + data.getData());
			}
		});
		ActionData<String> testData = new ActionData<String>("Hello, world!");
		s1.fileEvent(TestEvent.class, testData);
		
		handler.close(true);
	}
	
}
