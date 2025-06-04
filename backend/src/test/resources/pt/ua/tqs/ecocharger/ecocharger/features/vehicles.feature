Feature: Check and Manage Vehicles
    As a Driver,
    I want to register my car in the application
    So that I can use it at charging stations and receive accurate insights about energy usage, CO₂ savings, and cost efficiency.

    Background:
        Given I am on the login page
        When I enter "ricardo.antunes2002@gmail.com" into the "email" field
        And I enter "banana" into the "password" field
        And I click the "login-button"
        And I navigate to the vehicles page

    Scenario: Checking the vehicles page
        Then I should see a table of vehicles
        And the table should have 2 rows
        And the table should have the following columns:
            | ID | Name | Make | Actions |
        And the table should contain the following data:
            | ID | Name                  | Make    |
            | 3  | Hyundai Kona Electric | Hyundai |
            | 4  | Renault Zoe           | Renault |

    Scenario: Adding multiple new vehicles with valid data
        When I click the "_addCarButton_1foq6_13" button
        And I fill in the vehicle form with:
            | name-input    | make-input  | model-input   | year-input | license-input | capacity-input | level-input | kilometers-input | consumption-input |
            | Tesla Model 3 | Tesla | Model 3 | 2020 | AA-12-CC      | 75               | 46            | 17000      | 15          |
        And I click the "_submitBtn_139ol_92" button
        Then I should see a success message saying "Vehicle added successfully!"

        When I click the "_addCarButton_1foq6_13" button
        And I fill in the vehicle form with:
            | name-input    | make-input  | model-input   | year-input | license-input | capacity-input | level-input | kilometers-input | consumption-input |
            | Toyota Prius | Toyota | Prius | 2018 | BB-34-DD      | 95               | 80            | 80000      | 4.5         |
        And I click the "_submitBtn_139ol_92" button
        Then I should see a success message saying "Vehicle added successfully!"

        When I click the "_addCarButton_1foq6_13" button
        And I fill in the vehicle form with:
            | name-input    | make-input  | model-input   | year-input | license-input | capacity-input | level-input | kilometers-input | consumption-input |
            | Citroen C4 | Citroen | C4    | 2019 | CC-56-EE      | 50               | 30            | 50000      | 6.2         |
        And I click the "_submitBtn_139ol_92" button
        Then I should see a success message saying "Vehicle added successfully!"

        And the table should have 5 rows
        And the table should contain the following data:
            | ID | Name                  | Make    |
            | 3  | Hyundai Kona Electric | Hyundai |
            | 4  | Renault Zoe           | Renault |
            | 5  | Tesla Model 3         | Tesla   |
            | 6  | Toyota Prius          | Toyota  |
            | 7  | Citroen C4            | Citroen |

    Scenario: Checking the details of a vehicle
        When I click the "view\-car\-3"
        Then I should see the vehicle details page for "Hyundai Kona Electric"
        And I should see the following details:
            | Name                      | Hyundai Kona Electric |
            | Make                      | Hyundai               |
            | Model                     | Kona Electric         |
            | Mileage                   | 15000 km              |
            | Registered                | 2020                  |
            | Energy Consumption        | 16.2 kWh              |
            | Battery Capacity          | 64 kWh                |
            | License Plate             | EE-56-FF              |
