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
@tag
Feature: Like note with no likes
  User likes a note, with was created by other user
  
  Background: User is logged in
  	Given I open the loginPage_likesNotes
    And User is created in Restapi_likesNotes
    When I type Username_likesNotes
    And I type password_likesNotes
    And I click on button to confirm_likesNotes
    Then I am on the board overview_likesNotes
    
  @tag2
  Scenario: Create new board
  When I open new board formular
  And I set board title
  And I set board column input0
  And I set board column input1
  And I set board column input2
  And I confirm board creation
  Then There should be a new board
  
  @tag3
  Scenario: Create new note in board
  Given I open Board One
  When I click on button new note
  And I type in text area "neu"
  And I submit Text Area Content
  Then Note should be visible  
  
  @tag4
  Scenario: Like note one with no likes
  When I like first note
  Then like should be 1
  