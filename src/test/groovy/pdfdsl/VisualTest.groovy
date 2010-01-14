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
package pdfdsl

import java.awt.Color
import com.lowagie.text.pdf.BaseFont
import com.lowagie.text.pdf.PdfPTable
import com.lowagie.text.pdf.PdfPCell
import com.lowagie.text.ListItem
import com.lowagie.text.Phrase
import com.lowagie.text.Paragraph
import com.lowagie.text.Chunk
import com.lowagie.text.ZapfDingbatsList


public class VisualTest extends GroovyTestCase {

  void testIt() {
    TestPdfFactory.createPdf("target/original.pdf")

    def data = [
        addressLines: ["Christopher Smith Jr.", "1492 Columbus Way", "Plymoth, MA 02360"],
    ]

    PdfDsl dsl = new PdfDsl()

    def pdfTemplate1 = dsl.createTemplate {
      font id: 'f1', file: '/Library/Fonts/Herculanum.ttf', embedded: true
      font id: 'f2', name: BaseFont.TIMES_BOLD
      font id: 'f3', name: BaseFont.TIMES_ROMAN
      font id: 'verdana', file: '/Library/Fonts/Verdana.ttf', embedded: true
      font id: 'verdana bold', file: '/Library/Fonts/Verdana Bold.ttf', embedded: true

      write text: "100, top-100", at: [100, top - 100], page: 1

      table at: [100, top - 100], page: 1, width: 4 * 72, height: 600, {
        headers justified: center, data: ["hello\nworld", "column 0", "column 1", "column 2", "column 3"], font: 'f1'

        rows font: 'f2', data: [
            ["c1", "c2", "c3", "c4", "c5"],
            ["c1", "c2", "c3", "c4", "c5"],
            ["c1", "c2", "c3", "c4", "c5"]
        ]
      }

      write text: "I follow the table", at: [100, lastY - fontSize], page: 1
      write text: "and I follow it", at: [100, lastY - fontSize], page: 1

      table at: [100, lastY], page: 1, width: 4 * 72, height: 600, {
        headers justified: center, data: ["hello\nworld", "column 0", "column 1", "column 2", "column 3"], font: 'f1'

        rows font: 'f2', data: [
            ["c1", "c2", "c3", "c4", "c5"],
            ["c1", "c2", "c3", "c4", "c5"],
            ["c1", "c2", "c3", "c4", "c5"]
        ]
      }

    }

    def pdfTemplate2 = dsl.createTemplate {
      def y = top - (fontSize * 3) - 2
      line at: [center - 20, y], to: [center + 20, y], width: 2

      line at: [left, top], to: [right, bottom], page: 3
      line at: [left, bottom], to: [right, top], width: 5, page: 3
      line at: [50, top - 100], to: [3 * 72 + 150, top - 100], width: 8, color: Color.BLUE

      write text: "hello world 2", at: [25, 700], page: 2
      write text: "hello world 1", at: [26, 700 + fontSize], page: 2
      write text: "hello world 3", at: [25, 700 - fontSize], page: 2
      write text: "bottom-right", justified: right, at: [right, bottom], page: 1
      write text: "bottom-left", justified: left, at: [left, bottom], page: 1
      write text: "top-right", justified: right, at: [right, top - fontSize], page: 1, font: 'f3'
      write text: "top-left", at: [left, top - fontSize], page: 1, font: 'f2'
      write text: "top-center", at: [(right - left) / 2, top - fontSize], page: 1
      write text: "almost-top-center", at: [center, top - fontSize - fontSize], page: 1
      write text: "top-center-justified", justified: center, at: [center, top - fontSize * 3], page: 1
      write text: "top-right-justified", justified: right, at: [center, top - fontSize * 4], page: 1
      write text: "centered-middle", justified: center, at: [center, middle], page: 1, font: 'f1'

      rectangle at: [center, middle], width: 144, height: 72, backgroundColor: Color.lightGray, borderColor: Color.RED

      section page: 1, at: [left + 50, 400], borderColor: Color.BLUE, {
        line text: "pinky jones", font: 'f1'
        line text: "suite abc"
        line text: "123 main st"
        line text: "des moines, ia 50023"
      }

      section page: 1, at: [center, 300], justified: center, font: 'f3', fontSize: 24, borderColor: Color.RED, padding: 10, {
        line text: "pinky jones"
        line text: "suite abc"
        line text: "123 main st"
        line text: "des moines, ia 50023"
      }

      section page: 1, at: [right - 50, 200], justified: right, borderColor: Color.YELLOW, {
        line text: "pinky jones"
        line text: "suite abc"
        line text: "123 main st"
        line text: "des moines, ia 50023"
      }

      section page: 1, at: [left + 50, 250], width: 250, height: 150, justified: left, font: 'f3', fontSize: 10, {
        text value: "This is my Main Heading", font: 'f2', fontSize: 12, newline: 'after'
        text value: "This is important.", font: 'f2'
        text value: "This is where all the unimportant text follows.  It looks something like this ... asdkfasd asdf asdf asd fasdf asd f"
      }

      section page: 1, at: [left + 50, lastY - fontSize], width: 250, height: 150, justified: left, font: 'f3', fontSize: 10, color: Color.WHITE, borderColor: Color.YELLOW, backgroundColor: Color.BLACK, padding: 4, {
        text value: "This is my Main Heading", font: 'f2', fontSize: 20, newline: 'after'
      }

      section page: 1, at: [left + 50, lastY - 12], width: 250, height: 150, justified: left, font: 'f3', fontSize: 10, borderColor: Color.YELLOW, padding: 4, {
        text value: "This is important.", font: 'f2'
        text value: "This is where all the unimportant text follows.  It looks something like this ... asdkfasd asdf asdf asd fasdf asd f"
      }

      def tableHeight = 0
      canvas page: 2, {
        PdfPTable table = new PdfPTable(4)
        PdfPTable nested1 = new PdfPTable(2)
        nested1.addCell("1.1")
        nested1.addCell("1.2")
        PdfPTable nested2 = new PdfPTable(1)
        nested2.addCell("20.1")
        nested2.addCell("20.2")
        (0..<24).each {i ->
          switch (i) {
            case 1:
              table.addCell new PdfPCell(nested1)
              break
            case 20:
              table.addCell new PdfPCell(nested2)
              break
            default:
              table.addCell "cell $i"
          }
        }
        table.totalWidth = 400
        table.writeSelectedRows 0, -1, 200, (float) (9.5 * 72), canvas
        tableHeight = table.getTotalHeight()
      }

      canvas page: 2, {
        PdfPTable table = new PdfPTable(4)
        PdfPTable nested1 = new PdfPTable(2)
        nested1.addCell("1.1")
        nested1.addCell("1.2")
        PdfPTable nested2 = new PdfPTable(1)
        nested2.addCell("20.1")
        nested2.addCell("20.2")
        (0..<24).each {i ->
          switch (i) {
            case 1:
              table.addCell new PdfPCell(nested1)
              break
            case 17:
              def list = new ZapfDingbatsList(108, 15)
              list.add(new ListItem("item 1"))
              list.add(new ListItem("item 2"))
              list.add(new ListItem("item 3"))
              list.add(new ListItem("item 4"))
              def listCell = new PdfPCell()
              listCell.addElement(list)
              table.addCell listCell
              break
            case 18:
              def list = new com.lowagie.text.List(com.lowagie.text.List.UNORDERED, 10)
              list.add(new ListItem("item 1"))
              list.add(new ListItem("item 2"))
              list.add(new ListItem("item 3"))
              list.add(new ListItem("item 4"))
              def listCell = new PdfPCell()
              listCell.addElement(list)
              table.addCell listCell
              break
            case 20:
              table.addCell new PdfPCell(nested2)
              break
            default:
              table.addCell "cell $i"
          }
        }
        table.totalWidth = 400
        table.writeSelectedRows 0, -1, 200, (float) (9.5 * 72 - tableHeight), canvas
      }

      columns page: 3, {
        column {
          section at: [left + 50, top - 100], width: 225, height: 550, justified: left, font: 'f3', fontSize: 10, {
            text value: "This is my Main Heading", font: 'f2', fontSize: 12, newline: 'after'
            text value: "This is important.", font: 'f2'
            text value: "This is where all the unimportant text follows.  It looks something like this ... asdkfasd asdf asdf asd fasdf asd f"
            text value: "This is where all the unimportant text follows.  It looks something like this ... asdkfasd asdf asdf asd fasdf asd f"
          }
          section at: [left + 50, lastY - fontSize], width: 225, height: 550, justified: left, font: 'f3', fontSize: 10, {
            text value: "This is my Main Heading", font: 'f2', fontSize: 12, newline: 'after'
            text value: "This is important.", font: 'f2'
            text value: "This is where all the unimportant text follows.  It looks something like this ... asdkfasd asdf asdf asd fasdf asd f"
            text value: "This is where all the unimportant text follows.  It looks something like this ... asdkfasd asdf asdf asd fasdf asd f"
            text value: "This is where all the unimportant text follows.  It looks something like this ... asdkfasd asdf asdf asd fasdf asd f"
            text value: "This is where all the unimportant text follows.  It looks something like this ... asdkfasd asdf asdf asd fasdf asd f"
          }
        }
        column {
          section at: [center + 25, top - 100], width: 225, height: 550, justified: left, font: 'verdana', fontSize: 10, {
            text value: "This is my Main Heading", font: 'verdana bold', fontSize: 12, newline: 'after'
            text value: "This is important.", font: 'verdana bold'
            text value: "This is where all the unimportant text follows.  It looks something like this ... asdkfasd asdf asdf asd fasdf asd f"
            text value: "This is where all the unimportant text follows.  It looks something like this ... asdkfasd asdf asdf asd fasdf asd f"
            text value: "This is where all the unimportant text follows.  It looks something like this ... asdkfasd asdf asdf asd fasdf asd f"
            text value: "This is where all the unimportant text follows.  It looks something like this ... asdkfasd asdf asdf asd fasdf asd f"
            text value: "This is where all the unimportant text follows.  It looks something like this ... asdkfasd asdf asdf asd fasdf asd f"
            text value: "This is where all the unimportant text follows.  It looks something like this ... asdkfasd asdf asdf asd fasdf asd f"
            text value: "This is where all the unimportant text follows.  It looks something like this ... asdkfasd asdf asdf asd fasdf asd f"
            text value: "This is where all the unimportant text follows.  It looks something like this ... asdkfasd asdf asdf asd fasdf asd f"
            text value: "This is where all the unimportant text follows.  It looks something like this ... asdkfasd asdf asdf asd fasdf asd f"
            text value: "This is where all the unimportant text follows.  It looks something like this ... asdkfasd asdf asdf asd fasdf asd f"
            text value: "This is where all the unimportant text follows.  It looks something like this ... asdkfasd asdf asdf asd fasdf asd f"
            text value: "This is where all the unimportant text follows.  It looks something like this ... asdkfasd asdf asdf asd fasdf asd f"
            text value: "This is where all the unimportant text follows.  It looks something like this ... asdkfasd asdf asdf asd fasdf asd f"
            text value: "This is where all the unimportant text follows.  It looks something like this ... asdkfasd asdf asdf asd fasdf asd f"
            text value: "This is where all the unimportant text follows.  It looks something like this ... asdkfasd asdf asdf asd fasdf asd f"
          }
        }
      }

      section page: 3, at: [left + 50, min(lastY - fontSize, middle)], width: 7.5 * 72, height: 150, justified: left, font: 'f3', fontSize: 10, {
        text value: "This is my Main Heading", font: 'f2', fontSize: 12, newline: 'after'
        text value: "This is important.", font: 'f2'
        text value: "This is where all the unimportant text follows.  It looks something like this ... asdkfasd asdf asdf asd fasdf asd f"
        text value: "This is where all the unimportant text follows.  It looks something like this ... asdkfasd asdf asdf asd fasdf asd f"
        text value: "This is where all the unimportant text follows.  It looks something like this ... asdkfasd asdf asdf asd fasdf asd f"
      }

      section page: 2, at: [center, bottom + 150], width: right - center - 50, height: bottom + 72, justified: left, font: 'f3', fontSize: 10, {
        text value: "This is where all the unimportant text follows.  It looks something like this ... asdkfasd asdf asdf asd fasdf asd f"
        text value: "This is where all the unimportant text follows.  It looks something like this ... asdkfasd asdf asdf asd fasdf asd f"
        text value: "This is where all the unimportant text follows.  It looks something like this ... asdkfasd asdf asdf asd fasdf asd f"
      }

      def t = top - 100
      each data.addressLines, { write page: 4, at: [100, t], text: it; t -= fontSize}

      section page: 4, at: [100, top - 200], fontSize: 10, {
        line font: "f1", text: "ADDRESS:"
        each data.addressLines, { line text: it }
      }
    }

    new File("target/create.pdf").withOutputStream {
      it << pdfTemplate1.stamp(pdfTemplate2.create())
    }

    new File("target/createTableOnly.pdf").withOutputStream { it << pdfTemplate1.create() }

    new File("target/update.pdf").withOutputStream {
      it << pdfTemplate1.stamp(pdfTemplate2.stamp(new File("target/original.pdf").readBytes()))
    }
  }

}