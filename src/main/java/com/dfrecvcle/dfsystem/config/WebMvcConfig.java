package com.dfrecvcle.dfsystem.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer{

	@Value("${file-save-path}")
	private String fileSavePath;

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/swagger-ui/**")
				.addResourceLocations("classpath:/META-INF/resources/webjars/springfox-swagger-ui/");
		
//		registry.addResourceHandler("doc.html").addResourceLocations("classpath:/META-INF/resources/");
//		registry.addResourceHandler("/img/**").addResourceLocations("classpath:/META-INF/resources/img/");
		registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
//		registry.addResourceHandler("/images/**").addResourceLocations("file:"+fileSavePath);
	}

}
