@ET-49
Feature: Login Page

  Scenario: User logs in with valid credentials
    Given I am on the login page
    When I enter "afonso@gmail.com" into the "email" field
    And I enter "pass" into the "password" field
    And I click the "login-button"
    Then I should be redirected to the home page
