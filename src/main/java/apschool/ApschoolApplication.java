package apschool;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.web.bind.annotation.RestController;


@SpringBootApplication
@RestController
public class ApschoolApplication extends SpringBootServletInitializer {
	
	public static void main(String[] args) {
		SpringApplication.run(ApschoolApplication.class, args);
	}
	
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder)
	  {
	    return builder.sources(new Class[] { ApschoolApplication.class });
	  }
	
}
