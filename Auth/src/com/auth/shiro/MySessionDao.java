package com.auth.shiro;

import java.io.Serializable;

import com.auth.base.BaseConfig;
import com.jfinal.plugin.redis.Cache;
import com.jfinal.plugin.redis.Redis;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.CachingSessionDAO;

public class MySessionDao extends CachingSessionDAO{

	@Override
	protected void doDelete(Session arg0) {
		// TODO Auto-generated method stub
		Cache codeCache = Redis.use("auth");
        codeCache.del("sessionId:" + arg0.getId());
	}

	@Override
	protected void doUpdate(Session arg0) {
		// TODO Auto-generated method stub
		Cache codeCache = Redis.use("auth");
		codeCache.set("sessionId:" + arg0.getId(),arg0);
	}

	@Override
	protected Serializable doCreate(Session arg0) {
		// TODO Auto-generated method stub
		Serializable sessionId = generateSessionId(arg0);
		assignSessionId(arg0, sessionId);
		Cache codeCache = Redis.use("auth");
		codeCache.set("sessionId:" + sessionId, arg0);
		return sessionId;
	}

	@Override
	protected Session doReadSession(Serializable arg0) {
		// TODO Auto-generated method stub
		Cache codeCache = Redis.use("auth");
		return codeCache.get("sessionId:" + arg0);
	}

	

}
