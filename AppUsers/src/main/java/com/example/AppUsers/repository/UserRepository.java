package com.example.AppUsers.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.AppUsers.entity.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long>{

}
