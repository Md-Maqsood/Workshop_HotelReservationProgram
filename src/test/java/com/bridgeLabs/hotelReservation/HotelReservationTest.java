package com.bridgeLabs.hotelReservation;

import org.junit.Assert;
import org.junit.Test;

public class HotelReservationTest {
	@Test
	public void givenHotelWhenAddedToHotelReservationSystemShouldHaveTheSame() {
		HotelReservation hotelReservation=new HotelReservation();
		hotelReservation.addHotel("Lakewood", 110);
		String hotelName=hotelReservation.hotels.get(0).getName();
		int hotelRate=hotelReservation.hotels.get(0).getRate();
		Assert.assertEquals("Lakewood", hotelName);
		Assert.assertEquals(110, hotelRate);
	}
}
