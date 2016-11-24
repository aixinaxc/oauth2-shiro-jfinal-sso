package com.auth.base;



import com.auth.ctrl.*;
import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.core.JFinal;
import com.jfinal.ext.handler.ContextPathHandler;
import com.jfinal.ext.plugin.shiro.ShiroInterceptor;
import com.jfinal.ext.plugin.shiro.ShiroPlugin;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.c3p0.C3p0Plugin;
import com.jfinal.plugin.redis.RedisPlugin;
import com.model._MappingKit;

public class Config extends JFinalConfig{
	/**
	 * 供Shiro插件使用。
	 */
	private Routes routes;


	/**
	 * 配置常量
	 */
	public void configConstant(Constants me) {
		// 加载少量必要配置，随后可用PropKit.get(...)获取值
		PropKit.use("a_little_config.txt");
		me.setDevMode(PropKit.getBoolean("devMode", true));
	}
	
	/**
	 * 配置路由
	 */
	public void configRoute(Routes me) {
		this.routes = me;
		me.add("/", InitCtrl.class);
		me.add("/res", ResCtrl.class);
		me.add("/role", RoleCtrl.class);
		me.add("/app", AppCtrl.class);
		me.add("/auth", AuthCtrl.class);

	}
	
	public static C3p0Plugin createC3p0Plugin() {
		return new C3p0Plugin(PropKit.get("jdbcUrl"), PropKit.get("user"), PropKit.get("password").trim());
	}
	
	/**
	 * 配置插件
	 */
	public void configPlugin(Plugins me) {
		// 配置C3p0数据库连接池插件
		C3p0Plugin C3p0Plugin = createC3p0Plugin();
		me.add(C3p0Plugin);
		
		// 配置ActiveRecord插件
		ActiveRecordPlugin arp = new ActiveRecordPlugin(C3p0Plugin);
		me.add(arp);
		
		// 所有配置在 MappingKit 中搞定
		_MappingKit.mapping(arp);


		//配置shiro
		ShiroPlugin shiroPlugin = new ShiroPlugin(this.routes);
		shiroPlugin.setLoginUrl("/");//登陆url：未验证成功跳转
		shiroPlugin.setSuccessUrl("/");//登陆成功url：验证成功自动跳转
		shiroPlugin.setUnauthorizedUrl("/unauth");//授权url：未授权成功自动跳转
		me.add(shiroPlugin);
		
		 //缓存user模块 到 redis
		 RedisPlugin codeRedis=new RedisPlugin("auth","127.0.0.1");
		 me.add(codeRedis);
		
	}
	
	/**
	 * 配置全局拦截器
	 */
	public void configInterceptor(Interceptors me) {
		me.add(new ShiroInterceptor());
	}
	
	/**
	 * 配置处理器
	 */
	public void configHandler(Handlers me) {
		me.add(new ContextPathHandler("contextPath"));
	}
	
	/**
	 * 建议使用 JFinal 手册推荐的方式启动项目
	 * 运行此 main 方法可以启动项目，此main方法可以放置在任意的Class类定义中，不一定要放于此
	 */
	public static void main(String[] args) {
		JFinal.start("WebRoot", 80, "/", 5);
	}
}
