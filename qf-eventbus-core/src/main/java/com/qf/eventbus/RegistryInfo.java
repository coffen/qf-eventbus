package com.qf.eventbus;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * <p>
 * Project Name: C2C商城
 * <br>
 * Description: 频道注册信息
 * <br>
 * File Name: RegistryInfo.java
 * <br>
 * Copyright: Copyright (C) 2015 All Rights Reserved.
 * <br>
 * Company: 杭州偶尔科技有限公司
 * <br>
 * @author 穷奇
 * @create time：2017年2月5日 下午1:03:21 
 * @version: v1.0
 *
 */
public class RegistryInfo implements Serializable {

	private static final long serialVersionUID = 3137497156933442857L;
	
	public String registryId;
	
	public String getRegistryId() {
		return registryId;
	}
	
	public void setRegistryId(String registryId) {
		this.registryId = registryId;
	}	
	
}
