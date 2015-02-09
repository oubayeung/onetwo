package org.onetwo.common.fish.plugin;

import org.onetwo.common.spring.web.mvc.config.JFishMvcPluginListener;
import org.springframework.web.context.WebApplicationContext;


public class EmptyJFishPluginAdapter extends AbstractJFishPlugin<Object> {
	
//	private static JFishPluginAdapter instance = new JFishPluginAdapter();
	
	private JFishMvcConfigurerListenerAdapter emptyJFishMvcConfigurerListener = new JFishMvcConfigurerListenerAdapter(this);
	
	
	EmptyJFishPluginAdapter() {
		super();
	}

	@Override
	public void setPluginInstance(Object plugin) {
	}

	@Override
	public void onStartWebAppConext(WebApplicationContext appContext) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStopWebAppConext() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init(JFishPluginMeta pluginMeta) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public JFishMvcPluginListener getJFishMvcConfigurerListener() {
		return emptyJFishMvcConfigurerListener;
	}

	@Override
	public boolean registerMvcResources() {
		return false;
	}

	@Override
	public boolean isEmptyPlugin() {
		return true;
	}

	
}
