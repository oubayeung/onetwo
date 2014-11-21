package org.onetwo.plugins.admin;

import org.onetwo.plugins.admin.controller.app.AppUserController;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses=AppUserController.class)
public class AdminAppWebContext {

}
