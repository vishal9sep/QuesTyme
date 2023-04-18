package com.QuesTyme.service;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.QuesTyme.config.AppConstants;
import com.QuesTyme.dto.UserDto;
import com.QuesTyme.entity.Role;
import com.QuesTyme.entity.User;
import com.QuesTyme.exceptions.ResourceNotFoundException;
import com.QuesTyme.repository.RoleRepo;
import com.QuesTyme.repository.UserRepository;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

@Service
public class UserService {

	public UserService(UserRepository userRepo) {
		this.userRepo = userRepo;
	}

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private RoleRepo roleRepo;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public UserDto registerStudent(UserDto userDto) {

		User user = this.modelMapper.map(userDto, User.class);

		user.setPassword(this.passwordEncoder.encode(user.getPassword()));
		Role role = this.roleRepo.findById(AppConstants.STUDENT_USER).get();
		user.getRoles().add(role);

		User newUser = this.userRepo.save(user);

		return this.modelMapper.map(newUser, UserDto.class);
	}

	public UserDto registerAdmin(UserDto userDto) {

		User user = this.modelMapper.map(userDto, User.class);
		user.setPassword(this.passwordEncoder.encode(user.getPassword()));
		Role role = this.roleRepo.findById(AppConstants.ADMIN_USER).get();
		user.getRoles().add(role);

		User newUser = this.userRepo.save(user);

		return this.modelMapper.map(newUser, UserDto.class);
	}

	public UserDto createUser(UserDto userDto) {

		User user = this.modelMapper.map(userDto, User.class);
		User savedUser = this.userRepo.save(user);
		return this.modelMapper.map(savedUser, UserDto.class);
	}

	public Map<Integer, User> userMap() {

		Map<Integer, User> map = new HashMap<>();
		userRepo.findAll().forEach(c -> map.put(c.getId(), c));
		return map;
	}

	public List<UserDto> getAdminUser() {

		List<User> users = this.userRepo.findAll();
		Role role = this.roleRepo.findById(AppConstants.ADMIN_USER).get();
		List<User> adminUsers = new ArrayList<User>();
		for (int i = 0; i < users.size(); i++) {
			if (users.get(i).getRoles().contains(role)) {
				adminUsers.add(users.get(i));
			}

		}

		List<UserDto> UserDtos = adminUsers.stream().map(user -> this.modelMapper.map(user, UserDto.class))
				.collect(Collectors.toList());
		return UserDtos;

	}

	public User getUserById(Integer userId) {
		User user = userRepo.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User", "User id", userId));
		return user;
	}

	public void bulkCreateUserCSV(MultipartFile file) throws IOException, CsvException {

		CSVReader csvReader = new CSVReader(new InputStreamReader(file.getInputStream()));

		List<String[]> rows = csvReader.readAll();
		csvReader.close();

		List<User> usersList = new ArrayList<>();
		int count = 0;
		for (int i = 1; i < rows.size(); i++) { // i=1 ,because we are skipping header row;

			String[] row = rows.get(i);

			User userCheck = userRepo.findByEmail(row[2]);
			if (userCheck != null) {
				count++;
				continue;
			}

			User user = new User();
			user.setName(row[1]);
			user.setEmail(row[2]);
			user.setPassword(this.passwordEncoder.encode(row[3]));
			Role role = this.roleRepo.findById(AppConstants.STUDENT_USER).get();
			user.getRoles().add(role);

			usersList.add(user);
		}
		System.out.println(count);
		userRepo.saveAll(usersList);

	}

//	public List<UserDto> getUsersByType(Type type) {
//
//		List<User> users = userRepo.findByType(type);
//
//		List<UserDto> UserDtos = users.stream().map(user -> this.modelMapper.map(user, UserDto.class))
//				.collect(Collectors.toList());
//
//		return UserDtos;
//	}

}
