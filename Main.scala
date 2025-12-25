import cats.syntax.all.*
import cats.effect.{ExitCode, IO, IOApp}
import fs2.{Stream, text}
import fs2.io.file.{Files, Path}

object Main extends IOApp:

  private val filterPredicate = (curr: LoginInfo, next: LoginInfo) =>
    curr.name == next.name &&
    curr.username == next.username &&
    curr.password == next.password &&
    curr.uri == next.uri

  override def run(args: List[String]): IO[ExitCode] =
    args match
      case path :: _ =>
        val inputPath = Path(path)
        val fileName = inputPath.fileName
        val curatedDir = inputPath.parent.fold(Path("curated"))(_.resolve("curated"))
        val outputPath = curatedDir.resolve(fileName)

        Files[IO].createDirectories(curatedDir) >>
          loadLoginFile(inputPath)
            .through(deleteDuplicatedLogins(filterPredicate))
            .evalTap(login => IO.println(login.name))
            .through(writeLoginFile(outputPath))
            .compile
            .drain
            .as(ExitCode.Success)
      case _ => ExitCode.Error.pure[IO]