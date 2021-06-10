package com.example.AppUsers.dto;

import com.sun.istack.NotNull;

public class ChangePasswordForm {
	
@NotNull
private Long id;
@NotNull
private String currentPassword;

@NotNull
private String newPassword;

@NotNull
private String confirmPassword;

public ChangePasswordForm() { }
public ChangePasswordForm(Long id) {this.id = id;}
public Long getId() {
	return id;
}
public void setId(Long id) {
	this.id = id;
}
public String getCurrentPassword() {
	return currentPassword;
}
public void setCurrentPassword(String currentPassword) {
	this.currentPassword = currentPassword;
}
public String getNewPassword() {
	return newPassword;
}
public void setNewPassword(String newPassword) {
	this.newPassword = newPassword;
}
public String getConfirmPassword() {
	return confirmPassword;
}
public void setConfirmPassword(String confirmPassword) {
	this.confirmPassword = confirmPassword;
}

//Getters & Setters

}
