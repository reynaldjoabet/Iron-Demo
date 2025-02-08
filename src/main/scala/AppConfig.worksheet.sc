import com.typesafe.config.ConfigFactory
import io.github.iltotore.iron.*
import io.github.iltotore.iron.constraint.all.*
import pureconfig.*
import pureconfig.generic.derivation.default.*

opaque type NonEmptyString = String :| Not[Blank]
opaque type DatabaseUrl = String :|
  Match["""(\b(https?|ftp|file)://)?[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|]"""]
type PortNumber = Int :| Interval.Closed[1, 65535]

given ConfigReader[NonEmptyString] =
  ConfigReader.fromString[NonEmptyString](ConvertHelpers.optF(_.refineOption))
given ConfigReader[DatabaseUrl] =
  ConfigReader.fromString[DatabaseUrl](ConvertHelpers.optF(_.refineOption))
given ConfigReader[PortNumber] =
  ConfigReader.fromString[PortNumber](ConvertHelpers.optF(_.toIntOption.flatMap(_.refineOption)))

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

hello("hello")

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
  override inline def test(value: Int): Boolean = value > 0
  override inline def message: String           = "Should be strictly positive"
}

val x1: Int :| Positive = 1

// val y1: Int :| Positive =- 1

// val goo: Int :| (Positive & Less[42]) = 178

//constrained opaque types

opaque type Positives <: Int = Int :| Greater[0] // has all the methods on Int

opaque type Positives1 = Int :| Greater[0]
object Positives extends RefinedTypeOps[Int, Greater[0], Positives]

val g: Positives = 10

val m: Int = g

val g1: Positives1 = 10

g == g1

//val m1:Positives1= -10

private type StatsConstraint =
  GreaterEqual[0] // & LessEqual[100000000 * 21000000]

opaque type Stats <: Long = Long :| StatsConstraint

object Stats extends RefinedTypeOps[Long, StatsConstraint, Stats]

100000000L * 21000000L

final case class Tag(name: Tag.Name, value: Tag.Value)

object Tag {

  private type NameConstraint = Not[Empty] & MaxLength[128]
  opaque type Name <: String  = String :| NameConstraint

  object Name extends RefinedTypeOps[String, NameConstraint, Name]

  private type ValueConstraint = Not[Empty] & MaxLength[512]
  opaque type Value <: String  = String :| ValueConstraint

  object Value extends RefinedTypeOps[String, ValueConstraint, Value]
}
