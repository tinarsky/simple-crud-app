package com.github.tinarsky.springmvc.controllers;

import com.github.tinarsky.springmvc.dao.PersonDAO;
import com.github.tinarsky.springmvc.models.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("/people")
public class PeopleController {
	private final PersonDAO personDAO;

	@Autowired
	public PeopleController(PersonDAO personDAO) {
		this.personDAO = personDAO;
	}

	@GetMapping()
	public String index(Model model) {
		model.addAttribute("people", personDAO.index());
		return "people/index";
	}

	@GetMapping("{id}")
	public String show(@PathVariable("id") int id, Model model,
					   HttpServletResponse response) {
		Optional<Person> optionalPerson = personDAO.show(id);
		if (optionalPerson.isEmpty()) {
			response.setStatus(404);
			return "people/notfound";
		}
		model.addAttribute("person", optionalPerson.get());
		return "people/show";
	}

	@GetMapping("/new")
	public String newPerson(@ModelAttribute("person") Person person) {
		return "people/new";
	}

	@PostMapping()
	public String create(@ModelAttribute("person") @Valid Person person,
						 BindingResult bindingResult) {
		if(bindingResult.hasErrors())
			return "people/new";

		personDAO.add(person);
		return "redirect:/people";
	}

	@GetMapping("/{id}/edit")
	public String edit(@PathVariable("id") int id, Model model,
					   HttpServletResponse response) {
		Optional<Person> optionalPerson = personDAO.show(id);
		if (optionalPerson.isEmpty()) {
			response.setStatus(404);
			return "people/notfound";
		}
		model.addAttribute("person", optionalPerson.get());
		return "people/edit";
	}

	@PatchMapping("/{id}")
	public String update(@PathVariable("id") int id,
						 @ModelAttribute("person") @Valid Person person,
						 BindingResult bindingResult,
						 HttpServletResponse response) {
		if(bindingResult.hasErrors())
			return "people/edit";

		if (!personDAO.existsPersonWithId(id)) {
			response.setStatus(404);
			return "people/notfound";
		}
		personDAO.update(id, person);
		return "redirect:/people";
	}

	@DeleteMapping("/{id}")
	public String delete(@PathVariable("id") int id,
						 HttpServletResponse response) {
		if (!personDAO.existsPersonWithId(id)) {
			response.setStatus(404);
			return "people/notfound";
		}
		personDAO.delete(id);
		return "redirect:/people";
	}
}
