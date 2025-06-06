Feature: Make a reservation

  Background:
    Given I am logged in as "afonso@gmail.com" with password "pass"

  Scenario: User successfully makes a reservation for a charging point
    When I select a charging station from the map
    And I click the "Reserve" button on a charging point
    And I set the reservation start time
    And I set the reservation end time
    And I click the "confirm-reservation-button"
    Then I should see the message "Reservation successfully created!"

  Scenario: User views reservation details
    When I visit the reservations page
    Then I should see at least one reservation with details

  Scenario: Generate, validate, and use OTP to start charging
    When I visit the reservations page
    And I click the "generate-otp-button" for the first reservation
    Then I store the OTP code for later use

    When I visit the slot page for the reserved charging point
    And I enter the stored OTP code into the inputs
    And I click the "validate-otp-button"
    Then I should see the car selection dropdown

    When I select a vehicle from the list
    And I click the "start-charging-button"
    Then I should see the charging session information
