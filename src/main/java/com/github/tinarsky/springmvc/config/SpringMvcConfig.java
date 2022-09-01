package com.github.tinarsky.springmvc.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import javax.sql.DataSource;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

@Configuration
@ComponentScan("com.github.tinarsky.springmvc")
@EnableWebMvc
public class SpringMvcConfig
		implements WebMvcConfigurer {
	private final ApplicationContext applicationContext;

	@Autowired
	public SpringMvcConfig(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	@Bean
	public SpringResourceTemplateResolver templateResolver(){
		var resolver = new SpringResourceTemplateResolver();
		resolver.setApplicationContext(applicationContext);
		resolver.setPrefix("/WEB-INF/views/");
		resolver.setSuffix(".html");
		return resolver;
	}

	@Bean
	public SpringTemplateEngine templateEngine(){
		var templateEngine = new SpringTemplateEngine();
		templateEngine.setTemplateResolver(templateResolver());
		templateEngine.setEnableSpringELCompiler(true);
		return templateEngine;
	}

	@Override
	public void configureViewResolvers(ViewResolverRegistry registry) {
		var viewResolver = new ThymeleafViewResolver();
		viewResolver.setTemplateEngine(templateEngine());
		registry.viewResolver(viewResolver);
	}

	@Bean
	public DataSource dataSource(){
		var dataSource = new DriverManagerDataSource();

		var properties = new Properties();
		String resourcePath = Thread.currentThread()
				.getContextClassLoader()
				.getResource("database.properties").getPath();
		try {
			properties.load(new FileReader(resourcePath));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		dataSource.setDriverClassName(properties.getProperty("driver.class.name"));
		dataSource.setUrl(properties.getProperty("url"));
		dataSource.setUsername(properties.getProperty("username"));
		dataSource.setPassword(properties.getProperty("password"));

		return dataSource;
	}

	@Bean
	public JdbcTemplate jdbcTemplate(){
		return new JdbcTemplate(dataSource());
	}
}
