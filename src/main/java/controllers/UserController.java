package controllers;

import java.util.HashMap;
import java.util.Map;

import org.mindrot.jbcrypt.BCrypt;

import models.User;
import spark.Request;
import spark.Response;
import spark.Route;
import utilities.AutoCloseableDB;
import utilities.MustacheRenderer;

public class UserController {
	
	public static final Route newForm = (Request req, Response res) -> {
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("currentUser", req.session().attribute("currentUser"));
		model.put("noUser", req.session().attribute("currentUser") == null);
		return MustacheRenderer.getInstance().render("session/signup.html", model);
	};
	
	public static final Route create = (Request req, Response res) -> {
		String encryptedPassword = BCrypt.hashpw(req.queryParams("password"), BCrypt.gensalt());
		User user = new User(
				req.queryParams("email"),
				encryptedPassword,
				req.queryParams("first_name"),
				req.queryParams("last_name")
				);
		try (AutoCloseableDB db = new AutoCloseableDB()) {
			user.saveIt();
			req.session().attribute("currentUser", user);
			req.session().attribute("pwMessage", " ");
			res.redirect("/");
			return "";
		}
	};
}
