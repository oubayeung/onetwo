package org.onetwo.common.web.view.jsp;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.log.MyLoggerFactory;
import org.onetwo.common.spring.SpringApplication;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.web.view.ThemeSetting;
import org.onetwo.common.web.view.ViewPermission;
import org.slf4j.Logger;
import org.springframework.util.Assert;

@SuppressWarnings("serial")
abstract public class AbstractBodyTag extends BodyTagSupport {
	
	public static final String VAR_PRIFEX = "__tag__";
	
	protected final Logger logger = MyLoggerFactory.getLogger(this.getClass());
	
	private ViewPermission viewPermission;
	private ThemeSetting themeSetting;
	
	public AbstractBodyTag(){
		this.viewPermission = SpringApplication.getInstance().getBean(ViewPermission.class, false);
		this.themeSetting = SpringApplication.getInstance().getSpringHighestOrder(ThemeSetting.class);
		Assert.notNull(themeSetting);
//		logger.info(""+this+", viewPermission: {}", viewPermission);
	}
	
	protected ThemeSetting getThemeSetting() {
		return themeSetting;
	}

	protected String getTagVarName(String name){
		return VAR_PRIFEX + name;
	}
	
	protected <T> T getComponentFromRequest(String name, Class<T> clazz){
		return clazz.cast(pageContext.getRequest().getAttribute(getTagVarName(name)));
	}
	
	protected void setComponentIntoRequest(String name, Object val){
		pageContext.getRequest().setAttribute(getTagVarName(name), val);
	}
	
	protected void clearComponentFromRequest(String name){
		pageContext.getRequest().removeAttribute(getTagVarName(name));
	}

	protected void assertParentTag(Class<?> parentTag) throws JspException{
		assertParentTag(parentTag, "tag["+getClass().getSimpleName()+"] must be a child tag of " + parentTag.getSimpleName());
	}
	
	protected void assertParentTag(Class<?> parentTag, String msg) throws JspException{
		if(!parentTag.isInstance(getParent()))
			throw new JspException(msg);
	}
	
	public void write(String content){
		try {
			this.pageContext.getOut().write(content);
		} catch (IOException e) {
			throw new BaseException("write content error: " + e.getMessage(), e);
		}
	}
	
	protected boolean checkPermission(String code){
		if(viewPermission==null)
			return true;
		return viewPermission.hasPermission(code);
	}

	protected ViewPermission getViewPermission() {
		return viewPermission;
	}
	
	/*abstract protected String getTemplate();
	

	protected void renderTemplate() throws JspException{
		renderTemplate(getTemplate());
	}*/
	
	protected void renderTemplate(String template) throws JspException{
		try {
			this.pageContext.include(template);
		} catch (Exception e) {
			JspException jspe = LangUtils.getCauseException(e, JspException.class);
			String msg = e.getMessage();
			if(jspe!=null && jspe.getRootCause()!=null){
				msg = jspe.getRootCause().getCause()==null?msg:jspe.getRootCause().getCause().getMessage();
			}
			throw new JspException("render template["+template+"] error : " + msg, e);
		}
	}
}
