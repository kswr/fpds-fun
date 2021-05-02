package week1

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

class Week1Spec extends AnyFlatSpec with should.Matchers {
  "Pattern matching" should "match function with type String => String" in {
    val f: String => String = {
      case "ping" => "pong"
      case _      => "empty"
    }
    f("ping") should be("pong")
    f("pong") should be("empty")
  }
  it should "throw exception when there is no matching case" in {
    val f: String => String = {
      case "ping" => "pong"
    }
    f("ping") should be("pong")
    assertThrows[MatchError] {
      f("pong") should be("empty")
    }
  }
  it should "verify if partial function is applicable" in {
    val f: PartialFunction[String, String] = {
      case "ping" => "pong"
    }
    f.isDefinedAt("ping") should be(true)
    f.isDefinedAt("pong") should be(false)
  }

  "Higher order functions" should "filter books by beginning of author's name" in {
    case class Book(title: String, authors: List[String])
    val books =
      Seq(
        Book("Book 1", List("John", "Bob")),
        Book("Book 2", List("John", "Jeff")),
        Book("Book3", List("Sam")),
        Book("Book4", List("Sam"))
      )

    val titles = for {
      b <- books
      a <- b.authors if a.startsWith("Jo")
    } yield b.title

    // this is wrong! it does not work and it can not work!
    // y is String, not a Book
    //    books flatMap (b =>
    //      b.authors withFilter (a => a startsWith "Jo") map (y => y.title)
    //    )

    // this is not correct, but compiles, opposed to the code 3 lines higher
    val titles1 = books.flatMap(b =>
      b.authors.withFilter(a => a.startsWith("Jo")).map(a => a)
    )

    // this is correct, but uses method exists, while exercise is about rewriting for comprehension using only maps and flatmaps
    val titles2 =
      books.filter(_.authors.exists(_.startsWith("Jo"))).map(_.title)

    // this is correct
    val titles3 = books.flatMap(b =>
      b.authors.withFilter(a => a.startsWith("Jo")).map(_ => b.title)
    )

    // can be uncommented to see why titles1 is incorrect
//    println(titles)
//    println(titles1)
//    println(titles2)
//    println(titles3)

    // conclusion: there is an error in the course in Lecture 1.2, presented snippet of code does not work,
    // it needs to be rewritten as in titles3 to work correctly
    // very helpful gist https://gist.github.com/PiotrCL/18918c620a29f403f7bda127d10faa08

    titles3 should be(titles)

  }

  "Random tree generator" should "generate random Tree object" in {

    trait Generator[+T] {
      self =>

      def generate: T

      def map[S](f: T => S): Generator[S] =
        new Generator[S] {
          override def generate: S = f(self.generate)
        }

      def flatMap[S](f: T => Generator[S]): Generator[S] =
        new Generator[S] {
          override def generate: S = f(self.generate).generate
        }
    }

    trait Tree

    case class Inner(left: Tree, right: Tree) extends Tree

    case class Leaf(x: Int) extends Tree

    val integers = new Generator[Int] {
      def generate: Int = scala.util.Random.nextInt()
    }

    val booleans = integers.map(_ >= 0)

    def leafs: Generator[Leaf] =
      for {
        x <- integers
      } yield Leaf(x)

    def inners: Generator[Inner] =
      for {
        l <- trees
        r <- trees
      } yield Inner(l, r)

    def trees: Generator[Tree] =
      for {
        isLeaf <- booleans
        tree <- if (isLeaf) leafs else inners
      } yield tree

    println(trees.generate)
  }

}
