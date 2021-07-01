package com.example.AppUsers.Service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.AppUsers.entity.Role;
import com.example.AppUsers.repository.UserRepository;

@Service
@Transactional
public class UserDetailsServiceImpl implements UserDetailsService{

	@Autowired
    UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		
		com.example.AppUsers.entity.User appUser = 
	                 userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Usuario no existe!"));
		
	    Set grantList = new HashSet(); 
	    
	    for (Role role: appUser.getRoles()) {
	        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(role.getDescription());
	            grantList.add(grantedAuthority);
	    }
			
	    UserDetails user = (UserDetails) new User(appUser.getUsername(), appUser.getPassword(), grantList);
	    return user;
			
	}

}
