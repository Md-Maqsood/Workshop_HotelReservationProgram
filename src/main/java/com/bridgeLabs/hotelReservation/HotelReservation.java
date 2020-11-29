package com.bridgeLabs.hotelReservation;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
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
	private static final String VALID_INPUT_PATTERN = "^((Regular)|(Rewards)){1}[:]{1}([0-9]{2}[A-Z][a-z]{2}[0-9]{4}[,][ ])*([0-9]{2}[A-Z][a-z]{2}[0-9]{4}){1}$";
	private static final Pattern DAY_PATTERN = Pattern.compile("[0-9]{2}[A-Z][a-z]{2}[0-9]{4}");
	private static final Pattern CUSTOMER_TYPE_PATTERN = Pattern.compile("Re[gw][ua][lr][ad][rs]");
	private static final List<DayOfWeek> WEEKENDS = Arrays
			.asList(new DayOfWeek[] { DayOfWeek.SUNDAY, DayOfWeek.SATURDAY });

	static Scanner sc = new Scanner(System.in);
	public List<Hotel> hotels;

	public HotelReservation() {
		this.hotels = new LinkedList<Hotel>();
	}

	public void addHotel(String name, int regularWeekdayRate, int regularWeekendRate, int rewardsWeekdayRate,
			int rewardsWeekendRate, int rating) {
		Hotel hotel = new Hotel(name, regularWeekdayRate, regularWeekendRate, rewardsWeekdayRate, rewardsWeekendRate,
				rating);
		this.hotels.add(hotel);
	}

	public Customer getCustomerInput() throws InvalidInputException {
		logger.info(
				"Enter the customer_type and date range in format <customer_type>:<date1>, <date2>, <date3>\nEg. Rewards:09Mar2020, 10Mar2020, 11Mar2020");
		String customerInput = sc.nextLine();
		if (!customerInput.matches(VALID_INPUT_PATTERN)) {
			throw new InvalidInputException("Invalid input. Try again by giving input in the right format mentioned.");
		}
		Matcher customerTypeMatcher = CUSTOMER_TYPE_PATTERN.matcher(customerInput);
		CustomerType customerType = CustomerType.REGULAR;
		if (customerTypeMatcher.find()) {
			if (customerTypeMatcher.group().equals("Rewards")) {
				customerType = CustomerType.REWARDS;
			}
		}
		Matcher dateMatcher = DAY_PATTERN.matcher(customerInput);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMMuuuu");
		List<DayOfWeek> daysList = new ArrayList<DayOfWeek>();
		while (dateMatcher.find()) {
			daysList.add(LocalDate.parse(dateMatcher.group(), formatter).getDayOfWeek());
		}
		int numWeekends = (int) daysList.stream().filter(day -> WEEKENDS.contains(day)).count();
		int numWeekdays = daysList.size() - numWeekends;
		return new Customer(numWeekdays, numWeekends, customerType);
	}

	public Hotel getCheapestBestRatedHotelForRewards(Customer customer) {
		int numWeekdays = customer.getNumWeekdays();
		int numWeekends = customer.getNumWeekends();
		Map<Hotel, Integer> hotelToTotalRateMap = hotels.stream().collect(Collectors.toMap(hotel -> hotel,
				hotel -> hotel.getRewardsWeekendRate() * numWeekends + hotel.getRewardsWeekdayRate() * numWeekdays));
		Hotel cheapestBestRatedHotel = hotelToTotalRateMap.keySet().stream().min((hotel1, hotel2) -> {
			int rateDifference = hotelToTotalRateMap.get(hotel1) - hotelToTotalRateMap.get(hotel2);
			int ratingDifference = hotel1.getRating() - hotel2.getRating();
			return rateDifference == 0 ? -(ratingDifference) : rateDifference;
		}).orElse(null);
		try {
			logger.info(cheapestBestRatedHotel.getName() + ", Rating: " + cheapestBestRatedHotel.getRating()
					+ " and Total Rates: $" + hotelToTotalRateMap.get(cheapestBestRatedHotel));
		} catch (NullPointerException e) {
			logger.info("No hotel found");
		}
		return cheapestBestRatedHotel;
	}

	public Hotel getCheapestBestRatedHotelForRegular(Customer customer) {
		int numWeekends = customer.getNumWeekends();
		int numWeekdays = customer.getNumWeekdays();
		Map<Hotel, Integer> hotelToTotalRateMap = hotels.stream().collect(Collectors.toMap(hotel -> hotel,
				hotel -> hotel.getRegularWeekendRate() * numWeekends + hotel.getRegularWeekdayRate() * numWeekdays));
		Hotel cheapestBestRatedHotel = hotelToTotalRateMap.keySet().stream().min((hotel1, hotel2) -> {
			int rateDifference = hotelToTotalRateMap.get(hotel1) - hotelToTotalRateMap.get(hotel2);
			int ratingDifference = hotel1.getRating() - hotel2.getRating();
			return rateDifference == 0 ? -(ratingDifference) : rateDifference;
		}).orElse(null);
		try {
			logger.info(cheapestBestRatedHotel.getName() + ", Rating: " + cheapestBestRatedHotel.getRating()
					+ " and Total Rates: $" + hotelToTotalRateMap.get(cheapestBestRatedHotel));
		} catch (NullPointerException e) {
			logger.info("No hotel found");
		}
		return cheapestBestRatedHotel;
	}

	public Hotel getBestRatedHotel(Customer customer) {
		int numWeekends = customer.getNumWeekends();
		int numWeekdays = customer.getNumWeekdays();
		Hotel bestRatedHotel = hotels.stream().max((hotel1, hotel2) -> hotel1.getRating() - hotel2.getRating())
				.orElse(null);
		try {
			int totalRate = bestRatedHotel.getRegularWeekdayRate() * numWeekdays
					+ bestRatedHotel.getRegularWeekendRate() * numWeekends;
			logger.info(bestRatedHotel.getName() + " & Total Rates $" + totalRate);
		} catch (NullPointerException e) {
			logger.info("No hotel found");
		}
		return bestRatedHotel;
	}

	public static void main(String[] args) {
		HotelReservation hotelReservation = new HotelReservation();
		hotelReservation.addHotel("Lakewood", 110, 90, 80, 80, 3);
		hotelReservation.addHotel("Bridgewood", 150, 50, 110, 50, 4);
		hotelReservation.addHotel("Ridgewood", 220, 150, 100, 40, 5);
		try {
			Customer customer = hotelReservation.getCustomerInput();
			CustomerType customerType = customer.getCustomerType();
			if (customerType == CustomerType.REGULAR) {
				hotelReservation.getCheapestBestRatedHotelForRegular(customer);
			} else {
				hotelReservation.getCheapestBestRatedHotelForRewards(customer);
			}
		} catch (InvalidInputException e) {
			logger.info(e.getMessage());
		}
	}
}



class InvalidInputException extends Exception {
	public InvalidInputException(String message) {
		super(message);
	}
}

enum CustomerType {
	REGULAR, REWARDS
}


