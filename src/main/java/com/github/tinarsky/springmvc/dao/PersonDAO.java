package com.github.tinarsky.springmvc.dao;

import com.github.tinarsky.springmvc.models.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class PersonDAO {
	private final JdbcTemplate jdbcTemplate;

	@Autowired
	public PersonDAO(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public List<Person> index() {
		return jdbcTemplate.query("SELECT * FROM person ORDER BY id",
				new BeanPropertyRowMapper<>(Person.class));
	}

	public Optional<Person> show(int id) {
		return jdbcTemplate.query("SELECT * FROM person WHERE id=?",
						new BeanPropertyRowMapper<>(Person.class), id)
				.stream().findAny();
	}

	public void add(Person person) {
		jdbcTemplate.update(
				"INSERT INTO person (name, age, email) VALUES (?, ?, ?)",
				person.getName(), person.getAge(), person.getEmail());
	}

	public boolean existsPersonWithId(int id) {
		return show(id).isPresent();
	}

	public void update(int id, Person updatedPerson) {
		jdbcTemplate.update(
				"UPDATE person SET name=?, age=?, email=? WHERE id=?",
				updatedPerson.getName(), updatedPerson.getAge(),
				updatedPerson.getEmail(), id);
	}

	public void delete(int id) {
		jdbcTemplate.update("DELETE FROM person WHERE id=?", id);
	}
}
