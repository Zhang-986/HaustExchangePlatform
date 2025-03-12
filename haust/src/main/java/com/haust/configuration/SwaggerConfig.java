package com.haust.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
@EnableSwagger2
@Configuration
public class SwaggerConfig implements WebMvcConfigurer {
    @Bean
    public Docket user(){
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("用户端")
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.haust.controller.user"))
                .paths(PathSelectors.any())
                .build();
    }
    @Bean
    public Docket admin(){
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("管理端")
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.haust.controller.admin"))
                .paths(PathSelectors.any())
                .build();
    }
    @Bean
    public Docket all(){
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("通用接口")
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.haust.controller.all"))
                .paths(PathSelectors.any())
                .build();
    }
    private ApiInfo apiInfo(){
        ApiInfo apiInfo = new ApiInfoBuilder()
                .title("Haust共享项目接口文档")
                .version("1.0")
                .description("Haust共享项目接口文档，包含用户端和管理端的所有接口")
                .termsOfServiceUrl("https://gitcode.com/qq_43023041/HAUST_Internal_referral_code_sharing_platform")
                .contact(contact())
                .license("Apache 2.0")
                .licenseUrl("https://www.apache.org/licenses/LICENSE-2.0")
                .build();
        return apiInfo;
    }
    private Contact contact(){
        return new Contact("Hauster","haust.edu.cn","web@haust.edu.cn");
    }
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("doc.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }




}
