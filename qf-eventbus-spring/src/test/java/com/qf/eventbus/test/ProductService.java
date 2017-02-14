package com.qf.eventbus.test;

import com.qf.eventbus.spring.anno.InterceptType;
import com.qf.eventbus.spring.anno.Interceptor;
import com.qf.eventbus.spring.anno.Listener;
import com.qf.eventbus.spring.anno.Publisher;
import com.qf.eventbus.spring.anno.Subscriber;

@Publisher
@Subscriber
public class ProductService {
	
	@Interceptor(event={"cacheEvent", "esEvent"}, type=InterceptType.PARAMETER_AFTER)
	public void save(long id) {
		System.out.println("product saved: " + id);
	}
	
	@Interceptor(event="cacheEvent", type=InterceptType.PARAMETER_AFTER)
	public void updateArchive(long id) {
		System.out.println("product deleted: " + id);
	}
	
	@Listener(channel="es")
	public void updateEs(long id) {
		System.out.println("product es updated: " + id);
	}
	
}
