Feature: Registering a new user
  As a user
  I want to register a new account
  So that I can access the application

  Background:
    Given I am on the registration page

    Scenario: Registering with valid data
      When I fill in the registration form with:
        | name   |  password | confirmPassword  | email                 |
        | User 3 |  pass123    | pass123            | user3@gmail.com       |
      And I submit the form
      Then I should be redirected to the home page

    Scenario: Registering with an already taken email
      When I fill in the registration form with:
        | name   |  password | confirmPassword  | email                 |
        | User 1 |  pass123    | pass123            | user1@gmail.com       |
      And I submit the form
      Then I should see an error message saying "Email already in use"
      And I should remain on the registration page

      Scenario: Registering with a weak password
      When I fill in the registration form with:
        | name   |  password | confirmPassword  | email                 |
        | User 5 |  pas     | pas               | user5@gmail.com       |
      And I submit the form
      Then I should see an error message saying "Password must be at least 6 characters"
      And I should remain on the registration page

    Scenario: Registering with mismatched passwords
      When I fill in the registration form with:
        | name   |  password | confirmPassword  | email                 |
        | User 6 |  pass6    | pass7            | user6@gmail.com       |
      And I submit the form
      Then I should see an error message saying "Passwords do not match."
      And I should remain on the registration page

    Scenario: Registering with an invalid email
      When I fill in the registration form with:
        | name   |  password | confirmPassword  | email                 |
        | User 7 |  pass8    | pass8            | user7gmail.com        |
      And I submit the form
      Then I should see an invalid email error message
      And I should remain on the registration page

