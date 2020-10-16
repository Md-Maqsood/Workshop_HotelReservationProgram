package com.bridgeLabs.hotelReservation;

import org.junit.Assert;
import org.junit.Test;

public class HotelReservationTest {
	@Test
	public void givenHotelWhenAddedToHotelReservationSystemShouldHaveTheSame() {
		HotelReservation hotelReservation = new HotelReservation();
		hotelReservation.addHotel("Lakewood", 110, 90);
		String hotelName = hotelReservation.hotels.get(0).getName();
		int hotelRate = hotelReservation.hotels.get(0).getWeekdayRate();
		Assert.assertEquals("Lakewood", hotelName);
		Assert.assertEquals(110, hotelRate);
	}

	@Test
	public void givenHotelsWhenAddedHotelsAndCheapestHotelFoundShouldReturnCheapestHotel() {
		HotelReservation hotelReservation = new HotelReservation();
		hotelReservation.addHotel("Lakewood", 110, 90);
		hotelReservation.addHotel("Bridgewood", 150, 50);
		hotelReservation.addHotel("Ridgewood", 220, 150);
		Customer customer = new Customer(2, 0);
		Hotel cheapestHotel = hotelReservation.getCheapestHotel(customer);
		Assert.assertEquals("Lakewood", cheapestHotel.getName());
	}

	@Test
	public void givenHotelWhenAddedToHotelReservationSystemShouldWeekdayAndWeekendRates() {
		HotelReservation hotelReservation = new HotelReservation();
		hotelReservation.addHotel("Lakewood", 110, 90);
		int hotelWeekdayRate = hotelReservation.hotels.get(0).getWeekdayRate();
		int hotelWeekendRate = hotelReservation.hotels.get(0).getWeekendRate();
		Assert.assertEquals(90, hotelWeekendRate);
		Assert.assertEquals(110, hotelWeekdayRate);
	}
	
	@Test
	public void givenHotelsWhenAddedHotelsWithWeekdayAndWeekendRatesAndCheapestHotelFoundShouldReturnCheapestHotel() {
		HotelReservation hotelReservation = new HotelReservation();
		hotelReservation.addHotel("Lakewood", 110, 90);
		hotelReservation.addHotel("Bridgewood", 150, 50);
		hotelReservation.addHotel("Ridgewood", 220, 150);
		Customer customer = new Customer(1, 2);
		Hotel cheapestHotel = hotelReservation.getCheapestHotel(customer);
		Assert.assertEquals("Bridgewood", cheapestHotel.getName());
	}
}
