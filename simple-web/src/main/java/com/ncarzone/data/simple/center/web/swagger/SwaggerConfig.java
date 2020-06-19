package com.ncarzone.data.simple.center.web.swagger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"com.ncarzone.data.simple.center.web"})
public class SwaggerConfig {

  Logger logger = LoggerFactory.getLogger(SwaggerConfig.class);

  @Value("${openSwagger:true}")
  boolean openSwagger;

  @Bean
  public Docket createRestApi() {
    logger.info("SpringFox-ui is started");
    if (openSwagger) {
      logger.info("SpringFox-scan is started");
    }
    return new Docket(DocumentationType.SWAGGER_2).enable(openSwagger).apiInfo(apiInfo()).select()
        // 只有带有注解的类才会生成文档
        .apis(RequestHandlerSelectors.withClassAnnotation(ApiScan.class)).paths(PathSelectors.any()).build();
  }

  private ApiInfo apiInfo() {
    return new ApiInfoBuilder().title("APIs").version("1.0").build();
  }

}
