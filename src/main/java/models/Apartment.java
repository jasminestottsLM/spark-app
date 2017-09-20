package models;

import java.util.ArrayList;
import java.util.List;

import org.javalite.activejdbc.Model;

public class Apartment extends Model {

	public Apartment() {
	}

	public Apartment(int rent, int numberOfBR, double numberOfBA, int squareFootage, String address, String city,
			String state, String zip) {
		setRent(rent);
		setNumberOfBR(numberOfBR);
		setNumberOfBA(numberOfBA);
		setSquareFootage(squareFootage);
		setAddress(address);
		setCity(city);
		setState(state);
		setZip(zip);
	}

	public double getNumberOfBR() {
		return getInteger("number_of_bedrooms");
	}

	public void setNumberOfBR(int numberOfBR) {
		set("number_of_bedrooms", numberOfBR);
	}

	public double getNumberOfBA() {
		return getDouble("number_of_bathrooms");
	}

	public void setNumberOfBA(double numberOfBA) {
		set("number_of_bathrooms", numberOfBA);
	}

	public int getSquareFootage() {
		return getInteger("square_footage");
	}

	public void setSquareFootage(int squareFootage) {
		set("square_footage", squareFootage);
	}

	public String getAddress() {
		return getString("address");
	}

	public void setAddress(String address) {
		set("address", address);
	}

	public String getCity() {
		return getString("city");
	}

	public void setCity(String city) {
		set("city", city);
	}

	public String getState() {
		return getString("state");
	}

	public void setState(String state) {
		set("state", state);
	}

	public String getZip() {
		return getString("zip_code");
	}

	public void setZip(String zip) {
		set("zip_code", zip);
	}

	public int getRent() {
		return getInteger("rent");
	}

	public void setRent(int rent) {
		set("rent", rent);
	}
}
