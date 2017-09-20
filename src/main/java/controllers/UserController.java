package controllers;

import org.mindrot.jbcrypt.BCrypt;

import models.User;
import spark.Request;
import spark.Response;
import spark.Route;
import utilities.AutoCloseableDB;
import utilities.MustacheRenderer;

public class UserController {
	
	public static final Route newForm = (Request req, Response res) -> {
		return MustacheRenderer.getInstance().render("session/signup.html", null);
	};
	
	public static final Route create = (Request req, Response res) -> {
		String encryptedPassword = BCrypt.hashpw(req.queryParams("password"), BCrypt.gensalt());
		User user = new User(
				req.queryParams("firstName"),
				req.queryParams("lastName"), 
				req.queryParams("email"), 
				encryptedPassword);
		try (AutoCloseableDB db = new AutoCloseableDB()) {
			user.saveIt();
			req.session().attribute("currentUser", user);
			req.session().attribute("pwMessage", " ");
			res.redirect("/");
			return "";
		}
	};
}
