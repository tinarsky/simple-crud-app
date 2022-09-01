package com.github.tinarsky.springmvc.models;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class Person {
	@NotEmpty(message = "Name should be not empty!")
	@Size(min = 2, max = 30, message = "Name length should be between 2 and 3!")
	private String name;
	@Min(value = 0, message = "Age should be greater than zero!")
	private int age;
	@NotEmpty(message = "Email should be not empty!")
	@Email(message = "Incorrect email!")
	private String email;
	private int id;

	public Person() {
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
