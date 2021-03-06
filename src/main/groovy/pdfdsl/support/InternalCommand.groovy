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

class InternalCommand {
  def lingo
  def defaults = [:]

  void setLingo(lingo) {
    this.lingo = new MapWrapper(defaults) + lingo
  }

  def stampWith(DslWriter dslWriter) {
  }

  def preChildExecute(childCommand, int index) {
    new MapWrapper(lingo.mapIn + childCommand)
  }

  def postChildExecute(childCommand, int index) {
  }

  def postChildrenExecute() {
  }

}