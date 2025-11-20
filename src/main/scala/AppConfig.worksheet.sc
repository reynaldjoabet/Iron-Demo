import java.time.LocalDate

import com.typesafe.config.ConfigFactory
import io.github.iltotore.iron.*
import io.github.iltotore.iron.constraint.all.*
import io.github.iltotore.iron.constraint.string.*
import pureconfig.*
import pureconfig.generic.derivation.default.*
type NonEmptyString = NonEmptyString.T
object NonEmptyString extends RefinedType[String, Not[Blank]] {
  def apply(value: String :| Not[Blank]): NonEmptyString = value.asInstanceOf[T]
}

type DatabaseUrl = DatabaseUrl.T
object DatabaseUrl
    extends RefinedType[String, Match[
      """(\b(https?|ftp|file)://)?[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|]"""
    ]] {
  def apply(
      value: String :|
        Match["""(\b(https?|ftp|file)://)?[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|]"""]
  ): DatabaseUrl = value.asInstanceOf[T]
}
type PortNumber = PortNumber.T
object PortNumber extends RefinedType[Int, Interval.Closed[1, 65535]] {
  def apply(value: Int :| Interval.Closed[1, 65535]): PortNumber = value.asInstanceOf[T]
}
given ConfigReader[NonEmptyString] =
  ConfigReader.fromString[NonEmptyString](ConvertHelpers.optF(NonEmptyString.option(_)))
given ConfigReader[DatabaseUrl] =
  ConfigReader.fromString[DatabaseUrl](ConvertHelpers.optF(DatabaseUrl.option(_)))
given ConfigReader[PortNumber] =
  ConfigReader.fromString[PortNumber](ConvertHelpers.optF(port => PortNumber.option(port.toInt)))


final case class ApiConfig(host: NonEmptyString, port: PortNumber) derives ConfigReader

final case class DatabaseConfig(
    driver: NonEmptyString,
    url: DatabaseUrl,
    user: NonEmptyString,
    pass: NonEmptyString
) derives ConfigReader

val apiConfig = ConfigFactory.parseString(s"""api{"host":"api.example.com","port":1234}""")
ConfigSource.fromConfig(apiConfig).at("api").load[ApiConfig]
// Right(ApiConfig(api.example.com,1234))

val databaseConfig = ConfigFactory.parseString(
  s"""database {
     |  "driver": "org.postgresql.Driver",
     |  "url": "jdbc:postgresql://localhost:5422/test-database",
     |  "user": "pure",
     |  "pass": "password"
     |}""".stripMargin
)
ConfigSource.fromConfig(databaseConfig).at("database").load[DatabaseConfig]

def hello(s: NonEmptyString) = 7

import io.github.iltotore.iron.compileTime

hello(NonEmptyString("world"))

// trait Repository[F[_]]{

//   def loadProduct(id: ProductId): F[Seq[(ProductId, LanguageCode, ProductName)]]

//   def loadProducts(): Stream[F, (ProductId, LanguageCode, ProductName)]

//   def saveProduct(p: Product): F[Int]

//   def updateProduct(p: Product): F[Int]
// }

//Refined types are subtypes of their unrefined form. For instance, Int :| Greater[0] is a subtype of Int

val x: Int :| Greater[0] = 9
val y: Int               = x //Compiles

import io.github.iltotore.iron.*
import io.github.iltotore.iron.cats
import io.github.iltotore.iron.circe
import io.github.iltotore.iron.compileTime
import io.github.iltotore.iron.constraint
import io.github.iltotore.iron.constraint.any
import io.github.iltotore.iron.constraint.char
import io.github.iltotore.iron.constraint.collection
import io.github.iltotore.iron.constraint.numeric
import io.github.iltotore.iron.constraint.string
import io.github.iltotore.iron.doobie
import io.github.iltotore.iron.internal
import io.github.iltotore.iron.skunk

final class Positive

given Constraint[Int, Positive] with {

  override inline def test(inline value: Int): Boolean = value > 0
  override inline def message: String                  = "Should be strictly positive"
}

val x1: Int :| Positive = 1.refineOption[Positive].get

// val y1: Int :| Positive =- 1

// val goo: Int :| (Positive & Less[42]) = 178

//constrained opaque types

type Positives1 = Positives.T
object Positives extends RefinedType[Int, Greater[0]]

val g1: Positives1 = Positives(10)

import io.github.iltotore.iron.compileTime.*
//val mn: Positives1 = 89

//val m1:Positives1= -10

private type StatsConstraint =
  GreaterEqual[0] // & LessEqual[100000000 * 21000000]

type Stats = Stats.T

object Stats extends RefinedType[Long, StatsConstraint]

100000000L * 21000000L

final case class Tag(name: Tag.Name, value: Tag.Value)

object Tag {

  private type NameConstraint = Not[Empty] & MaxLength[128]
  opaque type Name <: String  = String :| NameConstraint

  object Name extends RefinedType[String, NameConstraint]

  private type ValueConstraint = Not[Empty] & MaxLength[512]
  opaque type Value <: String  = String :| ValueConstraint

  object Value extends RefinedType[String, ValueConstraint]
}
