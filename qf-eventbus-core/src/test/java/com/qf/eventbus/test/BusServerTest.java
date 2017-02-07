package com.qf.eventbus.test;

import com.qf.eventbus.ActionData;
import com.qf.eventbus.BusServer;
import com.qf.eventbus.BusSignaler;
import com.qf.eventbus.ChannelHandler;
import com.qf.eventbus.Listener;

public class BusServerTest {
	
	public static void main(String[] args) {
		final BusSignaler s = BusServer.buildSignaler();
		String channel = "test";
		boolean created = s.buildChannel(channel);
		if (created) {
			ChannelHandler<?> handler = s.getChannelHandler(channel);
			handler.open();
		}
		s.register(channel, TestEvent.class);
		s.subscribe(channel, new Listener() {			
			public <T> void onEvent(ActionData<T> data) {
				System.out.println(data.getRegisterId() + ":" + data.getData());
			}
		});
		ActionData<String> testData = new ActionData<String>("Hello, world!");
		s.fileEvent(TestEvent.class, testData);
	}
	
}
