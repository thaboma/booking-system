package za.co.interview.study.impl.config;


import io.swagger.v3.oas.models.OpenAPI;

import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
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

	@Bean
	public OpenAPI eCertOpenAPI() {
		Contact contact=new Contact();
		contact.setName("Thabo Madikane");
		contact.setEmail("thaboma@gmail.com");

		return new OpenAPI()
				.info(new Info().title("Conference room booking system")
						.description("This project is about reserving a slot in a diary subjects to availability of slots on the current day and also taking into consideration of maintenance slots in between ")
						.version("v1.0.0")
						.contact(contact));
	}

}
