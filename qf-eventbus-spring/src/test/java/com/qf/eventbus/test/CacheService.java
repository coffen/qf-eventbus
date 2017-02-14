package com.qf.eventbus.test;

import com.qf.eventbus.spring.anno.Listener;
import com.qf.eventbus.spring.anno.Subscriber;

@Subscriber
public class CacheService {
	
	@Listener(channel="cache")
	public void onChange(long id) {
		System.out.println("product cache updated: " + id);
	}

}
