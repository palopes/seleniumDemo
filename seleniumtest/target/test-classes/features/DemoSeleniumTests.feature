Feature: Demo Selenium Tests
  This file is the container of the scenarios for SeleniumEasy portal

  @SD-0001
  Scenario: Open and closing portal
    When open SeleniumEasy portal
    Then i close publicity popup
    Then i click on the top menu "Input forms"
    And select on sub menu "Simple Form Demo"
    And i insert "Hello world!" on single input field example
    And i press the button "Show Message"

# @SD-0002
# Scenario: Open and closing portal
#   When open SeleniumEasy portal
#   #Then i close publicity popup
#   Then i click on the top menu "Input forms"
#   And select on sub menu "Simple Form Demo"
#   And i insert 40 on the "a" field
#   And i insert 2 on the "b" field
#   And i press the button "Get Total"
#   Then i validate if the sum of values is numberic