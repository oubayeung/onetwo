package org.onetwo.plugins.security;

import java.util.List;

import org.onetwo.common.fish.plugin.AbstractJFishPlugin;
import org.onetwo.common.fish.plugin.EmptyJFishMvcConfigurerListener;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.spring.web.authentic.SpringTargetArgumentResolver;
import org.onetwo.common.spring.web.mvc.config.JFishMvcConfigurerListener;
import org.onetwo.plugins.security.client.SsoClientContext;
import org.onetwo.plugins.security.client.SsoClientWebContext;
import org.onetwo.plugins.security.common.SecurityWebContext;
import org.onetwo.plugins.security.server.SsoServerContext;
import org.onetwo.plugins.security.server.SsoServerWebContext;
import org.springframework.core.io.Resource;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;


public class SecurityWebPlugin extends AbstractJFishPlugin<SecurityWebPlugin> {

	private static SecurityWebPlugin instance;
	
	
	public static SecurityWebPlugin getInstance() {
		return instance;
	}
	

	public static String getTemplatePath(String template) {
		return getInstance().getPluginMeta().getPluginConfig().getTemplatePath(template);
	}

	@Override
	public void onMvcContextClasses(List<Class<?>> annoClasses) {
		annoClasses.add(SecurityWebContext.class);
		Resource config = SpringUtils.classpath(SsoServerContext.SSO_SERVER_CONFIG_PATH);
		if(config.exists()){
			annoClasses.add(SsoServerWebContext.class);
		}
		config = SpringUtils.classpath(SsoClientContext.SSO_CLIENT_CONFIG_PATH);
		if(config.exists()){
			annoClasses.add(SsoClientWebContext.class);
		}
	}

	@Override
	public void setPluginInstance(SecurityWebPlugin plugin){
		instance = plugin;
	}

	@Override
	public JFishMvcConfigurerListener getJFishMvcConfigurerListener() {
		return new EmptyJFishMvcConfigurerListener(){

			@Override
			public void onRegisterArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
				argumentResolvers.add(new SpringTargetArgumentResolver());
			}
			
		};
	}


	@Override
	public boolean registerMvcResources() {
		return true;
	}

}