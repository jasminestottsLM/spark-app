package controllers;

import org.mindrot.jbcrypt.BCrypt;

import models.User;
import spark.Request;
import spark.Response;
import spark.Route;
import utilities.AutoCloseableDB;

public class SessionApiController {
	
	public static final Route login = (Request req, Response res) -> {
		String email = req.queryParams("email");
		String password = req.queryParams("password");
		
		try(AutoCloseableDB db = new AutoCloseableDB()) {
			User user = User.findFirst("email = ?", email);
			if (user != null && BCrypt.checkpw(password, user.getPassword())) {
				req.session().attribute("currentUser", user);
			} else if (user != null) {
				req.session().attribute("message", "Incorrect Password");
			} else {
				req.session().attribute("message", "Username not found");
			}
			res.header("Content-Type", "application/json");
			return user.toJson(true);
		}
	};
}
