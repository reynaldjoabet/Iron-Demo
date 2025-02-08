import cats.data.Chain
import cats.*
import cats.data.EitherNel
import cats.syntax.all.*
import cats.data.*
import cats.data.Validated.*
import cats.data.ValidatedNec
import cats.NonEmptyParallel
import cats.data.Validated.*
import cats.data.NonEmptyChain

import cats.Parallel
import cats.data.EitherNel
import cats.syntax.apply.*
import cats.syntax.parallel.*
object Main extends App {

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
      Validated.condNec(username.matches("^[a-zA-Z0-9]+"), username, s"$username should be alphanumeric")

    def validateEmail(email: String): ValidatedNec[String, String] =
      Validated.condNec(email.matches("^[]+@([-]+)+[-]{2,4}$"), email, s"$email should be a valid email")

    def validatePassword(password: String): ValidatedNec[String, String] =
      Validated.condNec(password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*/d)[a-zA-Z/d]+$"), password, s"$password should contain at least an upper, a lower and a number")

    def createAccount(username: String, email: String, password: String): ValidatedNec[String, Account] = (
      validateUsername(username),
      validateEmail(email),
      validatePassword(password)
    ).mapN(Account.apply)//.parMapN(Account.apply)
  }


  //Valid(Account(...))
  Account.createAccount("Iltotore", "thisismymail@gmail.com", "SafePassword1")

  //Invalid(NonEmptyChain("Value should be alphanumeric")))
  Account.createAccount("Il_totore", "thisismymail@gmail.com", "SafePassword1")

  /* 
   * Invalid(NonEmptyChain(
   *   "Value should be alphanumeric"),
   *   "Value must contain at least an upper, a lower and a number")
   * ))
   */
  Account.createAccount("Il_totore", "thisismymail@gmail.com", "this_is_not_fine")


import cats.data.NonEmptyList
import cats.instances.parallel.*

import io.github.iltotore.iron.*
import io.github.iltotore.iron.constraint.all.*
import io.github.iltotore.iron.circe.given

import io.circe.Codec
import io.circe.syntax.*
//import io.circe.parser.*

case class OrderLine(product: String :| Not[Empty], quantity: Int :| Positive) derives Codec.AsObject

//case class Order(orderId: String :| UUIDConstraint, lines: NonEmptyList[OrderLine]) derives Codec.AsObject
}
