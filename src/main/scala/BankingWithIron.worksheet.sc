import java.time.LocalDate

import io.github.iltotore.iron.*
import io.github.iltotore.iron.cats.given
import io.github.iltotore.iron.constraint.numeric.*
import io.github.iltotore.iron.constraint.string.Match

// Define the CardType enum
enum CardType {
  case Debit, Credit
}

// Define a case class for Card with validation
case class Card(
    cardNumber: CardNumber,
    cardType: CardType,
    expirationDate: LocalDate,
    account: Account
)

type CardNumber = CardNumber.T
object CardNumber extends RefinedType[String, Match["\\d{4}-\\d{4}-\\d{4}-\\d{4}"]] {
  def apply(value: String :| Match["\\d{4}-\\d{4}-\\d{4}-\\d{4}"]): CardNumber = value
    .asInstanceOf[T]
}
// Define the AccountType enum
enum AccountType {
  case Savings, Checking, Business
}

type Amount = Amount.T

object Amount extends RefinedType[Int, GreaterEqual[0]] {
  def apply(value: Int :| GreaterEqual[0]): Amount = value.asInstanceOf[T]

  extension (amount: Amount) {

    // addition and subtraction operations
    def +(other: Amount): Amount = (amount.value + other.value).asInstanceOf[T]
    def -(other: Amount): Amount = (amount.value - other.value).asInstanceOf[T]

    def >(other: Amount): Boolean = amount.value > other.value

    def <(other: Amount): Boolean = amount.value < other.value
  }
}
// Define a case class for Customer
case class Customer(id: String, name: String)

// Define a case class for Account with validation
case class Account(
    id: String,
    customer: Customer,
    accountType: AccountType,
    balance: Amount,
    cards: List[Card] = List.empty // Default empty list of cards
)

// Define a sealed trait and case classes for Transaction
sealed trait Transaction {
  def id: String
  def fromAccount: Account
  def toAccount: Account
  def amount: Amount
}

case class Deposit(id: String, toAccount: Account, amount: Amount) extends Transaction {
  def fromAccount: Account = Account("", Customer("", ""), AccountType.Savings, amount, List.empty)
}

case class Withdrawal(id: String, fromAccount: Account, amount: Amount) extends Transaction {
  def toAccount: Account = Account("", Customer("", ""), AccountType.Savings, amount)
}

case class Transfer(id: String, fromAccount: Account, toAccount: Account, amount: Amount)
    extends Transaction {}

object BankService {

  def deposit(account: Account, amount: Amount): Account = {
    val balance: Amount = (account.balance + amount).asInstanceOf[Amount]
    account.copy(balance = balance)
  }

  def withdraw(account: Account, amount: Amount): Either[String, Account] =
    if amount > account.balance then Left("Insufficient funds")
    else Right(account.copy(balance = (account.balance - amount).asInstanceOf[Amount]))

  def transfer(
      fromAccount: Account,
      toAccount: Account,
      amount: Amount
  ): Either[String, (Account, Account)] =
    withdraw(fromAccount, amount).map { updatedFromAccount =>
      val updatedToAccount = deposit(toAccount, amount)
      (updatedFromAccount, updatedToAccount)
    }

  def addCard(account: Account, card: Card): Account =
    account.copy(cards = card :: account.cards)
}

import BankService._

//@main def runBankingDemo() {
val customer1 = Customer("1", "Alice")
val customer2 = Customer("2", "Bob")

val account1 = Account("acc1", customer1, AccountType.Checking, Amount(1000))
val account2 = Account("acc2", customer2, AccountType.Savings, Amount(500))

// Deposit money into account1
val updatedAccount1 = BankService.deposit(account1, Amount(200))
println(s"Updated balance of account1: ${updatedAccount1.balance}")

// Withdraw money from account2
BankService.withdraw(account2, Amount(100)) match {
  case Right(updatedAccount2) => println(s"Updated balance of account2: ${updatedAccount2.balance}")
  case Left(error)            => println(s"Error: $error")
}

// Transfer money from account1 to account2
BankService.transfer(updatedAccount1, account2, Amount(300)) match {
  case Right((fromAccount, toAccount)) =>
    print("Updated balance of fromAccount: ")
  case Left(error) => println(s"Error: $error")
}

// Create a new card
val cardExpiration = LocalDate.of(2025, 12, 31)
val newCard = Card(
  cardNumber = CardNumber("1234-5678-9876-5432"),
  cardType = CardType.Debit,
  expirationDate = cardExpiration,
  account = account1
)

// Add the card to the account
val updatedAccount12 = BankService.addCard(account1, newCard)

// Print account details
println(s"Updated account details: $updatedAccount1")

import io.github.iltotore.iron.constraint.collection.*

object U {
  type UserName = UserName.T
  object UserName extends RefinedType[String, MinLength[10]]
}
object P {
  type Password = Password.T
  object Password extends RefinedType[String, MinLength[10]]
}

val p: P.Password = P.Password("foobar2000") // ok
//val u: U.UserName = p // error: type mismatch due to opaque types

p

Amount.either(98)
