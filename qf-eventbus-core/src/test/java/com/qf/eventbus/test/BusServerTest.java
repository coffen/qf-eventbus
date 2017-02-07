package com.qf.eventbus.test;

import com.qf.eventbus.ActionData;
import com.qf.eventbus.BusServer;
import com.qf.eventbus.BusSignaler;
import com.qf.eventbus.Listener;

public class BusServerTest {
	
	public static void main(String[] args) {
		final BusSignaler s = BusServer.buildSignaler();
		s.buildChannel("test");
		s.register("test", TestEvent.class);
		s.subscribe("test", new Listener() {			
			public <T> void onEvent(ActionData<T> data) {
				System.out.println(data.getRegisterId() + ":" + data.getData());
			}
		});
		ActionData<String> testData = new ActionData<String>() {	
			
			private String word = "Hello, World!";
			
			@Override
			public String getRegisterId() {
				return s.getSignalerId();
			}
			
			@Override
			public String getData() {
				return word;
			}
			
			@Override
			public String getChannel() {
				return "test";
			}
		};
		s.fileEvent(TestEvent.class, testData);
	}
	
}
