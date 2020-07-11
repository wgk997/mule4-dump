package com.mulesoft.training;

import java.util.ArrayList;

public class FlightArray {
	
	private ArrayList<Flight> flights;

	public FlightArray() {
		// TODO Auto-generated constructor stub
	}
	
	public FlightArray(ArrayList<Flight> flights) {
		super();
		this.flights = flights;
	}

	public ArrayList<Flight> getFlights() {
		return flights;
	}

	public void setFlights(ArrayList<Flight> flights) {
		this.flights = flights;
	}



}
