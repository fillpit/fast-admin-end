package com.kenfei.admin.app.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * swagger配置类
 * @author fei
 * @date 2021/11/4 16:26
 */
@Configuration
@EnableSwagger2
public class Swagger2Config {

  @Value("${app.name}")
  private String title;

  @Bean
  public Docket api() {
    return new Docket(DocumentationType.SWAGGER_2)
      .apiInfo(apiInfo())
      .select()
      // 自行修改为自己的包路径
      .apis(RequestHandlerSelectors.basePackage("com.kenfei.admin.**.controller"))
      .paths(PathSelectors.any())
      .build();
  }

  private ApiInfo apiInfo() {
    return new ApiInfoBuilder()
      .title(title + " 微服务API文档")
//    .description("大鹏教育工作流微服务项目")
      //服务条款网址
//    .termsOfServiceUrl("https://blog.csdn.net/ysk_xh_521")
    .version("1.0")
//    .contact(new Contact("liuxz", "http://liuxz.cn", "1510822551@qq.com"))
      .build();
  }
}
