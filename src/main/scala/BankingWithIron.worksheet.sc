// import io.github.iltotore.iron.constraint.string.Match
// import java.time.LocalDate
// import io.github.iltotore.iron.* 
// import io.github.iltotore.iron.constraint.numeric.* 
// //import io.github.iltotore.iron.cats.given

// // Define the CardType enum
// enum CardType {
//   case Debit, Credit
// }

// // Define a case class for Card with validation
// case class Card(
//     cardNumber: String :| Match["\\d{4}-\\d{4}-\\d{4}-\\d{4}"],
//     cardType: CardType,
//     expirationDate: LocalDate,
//     account: Account
// )

// // Define the AccountType enum
// enum AccountType {
//   case Savings, Checking, Business
// }

// opaque type  Amount  = Int :| GreaterEqual[0]

// object  Amount{
//     def apply(value: Int :| GreaterEqual[0]): Amount = value

//     extension (amount:Amount){
//         def +(other:Amount):Amount = amount.refineUnsafe + other
//         def -(other:Amount):Amount = amount.refineUnsafe - other
//     }
// }
// // Define a case class for Customer
// case class Customer(id: String, name: String)

// // Define a case class for Account with validation
// case class Account(
//   id: String,
//   customer: Customer,
//   accountType: AccountType,
//   balance: Amount,
//   cards: List[Card] = List.empty  // Default empty list of cards
// )

// // Define a sealed trait and case classes for Transaction
// sealed trait Transaction {
//   def id: String
//   def fromAccount: Account
//   def toAccount: Account
//   def amount: Amount
// }

// case class Deposit(id: String, toAccount: Account, amount: Amount) extends Transaction {
//   def fromAccount: Account = Account("", Customer("", ""), AccountType.Savings,amount, List.empty)
// }

// case class Withdrawal(id: String, fromAccount: Account, amount: Int :| GreaterEqual[0]) extends Transaction {
//   def toAccount: Account = Account("", Customer("", ""), AccountType.Savings, amount)
// }

// case class Transfer(id: String, fromAccount: Account, toAccount: Account, amount: Int :| GreaterEqual[0])
//     extends Transaction {}

// object BankService {

//   def deposit(account: Account, amount: Amount): Account =
//     {
//         val balance: Amount = (account.balance + amount).asInstanceOf[Amount]
//         account.copy(balance = balance)
//     }
    

//   def withdraw(account: Account, amount: Int :| GreaterEqual[0]): Either[String, Account] =
//     if amount > account.balance then Left("Insufficient funds")
//     else Right(account.copy(balance = (account.balance - amount).asInstanceOf[Amount]))

//   def transfer(
//       fromAccount: Account,
//       toAccount: Account,
//       amount: Int :| GreaterEqual[0]
//   ): Either[String, (Account, Account)] =
//     withdraw(fromAccount, amount).map { updatedFromAccount =>
//       val updatedToAccount = deposit(toAccount, amount)
//       (updatedFromAccount, updatedToAccount)
//     }

//   def addCard(account: Account, card: Card): Account =
//     account.copy(cards = card :: account.cards)
// }

// import BankService._

// //@main def runBankingDemo() {
// val customer1 = Customer("1", "Alice")
// val customer2 = Customer("2", "Bob")

// val account1 = Account("acc1", customer1, AccountType.Checking,1000)
// val account2 = Account("acc2", customer2, AccountType.Savings, 500)

// // Deposit money into account1
// val updatedAccount1 = BankService.deposit(account1, 200)
// println(s"Updated balance of account1: ${updatedAccount1.balance}")

// // Withdraw money from account2
// BankService.withdraw(account2, 100) match {
//   case Right(updatedAccount2) => println(s"Updated balance of account2: ${updatedAccount2.balance}")
//   case Left(error)            => println(s"Error: $error")
// }

// // Transfer money from account1 to account2
// BankService.transfer(updatedAccount1, account2, 300) match {
//   case Right((fromAccount, toAccount)) =>

//     print("Updated balance of fromAccount: ")
//   case Left(error) => println(s"Error: $error")
// }

// // Create a new card
// val newCard = Card(
//   cardNumber = "1234-5678-9876-5432".asInstanceOf[String :| Match["\\d{4}-\\d{4}-\\d{4}-\\d{4}"]],
//   cardType = CardType.Debit,
//   expirationDate = LocalDate.of(2025, 12, 31),
//   account = account1
// )

// // Add the card to the account
// val updatedAccount12 = BankService.addCard(account1, newCard)

// // Print account details
// println(s"Updated account details: $updatedAccount1")

// import io.github.iltotore.iron.constraint.collection.*

// object U {
//    type UserName = String :| MinLength[ 10 ]
//    object UserName extends RefinedTypeOps[ String , MinLength[ 10 ], UserName]
// }
// object P {
//    type Password = String :| MinLength[ 10 ]
//    object Password extends RefinedTypeOps[ String , MinLength[ 10 ], Password]
// }

// val p: P.Password = P.Password( "foobar2000" ) // ok 
// val u: U.UserName = p // ok but danger!!




