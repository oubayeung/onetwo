package org.onetwo.common.hibernate;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.ImprovedNamingStrategy;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;
import org.hibernate.event.spi.PreInsertEventListener;
import org.hibernate.event.spi.PreUpdateEventListener;
import org.hibernate.event.spi.SaveOrUpdateEventListener;
import org.onetwo.common.log.MyLoggerFactory;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.spring.config.JFishPropertyPlaceholder;
import org.slf4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.orm.hibernate4.LocalSessionFactoryBuilder;

public class ExtLocalSessionFactoryBean extends LocalSessionFactoryBean implements ApplicationContextAware {
	private static final String DEFAULT_HIBERNATE_CONFIG_PREFIX = "hibernate.";
	private static final String EXT_HIBERNATE_CONFIG_PREFIX = "hib.";

	private final Logger logger = MyLoggerFactory.getLogger(this.getClass());
	
	private ApplicationContext applicationContext;
	private PreInsertEventListener[] preInsertEventListeners;
	private PreUpdateEventListener[] preUpdateEventListeners;
	private SaveOrUpdateEventListener[] saveOrUpdateEventListeners;
	
	@Autowired
	private JFishPropertyPlaceholder configHolder; 
	
	public ExtLocalSessionFactoryBean(){
	}
	
	public void afterPropertiesSet() throws IOException {
		if(getHibernateProperties()==null || getHibernateProperties().isEmpty()){
			this.setHibernateProperties(autoHibernateConfig());
		}
		
		super.afterPropertiesSet();
	}
	
	protected Properties autoHibernateConfig(){
		Properties props = configHolder.getMergedConfig();
		Properties hibConfig = new Properties();
		String key = null;
		logger.info("================ hibernate config ================");
		for (Map.Entry<Object, Object> e : props.entrySet()){
			key = e.getKey().toString();
			if(key.startsWith(DEFAULT_HIBERNATE_CONFIG_PREFIX)){
				logger.info("{}: {}", key, e.getValue().toString());
				hibConfig.setProperty(key, e.getValue().toString());
				
			}else if(key.startsWith(EXT_HIBERNATE_CONFIG_PREFIX)){
				key = key.substring(EXT_HIBERNATE_CONFIG_PREFIX.length());
				logger.info("{}: {}", key, e.getValue().toString());
				hibConfig.setProperty(key, e.getValue().toString());
			}
		}
		logger.info("================ hibernate config ================");
		return hibConfig;
	}
	

	protected SessionFactory buildSessionFactory(LocalSessionFactoryBuilder sfb) {
		/*if(sfb.getInterceptor()==null){
			sfb.setInterceptor(new TimestampInterceptor());
		}*/
		
		sfb.setNamingStrategy(new ImprovedNamingStrategy());
		
		SessionFactory sf = super.buildSessionFactory(sfb);
		SessionFactoryImplementor sfi = (SessionFactoryImplementor) sf;
		EventListenerRegistry reg = sfi.getServiceRegistry().getService(EventListenerRegistry.class);
		
		if(preInsertEventListeners==null){
			List<PreInsertEventListener> preInserts = SpringUtils.getBeans(applicationContext, PreInsertEventListener.class);
			this.preInsertEventListeners = preInserts.toArray(new PreInsertEventListener[0]);
		}
		reg.getEventListenerGroup(EventType.PRE_INSERT).appendListeners(preInsertEventListeners);

		if(preUpdateEventListeners==null){
			List<PreUpdateEventListener> preUpdates = SpringUtils.getBeans(applicationContext, PreUpdateEventListener.class);
			this.preUpdateEventListeners = preUpdates.toArray(new PreUpdateEventListener[0]);
		}
		reg.getEventListenerGroup(EventType.PRE_UPDATE).appendListeners(preUpdateEventListeners);

		if(saveOrUpdateEventListeners==null){
			List<SaveOrUpdateEventListener> preUpdates = SpringUtils.getBeans(applicationContext, SaveOrUpdateEventListener.class);
			this.saveOrUpdateEventListeners = preUpdates.toArray(new SaveOrUpdateEventListener[0]);
		}
		reg.getEventListenerGroup(EventType.SAVE_UPDATE).appendListeners(saveOrUpdateEventListeners);
//		reg.getEventListenerGroup(EventType.SAVE_UPDATE).appendListener(new SaveOrUpdateTimeListener());
		HibernateUtils.initSessionFactory(sf);
		return sf;
	}

	public void setPreInsertEventListeners(PreInsertEventListener[] preInsertEventListeners) {
		this.preInsertEventListeners = preInsertEventListeners;
	}

	public void setPreUpdateEventListeners(PreUpdateEventListener[] preUpdateEventListeners) {
		this.preUpdateEventListeners = preUpdateEventListeners;
	}

	public void setSaveOrUpdateEventListeners(SaveOrUpdateEventListener[] saveOrUpdateEventListeners) {
		this.saveOrUpdateEventListeners = saveOrUpdateEventListeners;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
}
