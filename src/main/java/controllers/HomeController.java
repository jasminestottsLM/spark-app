package controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mindrot.jbcrypt.BCrypt;

import models.Apartment;
import models.User;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.template.velocity.VelocityTemplateEngine;
import utilities.AutoCloseableDB;

public class HomeController {

	// public static final Route index = (Request req, Response res) -> {
	// MustacheRenderer.getInstance();
	public static final Route index2 = (Request req, Response res) -> {
		try (AutoCloseableDB db = new AutoCloseableDB()) {
			List<Apartment> apartments = Apartment.findAll();
			Map<String, Object> model = new HashMap<String, Object>();
			model.put("apartments", apartments);
			model.put("currentUser", req.session().attribute("currentUser"));
			model.put("noUser", req.session().attribute("currentUser") == null);
			model.put("message", req.session().attribute("message"));
			// return MustacheRenderer.getInstance().render("home/index.html", model);
			// // try this last line with a different index template
			return new VelocityTemplateEngine().render(new ModelAndView(model, "/templates/home/index2.vm"));
		}
	};

}