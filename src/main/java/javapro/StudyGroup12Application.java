package javapro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import javax.annotation.PostConstruct;
import java.util.TimeZone;

@SpringBootApplication
public class StudyGroup12Application {

	public static void main(String[] args) {
		SpringApplication.run(StudyGroup12Application.class, args);
	}

	@PostConstruct
	public void init(){
		TimeZone.setDefault(TimeZone.getTimeZone("Europe/Moscow"));
	}
}
