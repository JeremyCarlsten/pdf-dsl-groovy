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

import com.lowagie.text.pdf.BaseFont
import java.awt.Color
import com.lowagie.text.Font

class MapWrapper {
  private mapIn

  MapWrapper(map) {
    mapIn = map
  }

  def propertyMissing(String name) {
    mapIn[name]
  }

  def getColor() {
      mapIn["color"] ?: Color.BLACK
  }

  def getText() {
      mapIn["text"] ?: ""
  }

  def getAt() {
    forceCoordinatesToLocation(mapIn["at"])
  }

  def getTo() {
    forceCoordinatesToLocation(mapIn["to"])
  }

  def getFont() {
    def font = new Font(baseFont, fontSize)
    if(color) {
      font.color = color
    }
    font
  }

  def getBaseFont() {
    if (mapIn.font) {
      mapIn.configuredFonts[mapIn.font]
    } else {
      BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED)
    }
  }

  float getTextLength() {
    baseFont.getWidthPoint(text, fontSize)
  }

  float getTextLength(t) {
    baseFont.getWidthPoint(t, fontSize)
  }

  float getJustificationOffset() {
    getJustificationOffset textLength
  }

  float getJustificationOffset(widthOfText) {
    def justification = mapIn["justified"]
    if (justification == Locations.center) {
      widthOfText / 2
    } else if (justification == Locations.right) {
      widthOfText
    } else 0
  }

  def plus(Map map) {
    new MapWrapper(mapIn + map)  
  }

  def plus(MapWrapper wrapper) {
    plus wrapper.mapIn
  }

  def getX(DslWriter dslWriter) {
    at[0].value(dslWriter.getPageSize(page), this)
  }

  def getY(DslWriter dslWriter) {
    at[1].value(dslWriter.getPageSize(page), this)
  }

  def getWidth() {
    forceToLocation(mapIn["width"] ?: 0)
  }

  def getWidth(DslWriter dslWriter) {
    width.value(dslWriter.getPageSize(page), this)
  }

  def getHeight() {
    forceToLocation(mapIn["height"] ?: 0)
  }

  def getHeight(DslWriter dslWriter) {
    height.value(dslWriter.getPageSize(page), this)
  }

  private List forceCoordinatesToLocation(location) {
    def x = forceToLocation(location[0])
    def y = forceToLocation(location[1])
    [x, y]
  }

  private Location forceToLocation(location) {
    (location instanceof Number) ? new Location(location) : location
  }

}