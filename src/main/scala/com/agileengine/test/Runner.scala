package com.agileengine.test

import java.io.File

import org.jsoup.Jsoup
import org.jsoup.nodes.{Document, Element}

import scala.collection.JavaConverters._
import scala.util.{Failure, Success, Try}

object Runner {

  /** Iterate through all elements in document
    * and find one with maz score
    *
    * @param target - target element
    * @param in - document to perform lookup in
    * @param threshold - consider elements only with higher score
    * @return
    */
  def findMostAlike(target: Element, in: Document, threshold: Int = 0): Option[Element] = {

    val scorer = Scorer()
    val targets = in.body.select("*")

    // todo: elements list might be empty
    val elementsWithScores = targets.iterator().asScala.map(el => el -> scorer.score(target, el))

    if (elementsWithScores.nonEmpty) {
      val (element, score) = elementsWithScores.maxBy(_._2)
      if (score >= threshold) Some(element)
      else None
    } else None
  }

  /** Build xml path for provided element
    *
    * @param element
    * @return
    */
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
          sampleDocument <- Try(Jsoup.parse(config.sample, Charset))
        } yield findMostAlike(element, sampleDocument)

        result match {
          case Success(Some(element)) =>
            println(xmlPath(element))

          case Success(None) =>
            println("No such element found")

          case Failure(t) =>
            println(s"Error: ${t.getMessage}")
        }

      case None =>
        println("failed to parse config")
    }
  }
}