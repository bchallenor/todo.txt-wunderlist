import java.nio.file.Paths

trait Config {
  val datFilePath = Paths.get(System.getProperty("user.home")).resolve("AppData/Roaming/6Wunderkinder/Wunderlist/Wunderlist.dat")
  val todoDirPath = Paths.get(System.getProperty("user.home")).resolve("tmp/todo")

  def getListTags(listTitle: String): (Option[Priority], List[Tag]) = listTitle match {
    case "Inbox" => (None, Nil)

    // e.g.
    //case "Groceries" => (Some(Priority('A')), List(Context("groceries")))
    //case "Japanese" => (Some(Priority('B')), List(Project("japanese")))

    case _ => sys.error(s"Unknown list title ($listTitle). Please add it to the getListTags(String) method in Config.scala.")
  }
}
