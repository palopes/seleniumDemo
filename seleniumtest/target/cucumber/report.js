$(document).ready(function() {var formatter = new CucumberHTML.DOMFormatter($('.cucumber-report'));formatter.uri("DemoSeleniumTests.feature");
formatter.feature({
  "line": 1,
  "name": "Demo Selenium Tests",
  "description": "This file is the container of the scenarios for SeleniumEasy portal",
  "id": "demo-selenium-tests",
  "keyword": "Feature"
});
formatter.before({
  "duration": 10432466500,
  "status": "passed"
});
formatter.scenario({
  "line": 5,
  "name": "Open and closing portal",
  "description": "",
  "id": "demo-selenium-tests;open-and-closing-portal",
  "type": "scenario",
  "keyword": "Scenario",
  "tags": [
    {
      "line": 4,
      "name": "@SD-0001"
    }
  ]
});
formatter.step({
  "line": 6,
  "name": "open SeleniumEasy portal",
  "keyword": "When "
});
formatter.step({
  "line": 7,
  "name": "i close publicity popup",
  "keyword": "Then "
});
formatter.step({
  "line": 8,
  "name": "i click on the top menu \"Input forms\"",
  "keyword": "Then "
});
formatter.step({
  "line": 9,
  "name": "select on sub menu \"Simple Form Demo\"",
  "keyword": "And "
});
formatter.step({
  "line": 10,
  "name": "i insert \"Hello world!\" on single input field example",
  "keyword": "And "
});
formatter.step({
  "line": 11,
  "name": "i press the button \"Show Message\"",
  "keyword": "And "
});
formatter.match({
  "location": "CommonStepDefinitions.open_SeleniumEasy_portal()"
});
formatter.result({
  "duration": 42108865300,
  "status": "passed"
});
formatter.match({
  "location": "CommonStepDefinitions.i_close_publicity_popup()"
});
formatter.result({
  "duration": 176058700,
  "status": "passed"
});
formatter.match({
  "arguments": [
    {
      "val": "Input forms",
      "offset": 25
    }
  ],
  "location": "CommonStepDefinitions.i_click_on_the_top_menu(String)"
});
formatter.result({
  "duration": 169094400,
  "status": "passed"
});
formatter.match({
  "arguments": [
    {
      "val": "Simple Form Demo",
      "offset": 20
    }
  ],
  "location": "CommonStepDefinitions.select_on_sub_menu(String)"
});
formatter.result({
  "duration": 2886290000,
  "status": "passed"
});
formatter.match({
  "arguments": [
    {
      "val": "Hello world!",
      "offset": 10
    }
  ],
  "location": "CommonStepDefinitions.i_insert_on_single_input_field_example(String)"
});
formatter.result({
  "duration": 796267600,
  "status": "passed"
});
formatter.match({
  "arguments": [
    {
      "val": "Show Message",
      "offset": 20
    }
  ],
  "location": "CommonStepDefinitions.i_press_the_button(String)"
});
formatter.result({
  "duration": 181008700,
  "status": "passed"
});
formatter.after({
  "duration": 281600,
  "status": "passed"
});
});