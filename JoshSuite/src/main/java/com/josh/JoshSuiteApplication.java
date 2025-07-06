package com.josh;

import com.josh.constants.Gender;
import com.josh.constants.Role;
import com.josh.entity.user.User;
import com.josh.repo.user.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

@EnableScheduling
@SpringBootApplication
@EntityScan(basePackages = "com.josh.entity")
public class JoshSuiteApplication implements CommandLineRunner {

	@Autowired
	private UserRepo userRepo;

	public static void main(String[] args) {
		SpringApplication.run(JoshSuiteApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Optional<User> defaultAdmin = userRepo.findByUsername("default_admin");
		if (defaultAdmin.isEmpty() || defaultAdmin == null){
			User defaultAdminUser = User.builder()
					.userId("AD-1000").name("Default Admin")
					.username("default_admin").role(Role.ADMIN)
					.password(new BCryptPasswordEncoder().encode("admin@123")).mobileNumber("9875345681")
					.gender(Gender.MALE).email("defaultAdmin@gmail.com")
					.build();
			userRepo.save(defaultAdminUser);
		}

	}
}
