package com.QuesTyme;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.task.TaskSchedulingAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.QuesTyme.config.AppConstants;
import com.QuesTyme.entity.Role;
import com.QuesTyme.repository.RoleRepo;


@SpringBootApplication(exclude = TaskSchedulingAutoConfiguration.class)
@EnableScheduling
public class QuesTymeApplication  implements CommandLineRunner{

	@Autowired
	private RoleRepo roleRepo;
	
	public static void main(String[] args) {
		SpringApplication.run(QuesTymeApplication.class, args);
	}
	
	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	@Override
	public void run(String... args) throws Exception {
		try {
			Role role = new Role();
			role.setId(AppConstants.ADMIN_USER);
			role.setName("ROLE_ADMIN");

			Role role1 = new Role();
			role1.setId(AppConstants.STUDENT_USER);
			role1.setName("ROLE_STUDENT");

			ArrayList<Role> roles = new ArrayList<Role>();
			roles.add(role);
			roles.add(role1);

			List<Role> result = this.roleRepo.saveAll(roles);
				
			
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}

}
