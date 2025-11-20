import cats.data.EitherNec
import cats.data.EitherNel
import cats.data.EitherNes
import cats.data.NonEmptySet
import cats.data.Validated
import cats.data.ValidatedNec
import cats.data.ValidatedNel
import cats.syntax.all.*  
import io.github.iltotore.iron.*
import io.github.iltotore.iron.cats.*
import io.github.iltotore.iron.constraint.all.*

case class User(name: String :| Alphanumeric, age: Int :| Positive)

def createUserAcc(name: String, age: Int): EitherNec[String, User] =
  (
    name.refineNec[Alphanumeric],
    age.refineNec[Positive]
  ).parMapN(User.apply)

createUserAcc("Iltotore", 18)   //Right(User(Iltotore,18))
createUserAcc("Il_totore", 18)  //Left(Chain(Should be alphanumeric))
createUserAcc("Il_totore", -18) //Left(Chain(Should be alphanumeric, Should be greater than 0))

type Username = Username.T
object Username extends RefinedType[String, Alphanumeric]{
  def apply(value: String :| Alphanumeric): Username = value.asInstanceOf[T]
}
type Age = Age.T
object Age extends RefinedType[Int,Positive]{
  def apply(value: Int :| Positive): Age = value.asInstanceOf[T]
}

case class User1(name: Username, age: Age)

def createUserAcc1(name: String, age: Int): EitherNec[String, User1] =
  (
    name.refineNec[Alphanumeric].map(Username.apply),
    age.refineNec[Positive].map(Age.apply)
  ).parMapN(User1.apply)

createUserAcc1("Iltotore", 18)   //Right(User(Iltotore,18))
createUserAcc1("Il_totore", 18)  //Left(Chain(Username should be alphanumeric))
createUserAcc1("Il_totore", -18) //Left(Chain(Username should be alphanumeric, Age should be positive))

import cats.*

import io.github.iltotore.iron.*
import io.github.iltotore.iron.cats.given
import io.github.iltotore.iron.constraint.all.*
val name1: String :| Alphanumeric = "Martin".refineUnsafe
val name2: String :| Alphanumeric = "George"
val age1: Int :| Greater[0]       = 60

name1.show // Martin
//name1 |+| name2 // MartinGeorge
age1 === 49 // false

import io.github.iltotore.iron.*
import io.github.iltotore.iron.cats.*
import io.github.iltotore.iron.constraint.all.*


type Temperature = Temperature.T

object Temperature extends RefinedType[Double,Positive]{
  
  def apply(value: Double :| Positive): Temperature = value.asInstanceOf[T]


  // def eitherNec(value: Double): EitherNec[String, Temperature] =
  //   value.refineEitherNec[Positive].map(_.asInstanceOf[T])

  // def eitherNel(value: Double): EitherNel[String, Temperature] =
  //   value.refineEitherNel[Positive].map(_.asInstanceOf[T])

  def refineNec(value: Double): EitherNec[String, Temperature] =
    value.refineNec[Positive].map(_.asInstanceOf[T])

  def refineNel(value: Double): EitherNel[String, Temperature] =
    value.refineNel[Positive].map(_.asInstanceOf[T])  

  def validated(value: Double): Validated[String, Temperature] =
    value.refineValidated[Positive].map(_.asInstanceOf[T])

  def validatedNec(value: Double): ValidatedNec[String, Temperature] =
    value.refineValidatedNec[Positive].map(_.asInstanceOf[T])

  def validatedNel(value: Double): ValidatedNel[String, Temperature] =
    value.refineValidatedNel[Positive].map(_.asInstanceOf[T])
}

// Temperature.eitherNec(-5.0)
// Temperature.eitherNel(-5.0)
Temperature.validated(-5.0)
Temperature.validatedNec(-5.0)
Temperature.validatedNel(-5.0)

type LanguageCode = LanguageCode.T

object LanguageCode extends RefinedType[String, Match["^[a-z]{2}$"]] {
  def apply(value: String :| Match["^[a-z]{2}$"]): LanguageCode = value.asInstanceOf[T]
}
type ProductName  = ProductName.T
object ProductName extends RefinedType[String,Not[Blank] & MaxLength[100]] {
  def apply(value: String :| (Not[Blank] & MaxLength[100])): ProductName = value.asInstanceOf[T]
}

final case class Translation(lang: LanguageCode, name: ProductName)

 type ProductId = ProductId.T
 object ProductId extends RefinedType[String,Match["^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$"]]{
    def apply(value: String :| Match["^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$"]): ProductId = value.asInstanceOf[T]
 }

final case class Product(id: ProductId, names: NonEmptySet[Translation])
9

val name: ProductName = ProductName("hello")

type NewUsername = NewUsername.T
object NewUsername extends RefinedType[String, MinLength[8] & Match["^[a-zA-Z0-9_]*$"]] {
  def apply(value: String :| (MinLength[8] & Match["^[a-zA-Z0-9_]*$"])): NewUsername = value.asInstanceOf[T]
}

def getUserFullName(username: NewUsername): String = username.value
getUserFullName(NewUsername("Namesmustbe8chars99more"))

opaque type NewUsername1 = String :| (MinLength[8] & Match["^[a-zA-Z0-9_]*$"])
