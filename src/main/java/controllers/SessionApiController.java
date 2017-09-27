package controllers;

import java.util.Map;

import org.javalite.common.JsonHelper;
import org.mindrot.jbcrypt.BCrypt;

import models.User;
import spark.Request;
import spark.Response;
import spark.Route;
import utilities.AutoCloseableDB;

public class SessionApiController {
	
	public static final Route login = (Request req, Response res) -> {
		String json = req.body();
		Map map = JsonHelper.toMap(json);
		User user = new User();
		user.fromMap(map);
		String email = user.getEmail();
		System.out.println(email);
		String password = user.getPassword();
		
		
		try (AutoCloseableDB db = new AutoCloseableDB()) {
			User currentUser = User.findFirst("email = ?", email);
			
			if (currentUser != null && BCrypt.checkpw(password, currentUser.getPassword()) 
					// && testCSRF.equals(receivedCSRF)
					) {
				req.session().attribute("currentUser", currentUser);
				System.out.println(currentUser);
				return currentUser.toJson(true);
			} else if (currentUser != null) {
				req.session().attribute("message", "Incorrect Password");
				return null;
			} else {
				req.session().attribute("message", "Username not found");
				return null;
			}
		}
		
		
		
	};
}
