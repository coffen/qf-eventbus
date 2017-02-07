package com.qf.eventbus.test;

import com.qf.eventbus.ActionData;
import com.qf.eventbus.BusServer;
import com.qf.eventbus.BusSignaler;
import com.qf.eventbus.Listener;

public class BusServerTest {
	
	public static void main(String[] args) {
		final BusSignaler s1 = BusServer.buildSignaler();
		String channel = "test";
		boolean created = s1.buildChannel(channel);
		if (created && s1.openChannel(channel)) {
			s1.register(channel, TestEvent.class);
			s1.subscribe(channel, new Listener() {			
				public <T> void onEvent(ActionData<T> data) {
					System.out.println(data.getRegisterId() + ":" + data.getData());
					data.setRegisterId(null);
				}
			});
			final BusSignaler s2 = BusServer.buildSignaler();
			s2.subscribe(channel, new Listener() {			
				public <T> void onEvent(ActionData<T> data) {
					System.out.println(data.getRegisterId() + ":" + data.getData());
				}
			});
			ActionData<String> data = new ActionData<String>("Hello, world!");
			s1.fileEvent(TestEvent.class, data);
			s1.fileEvent(TestEvent.class, data);
			
			s2.fileEvent(TestEvent.class, "test", new ActionData<String>("Another question!"));
			
			//s2.closeChannel(channel);
			//s1.closeChannel(channel);			
		}
	}
	
}
