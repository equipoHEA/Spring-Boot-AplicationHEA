package com.example.AppUsers.Service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.AppUsers.Exception.CustomeFieldValidationException;
import com.example.AppUsers.Exception.UsernameOrIdNotFound;
import com.example.AppUsers.dto.ChangePasswordForm;
import com.example.AppUsers.entity.User;
import com.example.AppUsers.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository repository;
	
	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Override
	public Iterable<User> getAllUsers() {
		return repository.findAll();
		
	}
	
	private boolean checkUsernameAvailable(User user) throws Exception{
		Optional<User> userFound = repository.findByUsername(user.getUsername());
		if(userFound.isPresent()) {
			throw new CustomeFieldValidationException("Username no disponible","username");
		}
		return true;
	}
	
	private boolean checkPasswordValid(User user) throws Exception{
		if(user.getConfirmPassword()==null || user.getConfirmPassword().isEmpty()) {
			throw new CustomeFieldValidationException("Confirm Password es obligatorio","confirmPassword");
		}
		if(!user.getPassword().equals(user.getConfirmPassword())) {
			throw new CustomeFieldValidationException("Password y confimPassword no coinciden","password");
		}
		return true;
	}

	@Override
	public User createUser(User user) throws Exception {
		if (checkUsernameAvailable(user) && checkPasswordValid(user)) {
			
			String encodePassword = bCryptPasswordEncoder.encode(user.getPassword());
			user.setPassword(encodePassword); 
			
			user = repository.save(user);
		}
		return user;
	}
	
	@Override
	public User getUserById(Long id) throws UsernameOrIdNotFound {
		return repository.findById(id).orElseThrow(() -> new UsernameOrIdNotFound("El Id de usuario no existe"));
	}
	
	@Override
	public User updateUser(User fromUser) throws Exception{
		User toUser = getUserById(fromUser.getId());
		mapUser(fromUser, toUser);
		return repository.save(toUser);
		
	}
	
	protected void mapUser(User from, User to) {
		to.setUsername(from.getUsername());
		to.setFirstName(from.getFirstName());
		to.setLastName(from.getLastName());
		to.setEmail(from.getEmail());
		to.setRoles(from.getRoles());
	}
	
	@Override
	@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	public void deleteUser(Long id) throws UsernameOrIdNotFound {
		User user = getUserById(id);
		
		repository.delete(user);
	}

	@Override
	public User changePassword(ChangePasswordForm form) throws Exception {
		User user = getUserById(form.getId());
		if( !isloggedUserADMIN() && !user.getPassword().equals(form.getCurrentPassword())) {
			throw new Exception ("Password Invalido!");
		}
		
		if(user.getPassword().equals(form.getNewPassword())) {
			throw new Exception ("Nuevo Password debe ser diferente al actual!");
		} 
		
		if( !form.getNewPassword().equals(form.getConfirmPassword())) {
			throw new Exception ("Los Password no coinciden!");
		}
		
		String encodePassword = bCryptPasswordEncoder.encode(form.getNewPassword());
		user.setPassword(encodePassword);
		return repository.save(user);
	}
	
	private boolean isloggedUserADMIN() {
		//Obtener el usuario logeado
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		UserDetails loggedUser = null;
		Object roles = null;

		//Verificar que ese objeto traido de sesion es el usuario
		if (principal instanceof UserDetails) {
			loggedUser = (UserDetails) principal;

			roles = loggedUser.getAuthorities().stream()
					.filter(x -> "ROLE_ADMIN".equals(x.getAuthority())).findFirst()
					.orElse(null); 
		}
		return roles != null ? true : false;
		
	}
	
	public User getloggedUser() throws Exception {
		//Obtener el usuario logeado
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		UserDetails loggedUser = null;

		//Verificar que ese objeto traido de sesion es el usuario
		if (principal instanceof UserDetails) {
			loggedUser = (UserDetails) principal;
		}
		
		User myUser = repository
				.findByUsername(loggedUser.getUsername()).orElseThrow(() -> new Exception("Problamas de sesion"));
		
		return myUser;
	}
	
}
