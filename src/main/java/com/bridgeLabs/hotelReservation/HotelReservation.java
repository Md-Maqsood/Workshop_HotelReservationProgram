package com.bridgeLabs.hotelReservation;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HotelReservation {
	private static final Logger logger=LogManager.getLogger(HotelReservation.class);
	static Scanner sc=new Scanner(System.in);
	public List<Hotel> hotels;
	
	public HotelReservation() {
		this.hotels=new ArrayList<Hotel>();
	}
	
	/**
	 * uc1
	 */
	public void addHotel(String name, int rate) {
		Hotel hotel=new Hotel(name, rate);
		this.hotels.add(hotel);
	}
	
	public static void main(String[] args) {
		HotelReservation hotelReservation=new HotelReservation();
		hotelReservation.addHotel("Lakewood", 110);
		hotelReservation.addHotel("Bridgewood", 160);
		hotelReservation.addHotel("Ridgewood", 220);
		hotelReservation.hotels.forEach(n->logger.info(n));
	}
}

class Hotel{
	private String name;
	private int rate;
	public Hotel(String name, int rate) {
		super();
		this.name = name;
		this.rate = rate;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getRate() {
		return rate;
	}
	public void setRate(int rate) {
		this.rate = rate;
	}
	@Override
	public String toString() {
		return "Hotel [\nName=" + name + "\nRate=$" + rate + "\n]";
	}
}
