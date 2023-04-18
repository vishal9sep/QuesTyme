package com.QuesTyme.controller;

import java.util.List;

import javax.validation.Valid;

import com.QuesTyme.config.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.QuesTyme.dto.UserDto;
import com.QuesTyme.service.UserService;

@RestController
@RequestMapping("/auth/users")
public class UserController {

	@Autowired
	private UserService userService;

	@PostMapping("/")
	public ResponseEntity<UserDto> CreateUser(@RequestBody UserDto userDto) {
		UserDto createUserDto = this.userService.createUser(userDto);
		return new ResponseEntity<UserDto>(createUserDto, HttpStatus.CREATED);
	}
	
	@PostMapping("/add/student")
	public ResponseEntity<UserDto> registerStudent(@Valid @RequestBody UserDto userDto) {
		
		
		try {
			
			UserDto registerStudent = this.userService.registerStudent(userDto);
			return new ResponseEntity<UserDto>(registerStudent, HttpStatus.CREATED);
			
        } catch (DataIntegrityViolationException ex) {
        	
            if (ex.getCause() instanceof DuplicateKeyException) {
                return new ResponseEntity<UserDto>(HttpStatus.CONFLICT);
            }
            throw ex;
        }

	}
	
	
	@PostMapping("/add/admin")
	public ResponseEntity<UserDto> registerAdmin(@Valid @RequestBody UserDto userDto) {
		UserDto registerStudent = this.userService.registerAdmin(userDto);	
		
		return new ResponseEntity<UserDto>(registerStudent, HttpStatus.CREATED);

	}

	@PostMapping("/bulk-create")
    public ResponseEntity<String> bulkCreateUsers(@RequestBody List<UserDto> userDtos) {
        for(UserDto userDto : userDtos) {
        	UserDto registerStudent = this.userService.registerStudent(userDto);
        }
        return ResponseEntity.ok("Bulk user creation successful");
    }

	@PostMapping("/bulk-create-byCSV")
	public ResponseEntity<String> bulkCreateUsers(@RequestParam("file") MultipartFile file) {

		try {
			userService.bulkCreateUserCSV(file);
			return ResponseEntity.ok("Successfully created Bulk Users by CSV file");

		} catch (Exception e) {
			System.out.println(e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Processing Error in CSV File");
		}
	}

	@GetMapping("/adminlist")
	public ResponseEntity<List<UserDto>> getAdmins() {
		List<UserDto> ans = this.userService.getAdminUser();
		return new ResponseEntity<List<UserDto>>(ans, HttpStatus.OK);
	}
	
//	@GetMapping("/usersByType")
//	public ResponseEntity<List<UserDto>> getUsersByType(@RequestParam("type") Type type) {
//
//		List<UserDto> ans = this.userService.getUsersByType(type);
//		return new ResponseEntity<List<UserDto>>(ans, HttpStatus.OK);
//	}

}
