import fs2.data.csv.{CsvRowDecoder, CsvRowEncoder, CsvRow}
import cats.data.NonEmptyList

case class LoginInfo(
  folder: String,
  favorite: String,
  `type`: String,
  name: String,
  notes: String,
  fields: String,
  reprompt: Int,
  uri: String,
  username: String,
  password: String,
  totp: String
)

object LoginInfo:
  given CsvRowDecoder[LoginInfo, String] = row =>
    for
      folder <- row.as[String]("folder").orElse(Right(""))
      favorite <- row.as[String]("favorite").orElse(Right(""))
      typeField <- row.as[String]("type").orElse(Right(""))
      name <- row.as[String]("name").orElse(Right(""))
      notes <- row.as[String]("notes").orElse(Right(""))
      fields <- row.as[String]("fields").orElse(Right(""))
      reprompt <- row.as[Int]("reprompt").orElse(Right(0))
      uri <- row.as[String]("login_uri").orElse(Right(""))
      username <- row.as[String]("login_username").orElse(Right(""))
      password <- row.as[String]("login_password").orElse(Right(""))
      totp <- row.as[String]("login_totp").orElse(Right(""))
    yield LoginInfo(folder, favorite, typeField, name, notes, fields, reprompt, uri, username, password, totp)

  given CsvRowEncoder[LoginInfo, String] = (login: LoginInfo) =>
    CsvRow.fromNelHeaders(
      NonEmptyList.of(
        "folder" -> login.folder,
        "favorite" -> login.favorite.toString,
        "type" -> login.`type`,
        "name" -> login.name,
        "notes" -> login.notes,
        "fields" -> login.fields,
        "reprompt" -> login.reprompt.toString,
        "login_uri" -> login.uri,
        "login_username" -> login.username,
        "login_password" -> login.password,
        "login_totp" -> login.totp
      )
    )



