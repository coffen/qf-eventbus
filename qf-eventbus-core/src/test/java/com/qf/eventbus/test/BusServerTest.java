package com.qf.eventbus.test;

import org.junit.Before;
import org.junit.Test;

import com.qf.eventbus.ActionData;
import com.qf.eventbus.BusServer;
import com.qf.eventbus.BusSignaler;
import com.qf.eventbus.Listener;

public class BusServerTest {
	
    private BusServer server;
	
    @Before
	public void init() {
		server = new BusServer();
	}
	
	@Test
	public void testBusSignaler() {
		final BusSignaler s1 = server.buildSignaler();
		String channel = "test";
		boolean created = s1.buildChannel(channel);
		if (created && s1.openChannel(channel)) {
			s1.register(channel, TestEvent.class);
			s1.subscribe(channel, new Listener() {			
				public <T> void onEvent(ActionData<T> data) {
					System.out.println(s1.getSignalerId() + ":" + data.getData());
					data.setRegisterId(null);
				}
			});
			final BusSignaler s2 = server.buildSignaler();
			s2.subscribe(channel, new Listener() {			
				public <T> void onEvent(ActionData<T> data) {
					System.out.println(s2.getSignalerId() + ":" + data.getData());
				}
			});
			ActionData<String> data1 = new ActionData<String>("Hello, world!");
			ActionData<String> data2 = new ActionData<String>("Woo, succeed");
			s1.fileEvent(TestEvent.class, data1);
			s1.fileEvent(TestEvent.class, data2);
			
			s2.fileEvent(TestEvent.class, "test", new ActionData<String>("Another question!"));
			
			// s2.closeChannel(channel);
			// s1.closeChannel(channel);			
		}
	}
	
}
