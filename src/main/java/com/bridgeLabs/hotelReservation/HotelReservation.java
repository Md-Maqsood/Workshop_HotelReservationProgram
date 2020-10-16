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
	private static final String VALID_INPUT_PATTERN = "^((Regular)|(Rewards)){1}[:]{1}([0-9]{2}[A-Z][a-z]{2}[0-9]{4}[,][ ])*([0-9]{2}[A-Z][a-z]{2}[0-9]{4}){1}$";
	private static final Pattern DAY_PATTERN = Pattern.compile("[0-9]{2}[A-Z][a-z]{2}[0-9]{4}");
	private static final Pattern CUSTOMER_TYPE_PATTERN = Pattern.compile("Re[gw][ua][lr][ad][rs]");
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
		Hotel cheapestBestRatedHotel = null;
		Integer minTotalRate = null;
		for (Hotel hotel : hotels) {
			Integer totalRate = hotel.getRewardsWeekdayRate() * numWeekdays
					+ hotel.getRewardsWeekendRate() * numWeekends;
			try {
				if (minTotalRate.compareTo(totalRate) > 0) {
					cheapestBestRatedHotel = hotel;
					minTotalRate = totalRate;
				} else if (minTotalRate.compareTo(totalRate) == 0) {
					if (hotel.getRating() > cheapestBestRatedHotel.getRating()) {
						cheapestBestRatedHotel = hotel;
						minTotalRate = totalRate;
					}
				}
			} catch (NullPointerException e) {
				cheapestBestRatedHotel = hotel;
				minTotalRate = totalRate;
			}
		}
		try {
			logger.debug(cheapestBestRatedHotel.getName() + ", Rating: " + cheapestBestRatedHotel.getRating()
					+ " and Total Rates: $" + minTotalRate);
		} catch (NullPointerException e) {
			logger.debug("No hotel found");
		}
		return cheapestBestRatedHotel;
	}

	public Hotel getCheapestBestRatedHotel(Customer customer) {
		int numWeekends = customer.getNumWeekends();
		int numWeekdays = customer.getNumWeekdays();
		Map<Hotel, Integer> hotelToTotalRateMap = hotels.stream().collect(Collectors.toMap(hotel -> hotel,
				hotel -> hotel.getRegularWeekendRate() * numWeekends + hotel.getRegularWeekdayRate() * numWeekdays));
		Hotel cheapestHotel = hotelToTotalRateMap.keySet().stream().min((hotel1, hotel2) -> {
			int rateDifference = hotelToTotalRateMap.get(hotel1) - hotelToTotalRateMap.get(hotel2);
			int ratingDifference = hotel1.getRating() - hotel2.getRating();
			return rateDifference == 0 ? -(ratingDifference) : rateDifference;
		}).orElse(null);
		try {
			logger.debug(cheapestHotel.getName() + ", Rating: " + cheapestHotel.getRating() + " and Total Rates: $"
					+ hotelToTotalRateMap.get(cheapestHotel));
		} catch (NullPointerException e) {
			logger.debug("No hotel found");
		}
		return cheapestHotel;
	}

	public Hotel getBestRatedHotel(Customer customer) {
		int numWeekends = customer.getNumWeekends();
		int numWeekdays = customer.getNumWeekdays();
		Hotel bestRatedHotel = hotels.stream().max((hotel1, hotel2) -> hotel1.getRating() - hotel2.getRating())
				.orElse(null);
		try {
			int totalRate = bestRatedHotel.getRegularWeekdayRate() * numWeekdays
					+ bestRatedHotel.getRegularWeekendRate() * numWeekends;
			logger.debug(bestRatedHotel.getName() + " & Total Rates $" + totalRate);
		} catch (NullPointerException e) {
			logger.debug("No hotel found");
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
			hotelReservation.getCheapestBestRatedHotelForRewards(customer);
		} catch (InvalidInputException e) {
			logger.info(e.getMessage());
		}
	}
}

class Customer {
	private int numWeekdays;
	private int numWeekends;
	private CustomerType customerType;

	public Customer(int numWeekdays, int numWeekends, CustomerType customerType) {
		super();
		this.numWeekdays = numWeekdays;
		this.numWeekends = numWeekends;
		this.customerType = customerType;
	}

	public CustomerType getCustomerType() {
		return customerType;
	}

	public void setCustomerType(CustomerType customerType) {
		this.customerType = customerType;
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

class InvalidInputException extends Exception {
	public InvalidInputException(String message) {
		super(message);
	}
}

enum CustomerType {
	REGULAR, REWARDS
}

class Hotel {
	private String name;
	private int regularWeekdayRate;
	private int regularWeekendRate;
	private int rewardsWeekdayRate;
	private int rewardsWeekendRate;
	private int rating;

	public Hotel(String name, int regularWeekdayRate, int regularWeekendRate, int rewardsWeekdayRate,
			int rewardsWeekendRate, int rating) {
		super();
		this.name = name;
		this.regularWeekdayRate = regularWeekdayRate;
		this.regularWeekendRate = regularWeekendRate;
		this.rewardsWeekdayRate = rewardsWeekdayRate;
		this.rewardsWeekendRate = rewardsWeekendRate;
		this.rating = rating;
	}

	public int getRewardsWeekdayRate() {
		return rewardsWeekdayRate;
	}

	public void setRewardsWeekdayRate(int rewardsWeekdayRate) {
		this.rewardsWeekdayRate = rewardsWeekdayRate;
	}

	public int getRewardsWeekendRate() {
		return rewardsWeekendRate;
	}

	public void setRewardsWeekendRate(int rewardsWeekendRate) {
		this.rewardsWeekendRate = rewardsWeekendRate;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	public int getRegularWeekendRate() {
		return regularWeekendRate;
	}

	public void setRegularWeekendRate(int regularWeekendRate) {
		this.regularWeekendRate = regularWeekendRate;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getRegularWeekdayRate() {
		return regularWeekdayRate;
	}

	public void setRegularWeekdayRate(int regularWeekdayRate) {
		this.regularWeekdayRate = regularWeekdayRate;
	}

	@Override
	public String toString() {
		return "Hotel [\nName=" + name + "\nRegular Weekday Rate=$" + regularWeekdayRate + "\nRegular Weekend Rate=$"
				+ regularWeekendRate + "\nRewards Weekday Rate=$" + rewardsWeekdayRate + "\nRewards Weekend Rate=$"
				+ rewardsWeekendRate + "\nRating= " + rating + "\n]";
	}
}
