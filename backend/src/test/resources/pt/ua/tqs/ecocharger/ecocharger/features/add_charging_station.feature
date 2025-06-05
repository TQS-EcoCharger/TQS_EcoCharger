Feature: Add a new charging station

  Scenario: Successfully adding a new charging station
    Given the user accesses the login page
    When the user logs in with email "tomas@gmail.com" and password "pass1"
    And the user clicks on the "Add Station" button
    And the user fills in the station form with:
      | cityName | Aveiro                  |
      | address  | Rua Fialho De ALMEIDA   |
      | latitude | 43                      |
      | longitude | -8                     |
      | countryCode | PT - Portugal        |
    And the user submits the station form
    Then the user should see the map with markers
