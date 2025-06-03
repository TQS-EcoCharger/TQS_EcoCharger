Feature: Reserving a charging point

  Background: User is logged in
    Given I am on the login page
    When I enter "afonso@gmail.com" into the "email" field
    And I enter "pass" into the "password" field
    And I click the "login-button"
    Then I should be redirected to the home page

  Scenario: User selects a station and makes a reservation
    When I click a station marker on the map
    And I click the "reserve-button"
    Then I should see a reservation confirmation
