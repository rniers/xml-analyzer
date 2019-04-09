package com.agileengine.test

import org.jsoup.nodes.Element

trait Rule {
  def compare(source: Element, target: Element): Double
  def weight: Int
  def maxScore: Int
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
  override val weight = 100
  override def compareBool(source: Element, target: Element): Boolean = {
    source.id() == target.id() && source.id().nonEmpty
  }
}

object EqualElements extends BooleanRule {
  override val weight: Int = 20

  override def compareBool(source: Element, target: Element): Boolean = {
    source.tagName() == target.tagName()
  }
}

object EqualTitles extends BooleanRule {
  override val weight = 100
  override def compareBool(source: Element, target: Element): Boolean = {
    source.ownText().trim.toLowerCase() == target.ownText().trim.toLowerCase()
  }
}