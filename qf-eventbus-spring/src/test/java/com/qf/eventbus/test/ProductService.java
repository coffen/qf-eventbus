package com.qf.eventbus.test;

import com.qf.eventbus.spring.anno.InterceptType;
import com.qf.eventbus.spring.anno.Interceptor;
import com.qf.eventbus.spring.anno.Listener;
import com.qf.eventbus.spring.anno.Publisher;
import com.qf.eventbus.spring.anno.Subscriber;

@Publisher
@Subscriber
public class ProductService {
	
	@Interceptor(event={"cacheEvent", "esEvent"}, type=InterceptType.RETURNING)
	public void save() {
		
	}
	
	@Interceptor(event="cacheEvent")
	public void updateArchive() {
		
	}
	
	@Listener(channel="esEvent")
	public void updateAmountAndSales() {
		
	}
	
}
