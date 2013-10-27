import org.joda.time.LocalDate

sealed trait Tag

case class Context(name: String) extends Tag {
  require(name forall (c => c >= 'a' && c <= 'z'))
  override def toString = "@" + name
}

case class Project(name: String) extends Tag {
  require(name forall (c => c >= 'a' && c <= 'z'))
  override def toString = "+" + name
}

case class Start(date: LocalDate) extends Tag {
  override def toString = "t:" + date
}

case class Due(date: LocalDate) extends Tag {
  override def toString = "due:" + date
}

case class Note(lines: List[String]) extends Tag {
  require(lines.nonEmpty && lines.forall(_.nonEmpty))
  override def toString = "note:" + lines.mkString("[", "; ", "]")
}

case class Subtasks(titles: List[String]) extends Tag {
  require(titles.nonEmpty && titles.forall(_.nonEmpty))
  override def toString = "sub:" + titles.mkString("[", "; ", "]")
}
