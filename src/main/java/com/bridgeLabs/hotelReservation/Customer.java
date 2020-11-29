package com.bridgeLabs.hotelReservation;

public class Customer {
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
