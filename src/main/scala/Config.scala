import java.nio.file.Paths

trait Config {
  val datFilePath = Paths.get(System.getProperty("user.home")).resolve("AppData/Roaming/6Wunderkinder/Wunderlist/Wunderlist.dat")
  val todoDirPath = Paths.get(System.getProperty("user.home")).resolve("tmp/todo")

  def getListTags(listTitle: String): (Option[Priority], List[Tag]) = listTitle match {
    // Add cases for each of your lists here, e.g.
    //case "Inbox" => (None, Nil)
    //case "Groceries" => (Some(Priority('A')), List(Context("groceries")))
    //case "Japanese" => (Some(Priority('B')), List(Project("japanese")))

    // Or uncomment this catch-all case to just get things working
    //case _ => (None, Nil)

    case _ => sys.error(s"Unknown list title ($listTitle). Please add it to the getListTags(String) method in Config.scala.")
  }
}
