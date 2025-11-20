import io.github.iltotore.iron.*
import io.github.iltotore.iron.constraint.numeric.*

import io.github.iltotore.iron.constraint.numeric.Positive
def log(x: Double :| Positive): Double =
  Math.log(x) // Used like a normal `Double`

log(1.0) //Automatically verified at compile time.
//log(-1.0) //Compile-time error: Should be strictly positive

val runtimeValue: Double = 90.8
log(runtimeValue.refineUnsafe) //Explicitly refine your external values at runtime.

//runtimeValue.refineEither.map(log)           //Use monadic style for functional validation
runtimeValue.refineEither[Positive].map(log) //More explicitly

case class IBAN(
    countryCode: String,
    checkDigits: String,
    bankCode: String,
    branchCode: String,
    accountNumber: String,
    nationalCheckDigit: String
)

val iban = IBAN(
  "FR",
  "14",
  "20041",
  "01005",
  "0500013M026",
  "06"
)

//bad example
val shuffled = IBAN(
  "0500013M026",
  "FR",
  "06",
  "14",
  "20041",
  "01005"
)

val bad = IBAN(
  "🇫🇷",
  "✅",
  "🏦",
  "🌳",
  "🧾",
  "🤡"
)

opaque type BranchCode <: String = String
object BranchCode {
  inline def wrap(input: String): BranchCode = input

  extension (value: BranchCode) {
    inline def unwrap: String = value
  }
  // def parse(input: String): Either[FormatError, BranchCode] =
  //   Either.cond(input.length == 5, wrap(input),
  //     FormatError("Branch code must be 5 chars"))
}

val km: BranchCode = "hello"

import io.github.iltotore.iron.constraint.collection
import io.github.iltotore.iron.constraint.collection.*
// def createIBAN(  countryCode: String,
//                  checkDigits: String,
//                  bankCode: String,
//                  branchCode: String,
//                  accountNumber: String,
//                  nationalCheckDigit: String
//               ): Either[String, User] =
//   for
//     ctr <- countryCode.refineEither[Alphanumeric & Length[Equals[2]]]
//     chk <- checkDigits.refineEither[Alphanumeric & Length[Equals[2]]]
//     ban <- bankCode.refineEither[Alphanumeric & Length[Equals[5]]]
//     bra <- branchCode.refineEither[Alphanumeric & Length[Equals[5]]]
//     acc <- accountNumber.refineEither[Alphanumeric & Length[Equals[11]]]
//     nck <- nationalCheckDigit.refineEither[Alphanumeric & Length[Equals[2]]]
//   yield IBAN(ctr, chk, ban, bra, acc, nck)
import io.github.iltotore.iron.constraint.numeric
import io.github.iltotore.iron.constraint.string.Alphanumeric
import io.github.iltotore.iron.constraint.string.Blank
import io.github.iltotore.iron.constraint.string.EndWith
import io.github.iltotore.iron.constraint.string.LettersLowerCase
import io.github.iltotore.iron.constraint.string.Match.apply
import io.github.iltotore.iron.constraint.string.ValidURL
import io.github.iltotore.iron.constraint.string.ValidUUID

object U {
  type UserName = UserName.T
  object UserName extends RefinedType[String, MinLength[10]] {
    def apply(value: String :| MinLength[10]): UserName = value.asInstanceOf[T]
  }
}
object P {
  // creates a new type which is different from UserName
  type Password = Password.T
  object Password extends RefinedType[String, MinLength[10]] {
    def apply(value: String :| MinLength[10]): Password = value.asInstanceOf[T]
  }
}

val p: P.Password = P.Password("foobar2000") // ok
//val u: U.UserName = p // does not compile!!

// import cats.data.NonEmptyList

// import io.github.iltotore.iron.*
// import io.github.iltotore.iron.constraint.all.*
// import io.github.iltotore.iron.circe.given

// import io.circe.Codec
// import io.circe.syntax.*
// import io.circe.parser.*

// case class OrderLine(product: String :| Not[Empty], quantity: Int :| Positive) derives Codec.AsObject

// type UUIDConstraint = DescribedAs[Match["^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$"], "Should be an UUID"]

// case class Order(orderId: String :| UUIDConstraint, lines: NonEmptyList[OrderLine]) derives Codec.AsObject

// val orderLines = NonEmptyList.of(
//   OrderLine("100", 10),
//   OrderLine("101", 5)
// )

// val order = Order(
//   "c93dd655-caca-4c99-aad8-3d91ad7a1b7e",
//   orderLines
// )

// println(order.asJson.toString)

// val json = """
// {
//   "orderId" : "c93dd655-caca-4c99-aad8-3d91ad7a1b7e",
//   "lines" : [
//     {
//       "product" : "100",
//       "quantity" : 10
//     },
//     {
//       "product" : "101",
//       "quantity" : 5
//     }
//   ]
// }
// """
// decode[Order](json)

// val invalidJson = """
// {
//   "orderId" : "not-an-uuid",
//   "lines" : [
//     {
//       "product" : "100",
//       "quantity" : 10
//     },
//     {
//       "product" : "101",
//       "quantity" : 5
//     }
//   ]
// }
// """
// println(decode[Order](invalidJson))
