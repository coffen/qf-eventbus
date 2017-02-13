package com.qf.eventbus.test;

import com.qf.eventbus.spring.anno.EventBinding;

@EventBinding(name="cacheEvent", channel="cache")
public class CacheEvent {

}
