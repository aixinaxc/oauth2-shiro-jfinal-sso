package com.model;

import com.jfinal.plugin.activerecord.ActiveRecordPlugin;

/**
 * Generated by JFinal, do not modify this file.
 * <pre>
 * Example:
 * public void configPlugin(Plugins me) {
 *     ActiveRecordPlugin arp = new ActiveRecordPlugin(...);
 *     _MappingKit.mapping(arp);
 *     me.add(arp);
 * }
 * </pre>
 */
public class _MappingKit {

	public static void mapping(ActiveRecordPlugin arp) {
		arp.addMapping("app_user", AppUser.class);
		arp.addMapping("auth_app", "app_id", App.class);
		arp.addMapping("auth_user", "user_id", User.class);
		arp.addMapping("auth_user_app", UserApp.class);
		arp.addMapping("auth_res", "res_id", Res.class);
		arp.addMapping("auth_role", "role_id", Role.class);
		arp.addMapping("auth_role_res", RoleRes.class);
		arp.addMapping("auth_user_role", RoleUser.class);

	}
}

