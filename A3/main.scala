import scala.util.parsing.combinator._
import scala.io.StdIn.readLine
import scala.collection.JavaConverters._

abstract class Tree

case class Alt(l: Tree, r: Tree) extends Tree
case class Opt(l: Tree, r: Tree) extends Tree
case class Any(l: Tree, r: Tree) extends Tree
case class Par(l: Tree, r: Tree) extends Tree

case class Var(n: Tree) extends Tree
case class Const(v: Tree) extends Tree

case class E(l: Tree, r: Tree) extends Tree
case class T(l: Tree, r: Tree) extends Tree
case class F(l: Tree) extends Tree
case class A(c: Tree) extends Tree

class Combinators extends JavaTokenParsers{

  var input: String = ""

  /*

 S  -: E$
 E  -: T '|' E | T
 T  -: F T | F       //  What to do
 F  -: A '?' | A
 A  -: C | '(' E ')'

 */

   def s: Parser[Tree] = e
   def e: Parser[Tree] = t ~ alt ~ e ^^ { case l ~ _ ~ r  =>  E(l, r) } | t //^^ { case l => E(l,None,None) } w/ option?
   def t: Parser[Tree] = f ~ t ^^ { case l ~ r  =>  T(l, r) } | f
   def f: Parser[Tree] = a ~ opt ^^ { case l ~ _  =>  F(l) } | a
   def a: Parser[Tree] = op ~ e ~ cp ^^ { case _ ~ c ~ _  =>  A(c) } | p
   def p: Parser[Tree] = varname

  // def const: Parser[Const] = "[0-9]+".r ^^ { str => Const(str.toInt) }
   def varname: Parser[Var] = "[0-9A-Za-z/.]".r ^^ { str => Var(str) }

   def alt[Tree] = "|"
   def opt[Tree] = "?"
   def any[Tree] = "."
   def op[Tree] = "("
   def cp[Tree] = ")"

   def plusc[Tree] = "+"
   def multc[Tree] = "*"       // Made multc = *
}

object Main extends Combinators {

  def matchIt(pattern: Any, input: Any):Boolean = pattern match {

    //case E(l, r) => matchIt(l, input) | matchIt(r, input)
    case E(l,r) => matchIt(pattern,e)



    case T(l, r) => eval(l, input) & eval(r, input)
    case F(l) => eval(l, input)
    case A(c) => eval(c, input)

    case Var(n) => if (n.equals(input)) {
      println("n : " + n)
      println("input: " + input)
      true
    } else {
      false
    }

  }

  def eval(t: Tree, input: String): Boolean = t match {

    case E(l, r) => eval(l, input) | eval(r, input)
    case T(l, r) => eval(l, input) & eval(r, input)
    case F(l) => eval(l, input)
    case A(c) => eval(c, input)

    case Var(n) => if (n.equals(input)) {
      println("n : " + n)
      println("input: " + input)
      true
      } else {
        false
      }
/*
    case Var(n) => input match {
      case Var(str) => if (n == ".") {
        println(n + " period... " + str)
        true
      } else {
        println(a + " " + str)
        a == str
      }
    }
*/
    //case Const(v)  => v
  }

  def main(args: Array[String]) {



    val pattern = scala.io.StdIn.readLine("Pattern: ")
    println("Pattern: " + pattern)
    val pat: Tree = parseAll(s, pattern).get
    println("Parsed Pat: " + pat)

    while (input != "q") {
      input = readLine("String: ")
      val inputList = input.toArray

   //   println("Eval: " + eval(pat, input))

      for (i <- 0 to (inputList.length - 1)) {

        val test = eval(pat,inputList(i).toString)
        println("Test: " + test)

      }
    }
  }
}