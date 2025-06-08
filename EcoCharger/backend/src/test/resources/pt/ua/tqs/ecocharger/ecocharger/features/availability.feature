Feature: Check Availability of a Charging Point

    Background:
        Given I am on the login page
        When I enter "ricardo.antunes2002@gmail.com" into the "email" field
        And I enter "banana" into the "password" field
        And I click the "login-button"

    Scenario: Checking the availability of a charging point
        When the user clicks on a marker
        And I click the "open-schedule-modal-btn"
        Then I should see the schedule modal