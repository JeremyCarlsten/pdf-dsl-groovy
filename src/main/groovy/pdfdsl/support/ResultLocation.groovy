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

class ResultLocation extends Location {
  private String operation
  private Location location1
  private Location location2

  ResultLocation(String operation, Number location1, Location location2) {
    this(operation, new Location(location1), location2)
  }

  ResultLocation(String operation, Location location1, Number location2) {
    this(operation, location1, new Location(location2))
  }

  ResultLocation(String operation, Number location1, Number location2) {
    this(operation, new Location(location1), new Location(location2))
  }

  ResultLocation(String operation, Location location1, Location location2) {
    this.operation = operation
    this.location1 = location1
    this.location2 = location2
    this.invoked = false
    this.cache = location1.cache & location2.cache

    valueClosure = {rect, mapWrapper ->
      def value1 = location1.value(rect, mapWrapper)
      def value2 = location2.value(rect, mapWrapper)
      switch (operation) {
        case "-":
          return value1 - value2
        case "+":
          return value1 + value2
        case "/":
          return value1 / value2
        case "*":
          return value1 * value2
      }
    }
  }

  String toString() {
    (location1.invoked && location2.invoked) ?
      String.valueOf(valueClosure(null, null)) :
      "(" + location1.toString() + " " + operation + " " + location2.toString() + ")"
  }

}