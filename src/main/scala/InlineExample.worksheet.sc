inline def add(x: Int, y: Int): Int = x + y

val result = add(3, 4) // At compile time, `val result = 3 + 4`

// Benefits:
// - Eliminates function call overhead.
// - Enables optimizations, such as constant folding.
val radius        = 9
inline val Pi     = 3.14159
val circumference = 2 * Pi * radius // Pi is inlined as 3.14159

inline def isEven(x: Int): Boolean =
  inline if x % 2 == 0 then true else false
//Use Case: Create highly optimized code paths based on compile-time conditions.

val result2 = isEven(4) // Compile-time check, replaced by `true`

inline def describe(x: Int): String =
  inline x match {
    case 1 => "One"
    case 2 => "Two"
    case _ => "Unknown"
  }
val description = describe(1) // Replaced with "One" at compile-time

//Inline with Meta-Programming
import scala.compiletime.*

inline def printType[T <: Tuple]: Unit =
  println(constValue[Tuple.Size[T]]) // Evaluates at compile time

printType[EmptyTuple]

3
printType[2 *: EmptyTuple]

//- scala.compiletime.constValue: Extracts constant values at compile-time.
//- scala.compiletime.erasedValue: Provides access to type-level erased information.

inline def summonType[T]: String =
  inline erasedValue[T] match {
    case _: Int    => "Type is Int"
    case _: String => "Type is String"
    case _         => "Unknown type"
  }
summonType[Int] // Replaced with "Type is Int"

//on the jvm generic type information is erased at runtime
//generic type information is erased at runtime
import scala.compiletime.constValue
import scala.quoted.*

type MyType = 42 // A constant type

inline def extractValue[T]: T = constValue[T] // Extract the constant value

val value = extractValue[MyType] // This will give you 42
//println(value)  // Output: 42

6
