package com.agileengine.test

import java.io.File

import org.jsoup.Jsoup
import org.jsoup.nodes.{Document, Element}

import scala.collection.JavaConverters._
import scala.collection.mutable
import scala.util.{Failure, Success, Try}

object Run {


  def findMostAlike(source: Element, in: Document): Option[Element] = {

    val scorer = Scorer()
    val targets = in.body.select("*")
    val candidates = mutable.PriorityQueue.empty[(Double, Element)](_._1 compareTo _._1)

    targets.iterator().asScala.foreach { el =>
      candidates.enqueue((scorer.score(source, el), el))
    }

    candidates.headOption.map(_._2)
  }


  def xmlPath(element: Element): String = {
    element.toString
  }

  case class Config(
                     in: File = new File("."),
                     sample: File = new File("."),
                     id: String = "make-everything-ok-button"
                   )



  def main(args: Array[String]): Unit = {

    val Charset = "UTF-8"

    val parser = new scopt.OptionParser[Config]("Find similar elements") {
      arg[File]("<input_origin_file_path>").required().valueName("<input_origin_file_path>")
        .action((x, c) => c.copy(in = x))
        .text("origin sample path to find the element with attribute " +
          "id=\"make-everything-ok-button\" and collect all the required information")

      arg[File]("<input_other_sample_file_path>").required().valueName("<input_other_sample_file_path>")
        .action((x, c) => c.copy(sample = x))
        .text("path to diff-case HTML file to search a similar element")

      arg[String]("<target_id>").optional().valueName("<target_id>")
        .action((x, c) => c.copy(id = x))
        .text("target element id")
    }

    parser.parse(args, Config()) match {
      case Some(config) =>

        val result = for {
          // todo: element might be `null`
          element <- Try(Jsoup.parse(config.in, Charset)).map(_.getElementById(config.id))
          sampleDocumet <- Try(Jsoup.parse(config.sample, Charset))
        } yield findMostAlike(element, sampleDocumet)

        result match {
          case Success(Some(element)) => xmlPath(element)
          case Success(None) =>
            println("No such element found")

          case Failure(t) =>
            println(s"Error: ${t.getMessage}")
        }

      case None =>
    }
  }
}