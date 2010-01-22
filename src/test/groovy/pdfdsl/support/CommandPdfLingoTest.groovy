/**
 * Copyright to the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */
package pdfdsl.support


class CommandPdfLingoTest extends GroovyTestCase {
  private def commandDefinitions
  private def commands
  private def defaultSettings
  private def commandPdfLingo
  private def grandChildDefinition
  private def childDefinition

  void setUp() {
    grandChildDefinition = new CommandDefinition(new Object())
    childDefinition = new CommandDefinition(new Object(), [grandChild: grandChildDefinition])

    commands = []
    defaultSettings = [:]
    commandDefinitions = [
        test1: new CommandDefinition(new Object(), [other: childDefinition]),
        test2: new CommandDefinition(new Object()),
        test3: new CommandDefinition(new Object()),
    ]
    commandPdfLingo = new CommandPdfLingo(commands, defaultSettings, commandDefinitions)
  }

  void test_valid_commands() {
    execute {
      test1()
      test2()
      test3()
    }

    assertEquals 3, commands.size()

    doAssertCommand command:commands[0], expected:[COMMAND_NAME:"test1", COMMAND_OBJECT:commandDefinitions.test1.commandObject]
    doAssertCommand command:commands[1], expected:[COMMAND_NAME:"test2", COMMAND_OBJECT:commandDefinitions.test2.commandObject]
    doAssertCommand command:commands[2], expected:[COMMAND_NAME:"test3", COMMAND_OBJECT:commandDefinitions.test3.commandObject]
  }

  void test_command_with_only_attributes() {
    execute {
      test1 code:2, other:'stuff'
    }

    doAssertCommand command:commands[0], expected:[COMMAND_NAME:"test1", COMMAND_OBJECT:commandDefinitions.test1.commandObject, code: 2, other:'stuff']
  }

  void test_command_with_only_closure() {
    execute {
      test1 {
        other code:5
      }
    }

    def childCommands = commands[0].CHILDREN
    assertNotNull childCommands
    
    commands[0].remove("CHILDREN")
    doAssertCommand command:commands[0], expected:[COMMAND_NAME:"test1", COMMAND_OBJECT:commandDefinitions.test1.commandObject]
    doAssertCommand command:childCommands[0], expected:[COMMAND_NAME:"other", COMMAND_OBJECT:childDefinition.commandObject, code: 5]
  }

  void test_command_with_attributes_and_closure() {
    execute {
      test1 code:1, number:7, {
        other code:5
      }
    }

    def childCommands = commands[0].CHILDREN
    assertNotNull childCommands

    commands[0].remove("CHILDREN")
    doAssertCommand command:commands[0], expected:[COMMAND_NAME:"test1", COMMAND_OBJECT:commandDefinitions.test1.commandObject, code:1, number:7]
    doAssertCommand command:childCommands[0], expected:[COMMAND_NAME:"other", COMMAND_OBJECT:childDefinition.commandObject, code: 5]
  }

  void test_command_with_grand_child() {
    execute {
      test1 {
        other {
          grandChild code:8
        }
      }
    }

    def grandChildCommands = commands[0].CHILDREN[0].CHILDREN
    assertNotNull grandChildCommands

    commands[0].remove("CHILDREN")
    doAssertCommand command:commands[0], expected:[COMMAND_NAME:"test1", COMMAND_OBJECT:commandDefinitions.test1.commandObject]
    doAssertCommand command:grandChildCommands[0], expected:[COMMAND_NAME:"grandChild", COMMAND_OBJECT:grandChildDefinition.commandObject, code: 8]
  }

  void test_invalid_command() {
    shouldFail( {
      test1()
      test2()
      other()
    }, {
      assertEquals "other is not a valid command; valid commands for '/' are (test1, test2, test3)", it.message
    })
  }

  void test_invalid_grand_child_command() {
    shouldFail( {
      test1 {
        other {
          missing()
        }
      }
    }, {
      assertEquals "missing is not a valid command; valid commands for '/test1/other' are (grandChild)", it.message
    })
  }

  void test_location_in_grand_child() {
    execute {
      test1 {
        other {
          grandChild length:1.inch
        }
      }
    }

    def grandChildCommands = commands[0].CHILDREN[0].CHILDREN

    doAssertCommand command:grandChildCommands[0], expected:[COMMAND_NAME:"grandChild", COMMAND_OBJECT:grandChildDefinition.commandObject, length:72.0f]
  }

  void test_loop_in_grand_child() {
    execute {
      test1 {
        other {
          3.times {
            grandChild length: it.inch
          }
        }
      }
    }

    def grandChildCommands = commands[0].CHILDREN[0].CHILDREN

    doAssertCommand command:grandChildCommands[0], expected:[COMMAND_NAME:"grandChild", COMMAND_OBJECT:grandChildDefinition.commandObject, length: 0.0f]
    doAssertCommand command:grandChildCommands[1], expected:[COMMAND_NAME:"grandChild", COMMAND_OBJECT:grandChildDefinition.commandObject, length: 72.0f]
    doAssertCommand command:grandChildCommands[2], expected:[COMMAND_NAME:"grandChild", COMMAND_OBJECT:grandChildDefinition.commandObject, length: 144.0f]
  }

  private def doAssertCommand(values) {
    assertEquals values.expected, values.command
  }

  private def execute(closure) {
    closure.delegate = commandPdfLingo
    closure()
  }

  private def shouldFail(closure, failureClosure) {
    try {
      execute closure
      fail "expected failure, but was none"
    } catch (Exception e) {
      failureClosure e
    }
  }
}