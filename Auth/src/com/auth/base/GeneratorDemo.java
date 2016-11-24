package com.auth.base;

import javax.sql.DataSource;

import com.jfinal.kit.PathKit;
import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.dialect.MysqlDialect;
import com.jfinal.plugin.activerecord.generator.Generator;
import com.jfinal.plugin.c3p0.C3p0Plugin;

/**
 * GeneratorDemo
 */
public class GeneratorDemo {
	
	public static DataSource getDataSource() {
		Prop p = PropKit.use("a_little_config.txt");
		C3p0Plugin c3p0Plugin = new C3p0Plugin(p.get("jdbcUrl"), p.get("user"), p.get("password"));
		c3p0Plugin.start();
		return c3p0Plugin.getDataSource();
	}
	
	public static void main(String[] args) {
		// base com.model 所使用的包名
		String baseModelPackageName = "com.model.base";
		// base com.model 文件保存路径
		String baseModelOutputDir = PathKit.getWebRootPath() + "/../src/com/model/base";
		
		// com.model 所使用的包名 (MappingKit 默认使用的包名)
		String modelPackageName = "com.model";
		// com.model 文件保存路径 (MappingKit 与 DataDictionary 文件默认保存路径)
		String modelOutputDir = baseModelOutputDir + "/..";
		
		// 创建生成器
		Generator gernerator = new Generator(getDataSource(), baseModelPackageName, baseModelOutputDir, modelPackageName, modelOutputDir);
		// 设置数据库方言
		gernerator.setDialect(new MysqlDialect());
		// 添加不需要生成的表名
		/*gernerator.addExcludedTable("api_user","api_cate","api_auth","api_api","db_verify"
				,"db_user_file","db_user","db_upgrade","db_region"
				,"db_personalized_package","db_person_name_relation"
				,"db_person_medical_projects_name","db_person_emergency_card"
				,"db_person","db_order_person","db_order","db_migration"
				,"db_message","db_medical_projects_reference_value"
				,"db_medical_projects_reference_population","db_medical_projects_info"
				,"db_medical_projects","db_location","db_hospital_star"
				,"db_hospital_package_type","db_hospital_package_relation"
				,"db_hospital_package","db_hospital","db_healthy_card"
				,"db_feedback","db_company_user_role","db_company_user_brand"
				,"db_company_user","db_company_role_res","db_company_role"
				,"db_company_res","db_company_log","db_city","db_carousel"
				,"db_apply","db_admin_log","db_admin_action","db_admin"
				,"db_access_token","db_depart","db_db_hospital_depart"
				,"db_hospital_v2","db_my_device","db_device","db_device_preoject"
				,"db_brand","db_goods_spec","db_goods","db_sort","db_file"
				,"db_search_key_word","db_appoint","db_pedmoeter");*/
		// 设置是否在 Model 中生成 dao 对象
		gernerator.setGenerateDaoInModel(true);
		// 设置是否生成字典文件
		gernerator.setGenerateDataDictionary(false);
		// 设置需要被移除的表名前缀用于生成modelName。例如表名 "osc_user"，移除前缀 "osc_"后生成的model名为 "User"而非 OscUser
		gernerator.setRemovedTableNamePrefixes("auth_");
		// 生成
		gernerator.generate();
	}
}




