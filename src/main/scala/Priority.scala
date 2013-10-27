case class Priority(code: Char) {
  require(code >= 'A' && code <= 'Z')
  override def toString = "(" + code + ")"
}
