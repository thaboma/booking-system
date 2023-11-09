package za.co.interview.study.impl.config;

import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfiguration {

	@Bean
	public GroupedOpenApi api() {
		return GroupedOpenApi
				.builder()
				.group("Case study test project")
				.packagesToScan("za.co.interview.study.impl.controller")
				.packagesToExclude("org.springframework.boot")
				.build();
	}
}
