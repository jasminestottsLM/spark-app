package controllers;

import models.User;
import spark.Request;
import spark.Response;
import spark.Route;
import utilities.MustacheRenderer;

public class SessionController {

	public static final Route newForm = (Request req, Response res) -> {
		MustacheRenderer.getInstance();
		return MustacheRenderer.getInstance().render("session/newForm.html", null);
	};
	
	public static final Route create = (Request req, Response res) -> {
		String email = req.queryParams("email");
		String password = req.queryParams("password");
		User user = new User(email, password, "first", "last");
		req.session().attribute("currentUser", user);
		res.redirect("/");
		return "";
	};

}
