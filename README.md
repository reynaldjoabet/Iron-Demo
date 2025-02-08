# Iron-Demo

It enables attaching constraints/assertions to types, to enforce properties and forbid invalid values.

- `Catch bugs`. In the spirit of static typing, use more specific types to avoid invalid values.
- `Compile-time and runtime`. Evaluate constraints at compile time, or explicitly check them at runtime (e.g. for a form).
- `Seamless`. Iron types are subtypes of their unrefined versions, meaning you can easily add or remove them.
- `No black magic`. Use Scala 3's powerful inline, types and restricted macros for consistent behaviour and rules. No unexpected behaviour.
- `Extendable`. Easily create your own constraints or integrations using classic typeclasses.


`LanguageCode`: A lowercase two-character Latin string is used as , and `ProductName` a non-empty string is used as . These are necessary so that it is impossible to create `Translation`, for example, with `name` equal `null` or `""`.

The necessary codecs for encoding and decoding in `Json are provided by the Iron integration library with Circe`

Now about the product model. As the identifier type, we use a clarifying type corresponding to the UUID format. And as the translation list type, we use a non-empty set `NonEmptySet` from the cats library.


First of all, we define three clarifying types: `NonEmptyString`, `DatabaseUrl`, `PortNumber`.

Then the configs `ApiConfig` and are defined `DatabaseConfig`. The configs will be read using the `pureconfig` library , so we use the built `derives` `ConfigReader`- in support for inheritance of type classes in Scala 3. Unfortunately, at the moment (July 2023) there is no module for interaction with pureconfig for the iron library , so it is necessary to additionally define `given ConfigReader[T]` for each specifying type.


[](https://www.scalabook.ru/books/pfhais.html)

`Inlining replaces a method or value reference with its definition during compilation.`
It's primarily used to improve performance by eliminating function call overhead, but it also enables compile-time computation and type-driven programming

- When a method is marked inline, its body is inlined at the call site during compilation, wherever it's used

-inline for Constant Values
Marking a value with inline makes it a compile-time constant.

- inline with Conditional Statements
The inline keyword works with if-statements to enable compile-time branching.

- inline with Match Expressions
Scala 3 allows inline match expressions for compile-time computation.

- inline Parameters
Marking parameters as inline allows passing constant expressions that can be evaluated during compilation.


[parse-don-t-validate](https://lexi-lambda.github.io/blog/2019/11/05/parse-don-t-validate/)

`coursier install --dir /usr/bin bloop --only-prebuilt=true `


### Two's Complement (Signed Integer Representation):
For signed integers, two's complement is the most common method used to represent negative numbers. Here's how it works:

- Take the binary representation of the positive value.
- Invert all the bits (1's complement).
- Add 1 to the result.
Example for `−3`(8-bit):
- 3 in binary: 00000011
- Invert the bits: 11111100
- Add 1: 11111101 -> This is `−3` in two's complement.


[building-a-rest-api-in-scala-3-using-iron-and-cats](https://dev.to/iltotore/building-a-rest-api-in-scala-3-using-iron-and-cats-eld)


[scala/iron](https://blog.michal.pawlik.dev/posts/scala/iron/)

[Let the compiler do the value validation: Iron looks like a good library for refinement types in Scala 3](https://blog.3qe.us/entry/2024/02/19/040745)