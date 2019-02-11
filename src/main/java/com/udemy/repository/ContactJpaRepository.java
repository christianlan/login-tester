package com.udemy.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.udemy.entity.Contact;

@Repository("contactJpaRepository")
public interface ContactJpaRepository extends JpaRepository<Contact, Serializable>{

	public abstract Contact findById(int id);
}
