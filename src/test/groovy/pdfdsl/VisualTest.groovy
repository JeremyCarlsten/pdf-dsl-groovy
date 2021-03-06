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

import com.lowagie.text.Chunk
import com.lowagie.text.Font
import com.lowagie.text.List
import com.lowagie.text.ListItem
import com.lowagie.text.pdf.BaseFont
import com.lowagie.text.pdf.PdfPCell
import com.lowagie.text.pdf.PdfPTable
import java.awt.Color

public class VisualTest extends GroovyTestCase {

  void testIt() {
    TestPdfFactory.createPdf("target/original.pdf")

    def data = [
        addressLines: ["Christopher Smith Jr.", "1492 Columbus Way", "Plymoth, MA 02360"],
    ]

    def owner = this

    PdfDsl dsl = new PdfDsl()

    def pdfTemplate1 = dsl.createTemplate {
      defaults margin:0.75.inch, font: 'text'
      
      font id: 'bold', name: BaseFont.TIMES_BOLD
      font id: 'times', name: BaseFont.TIMES_ROMAN

      namedFont id: 'text', font: 'times', size: 11
      namedFont id: 'bold text', font: 'bold', size: 24

      write text: "100, top-100", at: [100, top - 100], page: 1, font: 'bold text'

      table at: [100, top - 100], page: 1, width: 4 * 72, height: 600, {
        headers justified: center, data: ["hello\nworld", "column 0", "column 1", "column 2", "column 3"], font: 'bold'

        rows font: 'bold', data: [
            ["c1", "c2", "c3", "c4", "c5"],
            ["c1", "c2", "c3", "c4", "c5"],
            ["c1", "c2", "c3", "c4", "c5"]
        ]
      }

      write text: "I follow the table", at: [100, lastY - fontSize], page: 1
      write text: "and I follow it", at: [100, lastY - fontSize], page: 1

      table at: [100, lastY], page: 1, width: 4 * 72, height: 600, {
        headers justified: center, data: ["hello\nworld", "column 0", "column 1", "column 2", "column 3"], font: 'bold'

        rows font: 'bold', data: [
            ["c1", "c2", "c3", "c4", "c5"],
            ["c1", "c2", "c3", "c4", "c5"],
            ["c1", "c2", "c3", "c4", "c5"]
        ]
      }
  
      page number:4, {
        def headingTemplate = { description ->
          spacer height:4.mm
          section {
            text value:description, font:"bold", fontSize: 12
          }
          spacer height:1.mm
        }

        def sectionTemplate = {
          section borderColor:Color.BLACK, {
            text value:"xxxx xx x x x x xxxx x xxx x x xxx x xx xxxxxx xxxxx x xxxxxx"
          }
        }

        include template:headingTemplate, args:['width: 1.5 inch']

        columns width:1.5.inches, spacing:0.5.inch, {
          column {
            include template:sectionTemplate
          }
          column {
            include template:sectionTemplate
          }
          column {
            include template:sectionTemplate
          }
        }

        include template:headingTemplate, args:['1.5 inch, 1.5 inch, 1.5 inch']

        columns spacing:0.5.inch, {
          column width:1.5.inches, {
            include template:sectionTemplate
          }
          column width:1.5.inches, {
            include template:sectionTemplate
          }
          column width:1.5.inches, {
            include template:sectionTemplate
          }
        }

        include template:headingTemplate, args:['implied, implied, implied']

        columns spacing:0.5.inch, {
          column {
            include template:sectionTemplate
          }
          column {
            include template:sectionTemplate
          }
          column {
            include template:sectionTemplate
          }
        }

        include template:headingTemplate, args:['1 inch, implied, implied']

        columns spacing:0.5.inch, {
          column width: 1.inch, {
            include template:sectionTemplate
          }
          column {
            include template:sectionTemplate
          }
          column {
            include template:sectionTemplate
          }
        }

        include template:headingTemplate, args:['1 inch, 1 inch, implied']

        columns spacing:0.5.inch, {
          column width: 1.inch, {
            include template:sectionTemplate
          }
          column width: 1.inch, {
            include template:sectionTemplate
          }
          column {
            include template:sectionTemplate
          }
        }

        include template:headingTemplate, args:['widths: 1 inch, 1 inch, implied']

        columns widths: [1.inch, 1.inch], spacing:0.5.inch, {
          column {
            include template:sectionTemplate
          }
          column {
            include template:sectionTemplate
          }
          column {
            include template:sectionTemplate
          }
        }

        include template:headingTemplate, args:['widths: 1 inch, 1 inch, implied -- column 2 overriden to 2 inches']

        columns widths: [1.inch, 1.inch], spacing:0.5.inch, {
          column {
            include template:sectionTemplate
          }
          column width:2.inches, {
            include template:sectionTemplate
          }
          column {
            include template:sectionTemplate
          }
        }
      }
    }

    def pdfTemplate2 = dsl.createTemplate {
      def y = top - (fontSize * 3) - 2
      line at: [center - 20, y], to: [center + 20, y], width: 2

      line at: [left, top], to: [right, bottom], page: 3
      line at: [left, bottom], to: [right, top], width: 5, page: 3
      line at: [50, top - 100], to: [3 * 72 + 150, top - 100], width: 8, color: Color.BLUE

      write text: "hello world 2", at: [25, 700], page: 5
      write text: "hello world 1", at: [26, 700 + fontSize], page: 2
      write text: "hello world 3", at: [25, 700 - fontSize], page: 2
      write text: "bottom-right", justified: right, at: [right, bottom], page: 1
      write text: "bottom-left", justified: left, at: [left, bottom], page: 1
      write text: "top-right", justified: right, at: [right, top - fontSize], page: 1, font: 'times'
      write text: "top-left", at: [left, top - fontSize], page: 1, font: 'bold'
      write text: "top-center", at: [(right - left) / 2, top - fontSize], page: 1
      write text: "almost-top-center", at: [center, top - fontSize - fontSize], page: 1
      write text: "top-center-justified", justified: center, at: [center, top - fontSize * 3], page: 1
      write text: "top-right-justified", justified: right, at: [center, top - fontSize * 4], page: 1
      write text: "centered-middle", justified: center, at: [center, middle], page: 1, font: 'bold'

      rectangle at: [center, middle], width: 144, height: 72, backgroundColor: Color.lightGray, borderColor: Color.RED

      section page: 1, at: [left + 50, 400], borderColor: Color.BLUE, {
        line text: "pinky jones", font: 'bold'
        line text: "suite abc"
        line text: "123 main st"
        line text: "des moines, ia 50023"
      }

      section page: 1, at: [center, 300], justified: center, font: 'times', fontSize: 24, borderColor: Color.RED, padding: 10, {
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

      section page: 1, at: [left + 50, 250], width: 250, height: 150, justified: left, font: 'times', fontSize: 10, {
        text value: "This is my Main Heading", font: 'bold', fontSize: 12, newline: 'after'
        text value: "This is important.", font: 'bold'
        text value: "This is where all the unimportant text follows.  It looks something like this ... asdkfasd asdf asdf asd fasdf asd f"
      }

      spacer height: 1.inch
      
      section page: 1, height: 150, justified: left, font: 'times', fontSize: 10, borderColor: Color.YELLOW, padding: 4, {
        text value: "This is important.", font: 'bold'
        text value: "This is where all the unimportant text follows.  It looks something like this ... asdkfasd asdf asdf asd fasdf asd f"
      }

      canvas page: 2, at:[200, 9.5.inches], {
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
        insertTable table:table
      }

      canvas page: 2, at:[200, lastY], {
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
              def createItem = {
                def item = new ListItem(it)
                item.setListSymbol(new Chunk((char) 108, new Font(Font.ZAPFDINGBATS, 5)))
                item.setLeading(10f)
                item.setSpacingAfter(0f)
                item
              }

              def list = new com.lowagie.text.List(false, 10)
              list.add(createItem("item 1"))
              list.add(createItem("item 2"))
              list.add(createItem("item 3"))
              list.add(createItem("item 4"))
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
        insertTable table:table
      }

      canvas page: 2, at:[200, lastY - 0.5.inch], {
        PdfPTable table = new PdfPTable([3.inches, 2.inches] as float[])
        (0..<12).each {i ->
          table.addCell "cell $i"
        }
        table.totalWidth = 400
        insertTable table:table

        table = new PdfPTable([3.inches, 2.inches, 1.inch] as float[])
        (0..<12).each {i ->
          table.addCell "cell $i"
        }
        table.totalWidth = 400
        insertTable table:table
      }

      page number: 3, {
        section justified: left, font: 'times', fontSize: 10, {
          text value: "This is my Main Heading", font: 'bold', fontSize: 12, newline: 'after'
          text value: "This is important.", font: 'bold'
          text value: "This is where all the unimportant text follows.  It looks something like this ... asdkfasd asdf asdf asd fasdf asd f"
          text value: "This is where all the unimportant text follows.  It looks something like this ... asdkfasd asdf asdf asd fasdf asd f"
          text value: "This is where all the unimportant text follows.  It looks something like this ... asdkfasd asdf asdf asd fasdf asd f"
          text value: "This is where all the unimportant text follows.  It looks something like this ... asdkfasd asdf asdf asd fasdf asd f"
        }

        spacer height: 0.25.inch

        columns widths: [250, 250], spacing:0.5.inch, {
          column sectionSpacing:10, {
            section justified: left, font: 'times', fontSize: 10, {
              text value: "This is my Main Heading", font: 'bold', fontSize: 12, newline: 'after'
              text value: "This is important.", font: 'bold'
              text value: "This is where all the unimportant text follows.  It looks something like this ... asdkfasd asdf asdf asd fasdf asd f"
            }

            spacer height: 0.25.inch
            
            section justified: left, font: 'times', fontSize: 10, {
              text value: "This is my Main Heading", font: 'bold', fontSize: 12, newline: 'after'
              text value: "This is important.", font: 'bold'
              text value: "This is where all the unimportant text follows.  It looks something like this ... asdkfasd asdf asdf asd fasdf asd f"
              text value: "This is where all the unimportant text follows.  It looks something like this ... asdkfasd asdf asdf asd fasdf asd f"
            }

            List list = new List(false, 10)
            list.add "item 1, xxx x xx x xx x xxx x xxxx x xxx x xxx x xxx xx xxx xxxxx xxx xxxxxx xxxxx xxxxx xxx xx xxx xxxx x xx xxxxx xx xx x xxx"
            list.add "item 2"
            list.add "item 3"
            insert list:list, leading:1
            
            insert table:exec(owner.createTable)

            def table = new PdfPTable(2)
            table.addCell("hello")
            table.addCell("world")
            table.addCell("yo")
            table.addCell("dog")
            insert table:table

            section justified: left, font: 'times', fontSize: 10, {
              text value: "This is my Main Heading", font: 'bold', fontSize: 12, newline: 'after'
              text value: "This is important.", font: 'bold'
            }
          }
          column {
            section justified: left, font: 'times', fontSize: 10, {
              text value: "This is my Main Heading", font: 'bold', fontSize: 12, newline: 'after'
              text value: "This is important.", font: 'bold'
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

        spacer height:0.25.inch
        
        section justified: left, font: 'times', fontSize: 10, {
          text value: "This is my Main Heading", font: 'bold', fontSize: 12, newline: 'after'
          text value: "This is important.", font: 'bold'
          text value: "This is where all the unimportant text follows.  It looks something like this ... asdkfasd asdf asdf asd fasdf asd f"
          text value: "This is where all the unimportant text follows.  It looks something like this ... asdkfasd asdf asdf asd fasdf asd f"
          text value: "This is where all the unimportant text follows.  It looks something like this ... asdkfasd asdf asdf asd fasdf asd f"
        }
      }

      section page: 2, at: [center, bottom + 150], width: right - center - 50, height: bottom + 72, justified: left, font: 'times', fontSize: 10, {
        text value: "This is where all the unimportant text follows.  It looks something like this ... asdkfasd asdf asdf asd fasdf asd f"
        text value: "This is where all the unimportant text follows.  It looks something like this ... asdkfasd asdf asdf asd fasdf asd f"
        text value: "This is where all the unimportant text follows.  It looks something like this ... asdkfasd asdf asdf asd fasdf asd f"
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


  private createTable = {
    PdfPTable table = new PdfPTable([1.5.inches, 1.inch, 0.5.inch, 0.5.inch] as float[])
    table.addCell("hello")
    table.addCell("world")
    table.addCell("yo")
    table.addCell("dog")
    table
  }

}