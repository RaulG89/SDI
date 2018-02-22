package com.uniovi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.uniovi.entities.User;

public interface UsersRepository extends JpaRepository<User, Long> {
	
	public User findByEmail(String email);

}