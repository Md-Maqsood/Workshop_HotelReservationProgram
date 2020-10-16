package com.bridgeLabs.hotelReservation;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HotelReservation {
	private static final Logger logger = LogManager.getLogger(HotelReservation.class);
	private static final Pattern DAY_PATTERN = Pattern.compile("[0-9]{2}[A-Z][a-z]{2}[0-9]{4}");
	private static final List<DayOfWeek> WEEKENDS = Arrays
			.asList(new DayOfWeek[] { DayOfWeek.SUNDAY, DayOfWeek.SATURDAY });

	static Scanner sc = new Scanner(System.in);
	public List<Hotel> hotels;

	public HotelReservation() {
		this.hotels = new ArrayList<Hotel>();
	}

	/**
	 * uc1
	 */
	public void addHotel(String name, int weekdayRate, int weekendRate, int rating) {
		Hotel hotel = new Hotel(name, weekdayRate, weekendRate, rating);
		this.hotels.add(hotel);
	}

	public Customer getCustomerInput() {
		logger.info("Enter the date range in format <date1>, <date2>, <date3>\nEg.: 09Mar2020, 10Mar2020, 11Mar2020");
		String customerInput = sc.nextLine();
		Matcher dateMatcher = DAY_PATTERN.matcher(customerInput);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMMuuuu");
		List<DayOfWeek> daysList = new ArrayList<DayOfWeek>();
		while (dateMatcher.find()) {
			daysList.add(LocalDate.parse(dateMatcher.group(), formatter).getDayOfWeek());
		}
		int numWeekends = (int) daysList.stream().filter(day -> WEEKENDS.contains(day)).count();
		int numWeekdays = daysList.size() - numWeekends;
		return new Customer(numWeekdays, numWeekends);
	}

	public Hotel getCheapestHotel(Customer customer) {
		int numWeekends = customer.getNumWeekends();
		int numWeekdays = customer.getNumWeekdays();
		Map<Hotel, Integer> hotelToTotalRateMap = hotels.stream().collect(Collectors.toMap(hotel -> hotel,
				hotel -> hotel.getWeekendRate() * numWeekends + hotel.getWeekdayRate() * numWeekdays));
		Hotel cheapestHotel = hotelToTotalRateMap.keySet().stream()
				.min((n1, n2) -> hotelToTotalRateMap.get(n1) - hotelToTotalRateMap.get(n2)).orElse(null);
		logger.debug(cheapestHotel.getName() + ", Total Rates: $" + hotelToTotalRateMap.get(cheapestHotel));
		return cheapestHotel;
	}

	public static void main(String[] args) {
		HotelReservation hotelReservation = new HotelReservation();
		hotelReservation.addHotel("Lakewood", 110, 90, 3);
		hotelReservation.addHotel("Bridgewood", 150, 50, 4);
		hotelReservation.addHotel("Ridgewood", 220, 150, 5);
		hotelReservation.hotels.forEach(hotel->logger.debug(hotel));
	}
}

class Customer {
	private int numWeekdays;
	private int numWeekends;

	public Customer(int numWeekdays, int numWeekends) {
		super();
		this.numWeekdays = numWeekdays;
		this.numWeekends = numWeekends;
	}

	public int getNumWeekends() {
		return numWeekends;
	}

	public void setNumWeekends(int numWeekends) {
		this.numWeekends = numWeekends;
	}

	public int getNumWeekdays() {
		return numWeekdays;
	}

	public void setNumWeekdays(int numWeekdays) {
		this.numWeekdays = numWeekdays;
	}

}

class Hotel {
	private String name;
	private int weekdayRate;
	private int weekendRate;
	private int rating;

	public Hotel(String name, int weekdayRate, int weekendRate, int rating) {
		super();
		this.name = name;
		this.weekdayRate = weekdayRate;
		this.weekendRate = weekendRate;
		this.rating=rating;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	public int getWeekendRate() {
		return weekendRate;
	}

	public void setWeekendRate(int weekendRate) {
		this.weekendRate = weekendRate;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getWeekdayRate() {
		return weekdayRate;
	}

	public void setWeekdayRate(int rate) {
		this.weekdayRate = rate;
	}

	@Override
	public String toString() {
		return "Hotel [\nName=" + name + "\nWeekday Rate=$" + weekdayRate + "\nWeekend Rate=$" + weekendRate +"\nRating= " + rating + "\n]";
	}
}
