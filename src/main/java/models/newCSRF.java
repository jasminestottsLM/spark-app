package models;

import java.util.UUID;

public class newCSRF {

	private static UUID newCSRF() {
		UUID newCSRF = UUID.randomUUID();
		return newCSRF;
	}
	
	public static UUID getNewCSRF() {
		return newCSRF();
	}
}
