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