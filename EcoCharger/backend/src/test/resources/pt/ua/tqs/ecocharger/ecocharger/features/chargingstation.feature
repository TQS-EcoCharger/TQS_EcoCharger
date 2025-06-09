@ET-18
Feature: Login and Map Interaction

  Scenario: User logs in and clicks on a map marker
    Given the user accesses the login page
    When the user logs in with email "tomas@gmail.com" and password "pass1"
    Then the user should see the map with markers
    When the user clicks on a marker
