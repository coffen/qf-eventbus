package com.qf.eventbus.test;

import org.springframework.stereotype.Service;

import com.qf.eventbus.spring.anno.Listener;

@Service("cacheService")
public class CacheServiceImpl implements CacheService {
	
	@Listener(channel="cache")
	public void onChange(long id) {
		System.out.println("product cache updated: " + id);
	}

}
