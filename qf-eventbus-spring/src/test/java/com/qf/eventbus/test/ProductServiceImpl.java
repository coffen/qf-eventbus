package com.qf.eventbus.test;

import org.springframework.stereotype.Service;

import com.qf.eventbus.spring.anno.InterceptType;
import com.qf.eventbus.spring.anno.Interceptor;
import com.qf.eventbus.spring.anno.Listener;

@Service("productService")
public class ProductServiceImpl implements ProductService {
	
	@Interceptor(event={"cacheEvent", "esEvent"}, type=InterceptType.PARAMETER_AFTER, expr="#root['id']")
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
