package com.etraveli.payments.client.dto.integration;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AirlineReservationRecordDto {
	private String firstName;
	private String itineraryId;
	private String lastName;
	private String pnr;
	private String ticketNumber;
	private String tripLegDateTime;
	private String tripLegFrom;
	private String tripLegTo;

	@JsonProperty("FirstName")
	public String getFirstName() {
		return firstName;
	}

	@JsonProperty("ItineraryId")
	public String getItineraryId() {
		return itineraryId;
	}

	@JsonProperty("LastName")
	public String getLastName() {
		return lastName;
	}

	@JsonProperty("Pnr")
	public String getPnr() {
		return pnr;
	}

	@JsonProperty("TicketNumber")
	public String getTicketNumber() {
		return ticketNumber;
	}

	@JsonProperty("TripLegDateTime")
	public String getTripLegDateTime() {
		return tripLegDateTime;
	}

	@JsonProperty("TripLegFrom")
	public String getTripLegFrom() {
		return tripLegFrom;
	}

	@JsonProperty("TripLegTo")
	public String getTripLegTo() {
		return tripLegTo;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setItineraryId(String itineraryId) {
		this.itineraryId = itineraryId;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setPnr(String pnr) {
		this.pnr = pnr;
	}

	public void setTicketNumber(String ticketNumber) {
		this.ticketNumber = ticketNumber;
	}

	public void setTripLegDateTime(String tripLegDateTime) {
		this.tripLegDateTime = tripLegDateTime;
	}

	public void setTripLegFrom(String tripLegFrom) {
		this.tripLegFrom = tripLegFrom;
	}

	public void setTripLegTo(String tripLegTo) {
		this.tripLegTo = tripLegTo;
	}
}
