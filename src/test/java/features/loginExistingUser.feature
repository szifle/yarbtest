#Author: your.email@your.domain.com
#Keywords Summary :
#Feature: List of scenarios.
#Scenario: Business rule through list of steps with arguments.
#Given: Some precondition step
#When: Some key actions
#Then: To observe outcomes or validation
#And,But: To enumerate more Given,When,Then steps
#Scenario Outline: List of steps for data-driven as an Examples and <placeholder>
#Examples: Container for s table
#Background: List of steps run before each of the scenarios
#""" (Doc Strings)
#| (Data Tables)
#@ (Tags/Labels):To group Scenarios
#<> (placeholder)
#""
## (Comments)
#Sample Feature Definition Template
@LoginExistingUser
Feature: LoginExistingUser
  I want to create a user in restapi and log him in

  @LoginExistingUser
  Scenario: loginExistingUser
    Given I open the loginPage
    And User is created in Restapi
    When I type Username
    And I type password
    And I click on button to confirm
    Then I am on the board overview