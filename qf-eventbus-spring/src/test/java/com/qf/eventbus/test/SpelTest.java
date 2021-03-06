package com.qf.eventbus.test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.junit.Test;

import com.qf.eventbus.spring.util.SpelExpressionUtil;

public class SpelTest {
	
	@Test
	public void test() {
		final Person p = new Person();
		p.setAge(38);
		p.setName("Coffen");
		List<Child> childList = new ArrayList<Child>();
		Child c1 = new Child();
		c1.setAge(11);
		c1.setName("Tom");
		childList.add(c1);
		Child c2 = new Child();
		c2.setAge(8);
		c2.setName("Mark");
		childList.add(c2);
		p.setChildren(childList);
		
		final Product pp = new Product();
		pp.setId(1112);
		Category cc = new Category();
		cc.setName("服饰");
		pp.setCategory(cc);
		
		// 预解析
		SpelExpressionUtil.preParse("#root.children[0].name");
		SpelExpressionUtil.preParse("#root['father'].children.size()");
		
		// 第一次运行
		int count = 32;
		long start = System.currentTimeMillis();
		final CountDownLatch latch1 = new CountDownLatch(count);
		for (int i = 0; i < count; i++) {
			Thread t = new Thread(new Runnable() {				
				public void run() {
					long u = System.currentTimeMillis();
					System.out.println(SpelExpressionUtil.parse(p, "#root.children[0].name"));
					System.out.println(SpelExpressionUtil.parse(new String[] { "father", "gift" }, new Object[] { p, pp }, "#root['father'].children.size()"));
					System.out.println("单次运行时间:" + (System.currentTimeMillis() - u));
					
					latch1.countDown();
				}
			});
			t.setDaemon(false);
			t.start();
		}
		try {
			latch1.await();
			System.out.println("总时间: " + (System.currentTimeMillis() - start));
		} 
		catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		// 第二次运行
		start = System.currentTimeMillis();
		final CountDownLatch latch2 = new CountDownLatch(count);
		for (int i = 0; i < count; i++) {
			Thread t = new Thread(new Runnable() {				
				public void run() {
					long u = System.currentTimeMillis();
					System.out.println(SpelExpressionUtil.parse(p, "#root.children[0].name"));
					System.out.println(SpelExpressionUtil.parse(new String[] { "father", "gift" }, new Object[] { p, pp }, "#root['father'].children.size()"));
					System.out.println("单次运行时间:" + (System.currentTimeMillis() - u));
					
					latch2.countDown();
				}
			});
			t.setDaemon(false);
			t.start();
		}
		try {
			latch2.await();
			System.out.println("总时间: " + (System.currentTimeMillis() - start));
		} 
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}

class Person {
	
	private String name;
	private int age;
	
	List<Child> children;
	
	public int getAge() {
		return age;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public void setAge(int age) {
		this.age = age;
	}
	
	public List<Child> getChildren() {
		return children;
	}
	
	public void setChildren(List<Child> children) {
		this.children = children;
	}
	
}

class Child extends Person {
	
	private Person father;
	
	public Person getFather() {
		return father;
	}
	
	public void setFather(Person father) {
		this.father = father;
	}
	
}

class Product {
	
	private int id;
	
	private Category category;
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public Category getCategory() {
		return category;
	}
	
	public void setCategory(Category category) {
		this.category = category;
	}
	
}

class Category {
	
	private String name;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
}
