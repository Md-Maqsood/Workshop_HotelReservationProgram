package com.bridgeLabs.hotelReservation;

import org.junit.Assert;
import org.junit.Test;

public class HotelReservationTest {
	@Test
	public void givenHotelWhenAddedToHotelReservationSystemShouldHaveTheSame() {
		HotelReservation hotelReservation = new HotelReservation();
		hotelReservation.addHotel("Lakewood", 110, 90, 80, 80, 3);
		String hotelName = hotelReservation.hotels.get(0).getName();
		int hotelRate = hotelReservation.hotels.get(0).getRegularWeekdayRate();
		Assert.assertEquals("Lakewood", hotelName);
		Assert.assertEquals(110, hotelRate);
	}

	@Test
	public void givenHotelsWhenAddedHotelsAndCheapestHotelFoundShouldReturnCheapestHotel() {
		HotelReservation hotelReservation = new HotelReservation();
		hotelReservation.addHotel("Lakewood", 110, 90, 80, 80, 3);
		hotelReservation.addHotel("Bridgewood", 150, 50, 110, 50, 4);
		hotelReservation.addHotel("Ridgewood", 220, 150, 100, 40, 5);
		Customer customer = new Customer(2, 0, CustomerType.REGULAR);
		Hotel cheapestHotel = hotelReservation.getCheapestBestRatedHotel(customer);
		Assert.assertEquals("Lakewood", cheapestHotel.getName());
	}

	@Test
	public void givenHotelWhenAddedToHotelReservationSystemShouldWeekdayAndWeekendRates() {
		HotelReservation hotelReservation = new HotelReservation();
		hotelReservation.addHotel("Lakewood", 110, 90, 80, 80, 3);
		int hotelWeekdayRate = hotelReservation.hotels.get(0).getRegularWeekdayRate();
		int hotelWeekendRate = hotelReservation.hotels.get(0).getRegularWeekendRate();
		Assert.assertEquals(90, hotelWeekendRate);
		Assert.assertEquals(110, hotelWeekdayRate);
	}

	@Test
	public void givenHotelsWhenAddedHotelsWithWeekdayAndWeekendRatesAndCheapestHotelFoundShouldReturnCheapestHotel() {
		HotelReservation hotelReservation = new HotelReservation();
		hotelReservation.addHotel("Lakewood", 110, 90, 80, 80, 3);
		hotelReservation.addHotel("Bridgewood", 150, 50, 110, 50, 4);
		hotelReservation.addHotel("Ridgewood", 220, 150, 100, 40, 5);
		Customer customer = new Customer(1, 2, CustomerType.REGULAR);
		Hotel cheapestHotel = hotelReservation.getCheapestBestRatedHotel(customer);
		Assert.assertEquals("Bridgewood", cheapestHotel.getName());
	}

	@Test
	public void givenHotelWhenAddedToHotelReservationSystemShouldHaveRating() {
		HotelReservation hotelReservation = new HotelReservation();
		hotelReservation.addHotel("Lakewood", 110, 90, 80, 80, 3);
		int rating = hotelReservation.hotels.get(0).getRating();
		Assert.assertEquals(3, rating);
	}

	@Test
	public void givenHotelsWhenAddedHotelsWithWeekdayAndWeekendRatesAndRatingsAndCheapestBestRatedHotelFoundShouldReturnCheapestBestRatedHotel() {
		HotelReservation hotelReservation = new HotelReservation();
		hotelReservation.addHotel("Lakewood", 110, 90, 80, 80, 3);
		hotelReservation.addHotel("Bridgewood", 150, 50, 110, 50, 4);
		hotelReservation.addHotel("Ridgewood", 220, 150, 100, 40, 5);
		Customer customer = new Customer(1, 1, CustomerType.REGULAR);
		Hotel cheapestBestRatedHotel = hotelReservation.getCheapestBestRatedHotel(customer);
		Assert.assertEquals("Bridgewood", cheapestBestRatedHotel.getName());
	}

	@Test
	public void givenHotelsWhenAddedHotelsAndBestRatedHotelFoundShouldReturnBestRatedHotel() {
		HotelReservation hotelReservation = new HotelReservation();
		hotelReservation.addHotel("Lakewood", 110, 90, 80, 80, 3);
		hotelReservation.addHotel("Bridgewood", 150, 50, 110, 50, 4);
		hotelReservation.addHotel("Ridgewood", 220, 150, 100, 40, 5);
		Customer customer = new Customer(1, 1, CustomerType.REGULAR);
		Hotel bestRatedHotel = hotelReservation.getBestRatedHotel(customer);
		Assert.assertEquals("Ridgewood", bestRatedHotel.getName());
	}

	@Test
	public void givenHotelWhenAddedToHotelReservationSystemShouldHaveRewardsWeekdayAndWeekendRates() {
		HotelReservation hotelReservation = new HotelReservation();
		hotelReservation.addHotel("Lakewood", 110, 90, 80, 80, 3);
		int hotelRewardsWeekdayRate = hotelReservation.hotels.get(0).getRewardsWeekdayRate();
		int hotelRewardsWeekendRate = hotelReservation.hotels.get(0).getRewardsWeekendRate();
		Assert.assertEquals(80, hotelRewardsWeekendRate);
		Assert.assertEquals(80, hotelRewardsWeekdayRate);
	}

	@Test
	public void givenHotelsWhenAddedHotelsAndCheapestBestRatedHotelFoundShouldReturnCheapestBestRatedHotelForRewards() {
		HotelReservation hotelReservation = new HotelReservation();
		hotelReservation.addHotel("Lakewood", 110, 90, 80, 80, 3);
		hotelReservation.addHotel("Bridgewood", 150, 50, 110, 50, 4);
		hotelReservation.addHotel("Ridgewood", 220, 150, 100, 40, 5);
		Customer customer = new Customer(1, 1, CustomerType.REWARDS);
		Hotel cheapestBestRatedHotel = hotelReservation.getCheapestBestRatedHotelForRewards(customer);
		Assert.assertEquals("Ridgewood", cheapestBestRatedHotel.getName());
	}
}
