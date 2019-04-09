package com.agileengine.test

import org.jsoup.nodes.Element

class Scorer(rules: Set[Rule]) {

  /**
    * Calculate the final similarity score for 2 elements
    *
    * @param target - element we looking similar elements for
    * @param candidate - next candidate
    * @return
    */
  def score(target: Element, candidate: Element): Double = {
    // calculate weighted average
    val (sumS, sumW) = rules.foldLeft((0D, 0D)){ (acc, rule) =>
      val (sumScore, sumWeight) = acc
      val normalized = rule.compare(target, candidate) / rule.maxScore

      (sumScore + normalized * rule.weight, sumWeight + rule.weight)
    }

    sumS / sumW
  }

}

object Scorer {
  def apply(): Scorer = new Scorer(
    Set(CompareAttributes, EqualIds, EqualElements, EqualTitles)
  )
}