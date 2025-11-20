// Define the AccountType enum
import java.time.LocalDate

// Define the CardType enum
enum CardType {
  case Debit, Credit
}

// Define a case class for Card
case class Card(
    cardNumber: String,
    cardType: CardType,
    expirationDate: LocalDate,
    account: Account
)
enum AccountType {
  case Savings, Checking, Business
}
// Define a case class for Customer
case class Customer(id: String, name: String)

// Define a case class for Account
//case class Account(id: String, customer: Customer, accountType: AccountType, balance: BigDecimal)
case class Account(
    id: String,
    customer: Customer,
    accountType: AccountType,
    balance: BigDecimal,
    cards: List[Card] = List() // Default empty list of cards
)

// Define a sealed trait and case classes for Transaction
sealed trait Transaction {
  def id: String
  def fromAccount: Account
  def toAccount: Account
  def amount: BigDecimal
}
case class Deposit(id: String, toAccount: Account, amount: BigDecimal) extends Transaction {
  def fromAccount: Account = Account("", Customer("", ""), AccountType.Savings, 0)
}
case class Withdrawal(id: String, fromAccount: Account, amount: BigDecimal) extends Transaction {
  def toAccount: Account = Account("", Customer("", ""), AccountType.Savings, 0)
}
case class Transfer(id: String, fromAccount: Account, toAccount: Account, amount: BigDecimal)
    extends Transaction {}

object BankService {

  def deposit(account: Account, amount: BigDecimal): Account =
    account.copy(balance = account.balance + amount)

  def withdraw(account: Account, amount: BigDecimal): Either[String, Account] =
    if amount > account.balance then Left("Insufficient funds")
    else Right(account.copy(balance = account.balance - amount))

  def transfer(
      fromAccount: Account,
      toAccount: Account,
      amount: BigDecimal
  ): Either[String, (Account, Account)] =
    withdraw(fromAccount, amount).map { updatedFromAccount =>
      val updatedToAccount = deposit(toAccount, amount)
      (updatedFromAccount, updatedToAccount)
    }
  def addCard(account: Account, card: Card): Account =
    account.copy(cards = card :: account.cards)
}
import BankService.*
//@main def runBankingDemo() {
val customer1 = Customer("1", "Alice")
val customer2 = Customer("2", "Bob")

val account1 = Account("acc1", customer1, AccountType.Checking, BigDecimal(1000))
val account2 = Account("acc2", customer2, AccountType.Savings, BigDecimal(500))

// Deposit money into account1
val updatedAccount1 = BankService.deposit(account1, BigDecimal(200))
println(s"Updated balance of account1: ${updatedAccount1.balance}")

// Withdraw money from account2
BankService.withdraw(account2, BigDecimal(100)) match {
  case Right(updatedAccount2) => println(s"Updated balance of account2: ${updatedAccount2.balance}")
  case Left(error)            => println(s"Error: $error")
}

// Transfer money from account1 to account2
BankService.transfer(updatedAccount1, account2, BigDecimal(300)) match {
  case Right((fromAccount, toAccount)) =>
    println(s"Updated balance of fromAccount: ${fromAccount.balance}")
    println(s"Updated balance of toAccount: ${toAccount.balance}")
  case Left(error) => println(s"Error: $error")
}

// Create a new card
val newCard = Card(
  cardNumber = "1234-5678-9876-5432",
  cardType = CardType.Debit,
  expirationDate = LocalDate.of(2025, 12, 31),
  account = account1
)

// Add the card to the account
val updatedAccount12 = BankService.addCard(account1, newCard)

// Print account details
println(s"Updated account details: $updatedAccount1")
