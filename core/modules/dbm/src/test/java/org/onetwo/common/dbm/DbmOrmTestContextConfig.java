package org.onetwo.common.dbm;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.onetwo.common.spring.cache.JFishSimpleCacheManagerImpl;
import org.onetwo.common.spring.config.JFishProfile;
import org.onetwo.dbm.spring.EnableDbm;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@JFishProfile
@ImportResource("classpath:conf/applicationContext-test.xml")
//@Import(JFishdbmSpringConfiguration.class)
@EnableDbm
public class DbmOrmTestContextConfig {

	@Resource
	private DataSource dataSource;
	
	@Bean
	public CacheManager cacheManager() {
		JFishSimpleCacheManagerImpl cache = new JFishSimpleCacheManagerImpl();
		return cache;
	}
	
}
