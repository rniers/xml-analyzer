package com.agileengine.test

import org.jsoup.nodes.Element
import scala.collection.JavaConverters._

trait Rule {
  def description: String
  def compare(source: Element, target: Element): Double
  def weight: Int
  def maxScore: Int

  override def toString: String = description
}

trait BooleanRule extends Rule {

  def compareBool(source: Element, target: Element): Boolean

  override def maxScore: Int = 1
  override def compare(source: Element, target: Element): Double = {
    if (compareBool(source, target)) 1
    else 0
  }

}

object EqualIds extends BooleanRule {
  override val description = "Elements have equal ids"
  override val weight = 100
  override def compareBool(source: Element, target: Element): Boolean = {
    source.id() == target.id() && source.id().nonEmpty
  }
}

object EqualTags extends BooleanRule {
  override val description = "Elements have equal tags"
  override val weight: Int = 20
  override def compareBool(source: Element, target: Element): Boolean = {
    source.tagName() == target.tagName()
  }
}

object EqualTitles extends BooleanRule {
  override val description = "Elements have equal titles"
  override val weight = 20
  override def compareBool(source: Element, target: Element): Boolean = {
    source.ownText().trim.toLowerCase() == target.ownText().trim.toLowerCase()
  }
}

object CompareAttributes extends Rule {
  override val description = "Number of element attributes is the same"
  override val weight = 100
  override val maxScore = 1

  override def compare(source: Element, target: Element): Double = {
    val sourceAttrMap = source
      .attributes()
      .asList()
      .asScala
      .map(attr => attr.getKey -> attr.getValue)
      .toMap
    val targetAttrMap = target
      .attributes()
      .asList()
      .asScala
      .map(attr => attr.getKey -> attr.getValue)
      .toMap

    if (sourceAttrMap.nonEmpty) {
      val sameCount = targetAttrMap.foldLeft(0) { (acc, attr) =>
        val (key, value) = attr
        if (sourceAttrMap.get(key).contains(value)) acc + 1
        else acc
      }
      sameCount.toDouble / sourceAttrMap.size
    } else 0
  }
}
