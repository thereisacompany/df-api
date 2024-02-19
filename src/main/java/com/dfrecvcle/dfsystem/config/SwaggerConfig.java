package com.dfrecvcle.dfsystem.config;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
@EnableOpenApi
@EnableKnife4j
public class SwaggerConfig {

	@Value("${springfox.documentation.swagger-ui.enabled}")
	private boolean mEnabled;
	
	@Bean
	public Docket createRestApi() {
		return new Docket(
				DocumentationType.SWAGGER_2)	// 設置使用規範
				.enable(mEnabled)			// 是否開啟 Swagger
				.apiInfo(apiInfo())			// 配置項目基本信息
				.groupName("df")	// 設置項目組名
				.select()					// 選擇那些路徑和api會生成document
				.apis(RequestHandlerSelectors.any())	// 對所有api進行監控
//				.apis(RequestHandlerSelectors.basePackage("mydlq.swagger.example.controller"))	// 如果需要指定對某個包的接口進行監控，則可以配置如下
				.paths(PathSelectors.any())	// 對所以路徑進行監控
				.paths(PathSelectors.regex("/error.*").negate())	// 忽略以"/error"開頭的路徑，可以防止顯示如404錯誤接口
				.paths(PathSelectors.regex("/actuator.*").negate())	// 忽略以"/actuator"開頭的路徑
				.build();
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
				.title("大豐環保系統")		// 標題
				.description("大豐環保系統")// 描述
				.version("1.0.7")	// 版本
				.license("")	// 設置許可證訊息 Apache LICENSE 2.0
				.licenseUrl("")	// 設置許可證url位址
				.contact(new Contact("RD2", "", "")) // 該api人員的聯絡訊息
				.build();
	}
}
