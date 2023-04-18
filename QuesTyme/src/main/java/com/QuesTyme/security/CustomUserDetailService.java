package com.QuesTyme.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.QuesTyme.exceptions.ResourceNotFoundException;
import com.QuesTyme.repository.UserRepository;



@Service
public class CustomUserDetailService implements UserDetailsService {

	@Autowired
	private UserRepository userRepo;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		UserDetails user = this.userRepo.findByEmail(username);
		if(user==null) {
			new ResourceNotFoundException("User","email : " + username,0);
		}

		return user;
	}

}