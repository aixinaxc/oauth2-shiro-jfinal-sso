package com.auth.shiro;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListener;

public class MySessionListener  implements SessionListener{
	
	@Override
	public void onExpiration(Session arg0) {
		// TODO Auto-generated method stub
		System.out.println("会话过期：" + arg0.getId());  
	}
	@Override
	public void onStart(Session arg0) {
		// TODO Auto-generated method stub
		 System.out.println("会话创建：" + arg0.getId());  
	}
	@Override
	public void onStop(Session arg0) {
		// TODO Auto-generated method stub
		System.out.println("会话停止：" + arg0.getId());  
	}    
}
