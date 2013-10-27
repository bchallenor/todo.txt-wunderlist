import java.io.{FileWriter, FileInputStream}
import java.nio.file.{Path, Paths}
import java.util.zip.GZIPInputStream
import scala.xml.{NodeSeq, XML}
import org.joda.time._
import org.joda.time.format._

object Program extends App with Config {
  val xml = {
    val fis = new FileInputStream(datFilePath.toFile)
    try {
      val gis = new GZIPInputStream(fis)
      XML.load(gis)
    } finally fis.close()
  }

  val dateTimeFormatters = List(ISODateTimeFormat.dateTime(), ISODateTimeFormat.dateTimeNoMillis())
  def parseDateTimeOrNone(node: NodeSeq): Option[DateTime] = {
    val text = node.text
    if (text.isEmpty) {
      None
    } else {
      val dateTimeOrNone = (dateTimeFormatters collectFirst Function.unlift(f => try Some(f.parseDateTime(text)) catch { case _: IllegalArgumentException => None })).headOption
      if (dateTimeOrNone.isEmpty) throw new IllegalArgumentException(s"No formats matched $text")
      dateTimeOrNone
    }
  }

  def toLocalDateInDefaultZone(dateTime: DateTime): LocalDate = new LocalDate(dateTime.getMillis, DateTimeZone.getDefault)

  // Wunderlist is pretty sloppy with local dates (used for due dates and reminders).
  // Sometimes it uses a timezone, sometimes it does not.
  // As we only need the date part though, we can just ignore the rest.
  def parseLocalDateOrNone(node: NodeSeq): Option[LocalDate] = {
    val text = node.text
    if (text.isEmpty) {
      None
    } else {
      val timeIdx: Int = text.indexOf('T')
      if (timeIdx < 0) throw new IllegalArgumentException(s"Expected a time part (even though it will be ignored)")
      Some(ISODateTimeFormat.localDateParser().parseLocalDate(text.substring(0, timeIdx)))
    }
  }

  def getTextLines(node: NodeSeq): List[String] = {
    val text = node.text
    if (text.isEmpty) Nil else text.split("\n").toList.filter(_.nonEmpty)
  }

  def getTaskTitles(node: NodeSeq): List[String] = {
    (for {
      taskNode <- node \ "Task"
      if parseDateTimeOrNone(taskNode \ "DeletedAt").isEmpty
      if parseDateTimeOrNone(taskNode \ "CompletedAt").isEmpty
    } yield {
      (taskNode \ "Title").text
    }).toList
  }

  val tasks = for {
    taskListNode <- xml \\ "TaskList"
    if parseDateTimeOrNone(taskListNode \ "DeletedAt").isEmpty
    taskNode <- taskListNode \ "Tasks" \ "Task"
    if parseDateTimeOrNone(taskNode \ "DeletedAt").isEmpty
  } yield {
    val listTitle = (taskListNode \ "Title").text
    val (priorityOrNone, listTags) = getListTags(listTitle)
    val taskTitle = (taskNode \ "Title").text

    val createdInstant = parseDateTimeOrNone(taskNode \ "CreatedAt") getOrElse sys.error("Expected CreatedAt")
    val createdDate = toLocalDateInDefaultZone(createdInstant)

    val completedInstantOrNone = parseDateTimeOrNone(taskNode \ "CompletedAt")
    val completedDateOrNone = completedInstantOrNone map toLocalDateInDefaultZone

    val startDateOrNone = parseLocalDateOrNone(taskNode \ "Reminders" \ "Reminder" \ "Date")
    val startTagOrNone = startDateOrNone map Start

    val dueDateOrNone = parseLocalDateOrNone(taskNode \ "DueDate")
    val dueTagOrNone = dueDateOrNone map Due

    val starred = (taskNode \ "Starred").text.toBoolean
    val starredTagOrNone = if (starred) Some(Project("starred")) else None

    val noteLines = getTextLines(taskNode \ "Note")
    val noteTagOrNone = if (noteLines.nonEmpty) Some(Note(noteLines)) else None

    val subtaskTitles = getTaskTitles(taskNode \ "Subtasks")
    val subtasksTagOrNone = if (subtaskTitles.nonEmpty) Some(Subtasks(subtaskTitles)) else None

    TaskItem(taskTitle, createdDate, priorityOrNone filter (_ => !completedDateOrNone.isDefined), completedDateOrNone, noteTagOrNone.toList ++ subtasksTagOrNone ++ listTags ++ startTagOrNone ++ dueTagOrNone ++ starredTagOrNone)
  }

  def printSorted(filePath: Path, completed: Boolean) {
    val fw = new FileWriter(filePath.toFile)
    try {
      tasks.filter(_.completedDateOrNone.isDefined == completed).map(_.toString).sorted foreach { line =>
        fw.write(line)
        fw.write("\n")
      }
    } finally fw.close()
  }

  printSorted(todoDirPath.resolve("todo.txt"), completed = false)
  printSorted(todoDirPath.resolve("done.txt"), completed = true)
}
