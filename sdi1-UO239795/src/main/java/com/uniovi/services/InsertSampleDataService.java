package com.uniovi.services;

import java.text.Normalizer;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uniovi.entities.Request;
import com.uniovi.entities.User;

@Service
public class InsertSampleDataService {
	@Autowired
	private UsersService usersService;

	@Autowired
	private RolesService rolesService;

	private String[] nombres = { "Iván", "Valery", "Ana", "Lorena", "Raúl",
			" María", "Laura", "Cristina", "Marta", "Sara", "Andrea", "Ana",
			"Alba", "Paula", "Sandra", "Nerea", " David", "Alejandro", "Daniel",
			"Javier", "Sergio", "Adrián", "Carlos", "Pablo", "Álvaro", "Pablo",
			"Jorge", "Hugo", "Manuel", "Pedro", "Elena", "Jairo", "Irene",
			"Iris", "Iria", "Miriam", "Miguel", "Luís" };

	private String[] apellidos = { "Aguilar", "Alonso", "Álvarez", "Arias",
			"Benítez", "Blanco", "Blesa", "Bravo", "Caballero", "Cabrera",
			"Calvo", "Cambil", "Campos", "Cano", "Carmona", "Carrasco",
			"Castillo", "Castro", "Cortés", "Crespo", "Cruz", "Delgado", "Díaz",
			"Díez", "Domínguez", "Durán", "Esteban", "Fernández", "Ferrer",
			"Flores", "Fuentes", "Gallardo", "Gallego", "García", "Garrido",
			"Gil", "Giménez", "Gómez", "González", "Guerrero", "Gutiérrez",
			"Hernández", "Herrera", "Herrero", "Hidalgo", "Ibáñez", "Iglesias",
			"Jiménez", "León", "López", "Lorenzo", "Lozano", "Marín", "Márquez",
			"Martín", "Martínez", "Medina", "Méndez", "Molina", "Montero",
			"Montoro", "Mora", "Morales", "Moreno", "Moya", "Muñoz", "Navarro",
			"Nieto", "Núñez", "Ortega", "Ortiz", "Parra", "Pascual", "Pastor",
			"Peña", "Pérez", "Prieto", "Ramírez", "Ramos", "Rey", "Reyes",
			"Rodríguez", "Román", "Romero", "Rubio", "Ruiz", "Sáez", "Sánchez",
			"Santana", "Santiago", "Santos", "Sanz", "Serrano", "Soler", "Soto",
			"Suárez", "Torres", "Vargas", "Vázquez", "Vega", "Velasco",
			"Vicente", "Vidal", "Ortín", "Redondo" };

	private String[] correos = { "gmail.com", "outlook.es", "yahoo.es",
			"hotmail.com", "telecable.es", "uniovi.es" };

	Set<User> users = new HashSet<User>();

	private User user1;

	@PostConstruct
	public void init() {
		inicializar(10);
	}

	private void inicializar(int limite) {
		user1 = new User("ivangonzalezmahagamage@gmail.com", "Iván",
				"González Mahagamage");
		user1.setPassword("123456");
		user1.setRole(rolesService.getAdmin());
		usersService.add(user1);

		User user2 = new User("ivangonzalezmahagamage2@gmail.com", "Iván",
				"González Mahagamage");
		user2.setPassword("123456");
		user2.setRole(rolesService.getAdmin());
		Request a = new Request(user1, user2);
		a.block();
		user2.getReceiveRequests().add(a);
		usersService.add(user2);
		rellenarBaseDatos(limite);

	}

	private void rellenarBaseDatos(int limite) {
		while (users.size() < limite) {
			createUser();
		}

		Iterator<User> a = users.iterator();
		int i = 0;
		while (a.hasNext()) {
			usersService.add(a.next());
			System.out.println(++i);
		}
	}

	private void createUser() {
		String name = nombres[integer(0, nombres.length)];
		String surName1 = apellidos[integer(0, apellidos.length)];
		String surName2 = apellidos[integer(0, apellidos.length)];
		String surName = surName1 + " " + surName2;
		String emailEnd = "@" + correos[integer(0, correos.length)];
		String email = limpiarAcentos(name + surName1 + surName2) + emailEnd;
		User user = new User(email.toLowerCase(), name, surName);
		user.setPassword("123456");
		user.setRole(rolesService.getUser());
		int i = integer(0, 20);
		if (i % 5 == 0) {
			user.getReceiveRequests().add(new Request(user1, user));
		} else if (i % 11 == 0) {
			Request a = new Request(user1, user);
			a.block();
			user.getReceiveRequests().add(a);
		} else if (i % 2 == 0) {
			Request a = new Request(user1, user);
			a.accept();
			user.getReceiveRequests().add(a);
		}
		users.add(user);
	}

	public Integer integer(int min, int max) {
		return (int) (new java.util.Random().nextFloat() * (max - min) + min);
	}

	public String limpiarAcentos(String cadena) {
		String limpio = null;
		if (cadena != null) {
			String valor = cadena;
			valor = valor.toUpperCase();
			// Normalizar texto para eliminar acentos, dieresis, cedillas y
			// tildes
			limpio = Normalizer.normalize(valor, Normalizer.Form.NFD);
			// Quitar caracteres no ASCII excepto la enie, interrogacion que
			// abre, exclamacion que abre, grados, U con dieresis.
			limpio = limpio
					.replaceAll("[^\\p{ASCII}(N\u0303)(n\u0303)(\u00A1)(\u00BF)"
							+ "(\u00B0)(U\u0308)(u\u0308)]", "");
			// Regresar a la forma compuesta, para poder comparar la enie con la
			// tabla de valores
			limpio = Normalizer.normalize(limpio, Normalizer.Form.NFC);
		}
		return limpio;
	}
}