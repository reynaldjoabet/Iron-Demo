import cats.*
import cats.data.*
import cats.data.Chain
import cats.data.EitherNel
import cats.data.NonEmptyChain
import cats.data.Validated.*
import cats.data.ValidatedNec
import cats.syntax.all.*
import cats.syntax.apply.*
import cats.syntax.parallel.*
import cats.NonEmptyParallel
import pureconfig.ConvertHelpers.optF
import pureconfig.error.{CannotConvert, ConfigReaderFailures, ConvertFailure, FailureReason}
import pureconfig.{ConfigConvert, ConfigReader}
//import io.github.iltotore.iron.pureconfig
import cats.Parallel

import io.github.iltotore.iron.*
import io.github.iltotore.iron.constraint.numeric.Interval.Closed

//import org.http4s.QueryParamDecoder
object Types {
  type PartsNum = PartsNum.T
  object PartsNum extends RefinedType[Int, Closed[2, 5]] {}
  type Theme = Theme.T
  object Theme extends RefinedType[String, Pure]

  type Language = Language.T
  object Language extends RefinedType[String, Pure]

  type Idea = Idea.T
  object Idea extends RefinedType[List[Theme], Pure]
}

object Main extends App {

//Many changes related to `RefinedTypeOps` definition have been introduced to provide better ergonomy.
// object PartsNum extends RefinedType[Int, Closed[2, 5], PartsNum] {
// //   given QueryParamDecoder[PartsNum] =
// //     QueryParamDecoder.fromUnsafeCast[PartsNum](p => PartsNum.applyUnsafe(p.value.toInt))("PartsNum")
// // }

  import Types.*

  println("Hello, World!")

//   import _root_.pureconfig.ConfigReader
//   import _root_.pureconfig.generic.derivation.default.*
//   import io.github.iltotore.iron.constraint.all.*
//   import io.github.iltotore.iron.pureconfig.given
//   opaque type Username = String :| MinLength[5]
//   object Username extends RefinedTypeOps[String, MinLength[5], Username]
//   case class IronTypeConfig(
//       username: String :| MinLength[5]
//   ) derives ConfigReader
//   case class NewTypeConfig(
//       username: Username
//   ) derives ConfigReader

  val nec: NonEmptyChain[Int] = NonEmptyChain(1, (2, 3, 4).toList: _*) // NonEmptyChain(1, Chain(2, 3, 4))

  case class Account(username: String, email: String, password: String)

  object Account {

    def validateUsername(username: String): ValidatedNec[String, String] =
      Validated
        .condNec(username.matches("^[a-zA-Z0-9]+"), username, s"$username should be alphanumeric")

    def validateEmail(email: String): ValidatedNec[String, String] =
      Validated
        .condNec(email.matches("^[]+@([-]+)+[-]{2,4}$"), email, s"$email should be a valid email")

    def validatePassword(password: String): ValidatedNec[String, String] =
      Validated.condNec(
        password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*/d)[a-zA-Z/d]+$"),
        password,
        s"$password should contain at least an upper, a lower and a number"
      )

    def createAccount(
        username: String,
        email: String,
        password: String
    ): ValidatedNec[String, Account] = (
      validateUsername(username),
      validateEmail(email),
      validatePassword(password)
    ).mapN(Account.apply) // .parMapN(Account.apply)

  }

  // Valid(Account(...))
  Account.createAccount("Iltotore", "thisismymail@gmail.com", "SafePassword1")

  // Invalid(NonEmptyChain("Value should be alphanumeric")))
  Account.createAccount("Il_totore", "thisismymail@gmail.com", "SafePassword1")

  /*
   * Invalid(NonEmptyChain(
   *   "Value should be alphanumeric"),
   *   "Value must contain at least an upper, a lower and a number")
   * ))
   */
  Account.createAccount("Il_totore", "thisismymail@gmail.com", "this_is_not_fine")

}