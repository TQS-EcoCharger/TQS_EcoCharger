@ET-20
Feature: Add a station to the map

  Scenario: Add a new station successfully
    Given I am on the home page
    When I click on the 8th marker icon
    And I click on the "Add Station" button
    And I fill the station form with:
      | cityName    | Lisboa                   |
      | address     | Rua fiulaho de aAASAS    |
      | latitude    | 43                       |
      | longitude   | -9                       |
      | countryCode | PT - Portugal            |
    And I submit the station form
    Then I should see the new marker on the map
