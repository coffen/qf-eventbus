package com.qf.eventbus.test;

import com.qf.eventbus.Event;
import com.qf.eventbus.spring.anno.EventBinding;

@EventBinding(channel="cache")
public class CacheEvent implements Event {

}
