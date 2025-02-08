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

opaque type Username = DescribedAs[Alphanumeric, "Username should be alphanumeric"]

opaque type Age = DescribedAs[Positive, "Age should be positive"]

case class User1(name: String :| Username, age: Int :| Age)

def createUserAcc1(name: String, age: Int): EitherNec[String, User1] =
  (
    name.refineNec[Username],
    age.refineNec[Age]
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

opaque type Temperature = Double :| Positive

//object Temperature extends RefinedTypeOps[Temperature]

// Temperature.eitherNec(-5.0)
// Temperature.eitherNel(-5.0)
// Temperature.validated(-5.0)
// Temperature.validatedNec(-5.0)
// Temperature.validatedNel(-5.0)

opaque type LanguageCode = String :| Match["^[a-z]{2}$"]
opaque type ProductName  = String :| Not[Blank]

final case class Translation(lang: LanguageCode, name: ProductName)

opaque type ProductId = String :|
  Match["^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$"]

final case class Product(id: ProductId, names: NonEmptySet[Translation])
9

val name: ProductName = "hello"

opaque type NewUsername = String :| MinLength[5]

def getUserFullName(username: NewUsername): String = username
getUserFullName("Names")

opaque type NewUsername1 = String :| (MinLength[8] & Match["^[a-zA-Z0-9_]*$"])
