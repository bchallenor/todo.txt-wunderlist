import org.joda.time.LocalDate

sealed case class TaskItem(title: String, createdDate: LocalDate, priorityOrNone: Option[Priority], completedDateOrNone: Option[LocalDate], tags: List[Tag]) {
  require(!completedDateOrNone.isDefined || !priorityOrNone.isDefined, "Cannot have a priority if completed")

  override def toString: String = {
    List[Iterable[String]](
      completedDateOrNone map (completedDate => List("x", completedDate.toString)) getOrElse Nil,
      priorityOrNone map (_.toString),
      List(createdDate.toString),
      Some(title),
      tags map (_.toString)
    ).flatten mkString " "
  }
}
