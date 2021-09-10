package com.ecom;

import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.convert.Jsr310Converters;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.ecom.app.exceptions.AppException;
import com.ecom.app.security.JwtAuthenticationFilter;
import com.ecom.category.model.Category;
import com.ecom.category.repository.CategoryRepository;
import com.ecom.user.model.Role;
import com.ecom.user.model.RoleName;
import com.ecom.user.model.User;
import com.ecom.user.repository.RoleRepository;
import com.ecom.user.repository.UserRepository;

@SpringBootApplication
@EntityScan(basePackageClasses = { EcommApiApplication.class, Jsr310Converters.class })
public class EcommApiApplication {

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	private static final String USER_ROLE_NOT_SET = "User role not set";

	public static void main(String[] args) {
		SpringApplication.run(EcommApiApplication.class, args);
	}

	@PostConstruct
	void init() {
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
	}

	@Bean
	public JwtAuthenticationFilter jwtAuthenticationFilter() {
		return new JwtAuthenticationFilter();
	}

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	@Bean
	InitializingBean sendDatabase() {
		return () -> {
			if (roleRepository.count() == 0) {
				for (RoleName roleName : RoleName.values()) {
					Role role = new Role();
					role.setName(roleName);
					roleRepository.save(role);
				}
			}

			if (userRepository.count() == 0) {
				BCryptPasswordEncoder bcpe = new BCryptPasswordEncoder();
				User user = new User("Super", "Admin", "admin", "admin@gmail.com", bcpe.encode("admin"));
				List<Role> roles = new ArrayList<>();
				roles.add(roleRepository.findByName(RoleName.ROLE_USER)
						.orElseThrow(() -> new AppException(USER_ROLE_NOT_SET)));
				roles.add(roleRepository.findByName(RoleName.ROLE_ADMIN)
						.orElseThrow(() -> new AppException(USER_ROLE_NOT_SET)));
				roles.add(roleRepository.findByName(RoleName.ROLE_VENDOR)
						.orElseThrow(() -> new AppException(USER_ROLE_NOT_SET)));
				user.setRoles(roles);
				userRepository.save(user);
			}

			if (categoryRepository.count() == 0) {
				categoryRepository.save(new Category(null, "cat1"));
			}

		};
	}

}
