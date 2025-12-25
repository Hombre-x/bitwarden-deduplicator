import cats.effect.IO
import fs2.{Pipe, Pull, Stream, text}
import fs2.io.file.{Files, Path}
import fs2.data.csv.*


def loadLoginFile(pathToLoginFile: Path): Stream[IO, LoginInfo] =
  Files[IO].readAll(pathToLoginFile)
    .through(text.utf8.decode)
    .through(decodeUsingHeaders[LoginInfo]())


def deleteDuplicatedLogins(
  predicate: (LoginInfo, LoginInfo) => Boolean
): Pipe[IO, LoginInfo, LoginInfo] = data =>

  def go(current: LoginInfo, s: Stream[IO, LoginInfo], duplicatesCount: Int): Pull[IO, LoginInfo, Int] =
    s.pull.uncons1.flatMap:
      case None =>
        Pull.output1(current).as(duplicatesCount)
      case Some((next, rest)) =>
        if predicate(current, next) then go(current, rest, duplicatesCount + 1)
        else Pull.output1(current) >> go(next, rest, duplicatesCount)

  data.pull.uncons1.flatMap:
    case None => Pull.pure(0)
    case Some((head, tail)) => go(head, tail, 0)
  .flatMap(count => Pull.eval(IO.println(s"Eliminated $count duplicate(s)")))
  .stream

end deleteDuplicatedLogins


def writeLoginFile(outputPath: Path): Pipe[IO, LoginInfo, Unit] = login =>
  login
    .through(encodeUsingFirstHeaders[LoginInfo]())
    .through(Files[IO].writeUtf8(outputPath))

  


