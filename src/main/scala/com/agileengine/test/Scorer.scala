package com.agileengine.test

import org.jsoup.nodes.Element

class Scorer(rules: Set[Rule]) {


  def score(source: Element, target: Element): Double = {
    val scores = rules.map { rule =>
      rule.compare(source, target) / rule.maxScore
    }

    scores.sum
  }

}

object Scorer {
  def apply(): Scorer = new Scorer(
    Set(EqualIds, EqualElements, EqualTitles)
  )
}