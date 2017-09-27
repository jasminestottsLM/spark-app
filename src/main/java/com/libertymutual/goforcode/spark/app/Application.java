package com.libertymutual.goforcode.spark.app;

import static spark.Spark.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.javalite.activejdbc.Base;
import org.mindrot.jbcrypt.BCrypt;

import controllers.ApartmentApiController;
import controllers.ApartmentController;
import controllers.SessionController;
import controllers.UserApiController;
import controllers.UserController;
import filters.SecurityFilters;
import models.Apartment;
import models.User;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.template.velocity.VelocityTemplateEngine;
import utilities.AutoCloseableDB;
import controllers.HomeController;
import controllers.SessionApiController;

public class Application {

	public static void main(String[] args) {

		String encryptedPassword = BCrypt.hashpw("password", BCrypt.gensalt());

		try (AutoCloseableDB db = new AutoCloseableDB()) {
			// opens a connection to the database
			User.deleteAll();
			
			User testUser = new User("email@email.net", encryptedPassword, "John", "Doe");
			testUser.saveIt();
			Object testId = testUser.getId();

			User testUser2 = new User("second@email.net", encryptedPassword, "Jane", "Doe");
			testUser2.saveIt();


			Apartment.deleteAll();
			Apartment a = new Apartment(4000, 1, 1, 350, "123 Main St.", "San Francisco", "CA", "95125");
			a.set("is_active", true);
			testUser.add(a);
			a.saveIt();
			
			Apartment b = new Apartment(4000, 5, 6, 4000, "123 Cowboy Way", "Houston", "TX", "77006");
			b.set("is_active", false);
			testUser.add(b);
			b.saveIt();			
			
			Apartment c = new Apartment(2000, 2, 2, 1000, "456 Main St.", "Random", "CO", "11111");
			c.set("is_active", true);
			testUser.add(c);
			c.saveIt();	
		}
		
		enableCORS("http://localhost:4200", "*", "*");
		
		// closes the connection to the database
		// get("/", HomeController.index);
		
		get("/", HomeController.index2);
		// get("/", (req, res) -> {
		// Map<String, Object> model = new HashMap<>();
		//
		// return new VelocityTemplateEngine().render(new ModelAndView(model,
		// "/templates/home/index2.vm"));
		// });
		get("/login", SessionController.newForm);
		post("/login", SessionController.create);
		post("/logout", SessionController.destroy);
		
		get("/users/new", UserController.newForm);
		post("/users/new", UserController.create);
		

		path("/apartments", () -> {
			before("/new", SecurityFilters.isAuthenticated);
			get("/new", ApartmentController.newForm);
			
			before("/mine", SecurityFilters.isAuthenticated);
			get("/mine", ApartmentController.index);
			
			
			get("/:id", ApartmentController.details);
				// goes after specific mappings
			
			before("/:id/likes", SecurityFilters.isAuthenticated);
			post("/:id/likes", ApartmentController.likes);
			
			before("/:id/activations", SecurityFilters.isAuthenticated);
			//also need user to == listing user
			post("/:id/activations", ApartmentController.activations);
			
			before("/:id/deactivations", SecurityFilters.isAuthenticated);
			//also need user to == listing user
			post("/:id/deactivations", ApartmentController.deactivations);
			
			before("", SecurityFilters.isAuthenticated);
			post("", ApartmentController.create);
		});
		
		path("/api", () -> {
			post("/sessions", SessionApiController.login);
			get("/apartments/:id", ApartmentApiController.details);
			post("/apartments", ApartmentApiController.create);
			get("/apartments", ApartmentApiController.index);
			get("/users/:id", UserApiController.details);
			post("/users", UserApiController.create);
		});
	}
	
	private static void enableCORS(final String origin, final String methods, final String headers) {

	    options("/*", (request, response) -> {

	        String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
	        if (accessControlRequestHeaders != null) {
	            response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
	        }

	        String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
	        if (accessControlRequestMethod != null) {
	            response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
	        }

	        return "OK";
	    });

	    before((request, response) -> {
	        response.header("Access-Control-Allow-Origin", origin);
	        response.header("Access-Control-Request-Method", methods);
	        response.header("Access-Control-Allow-Headers", headers);
	        response.header("Access-Control-Allow-Credentials", "true");
	        // Note: this may or may not be necessary in your particular application
//	        response.type("application/json");
	    });
	}
}
