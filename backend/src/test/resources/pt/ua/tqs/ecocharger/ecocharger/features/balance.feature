Feature: Stripe Balance Top-Up

  Background:
    Given I am logged in as "afonso@gmail.com" with password "pass"

  Scenario: Successfully top up balance via Stripe
    When I open the balance top-up modal
    And I enter "10.0" as the top-up amount
    And I click the "top-up-button"
    Then I should be redirected to Stripe Checkout
    When I complete the test Stripe payment
    Then I should be redirected back and see updated balance
