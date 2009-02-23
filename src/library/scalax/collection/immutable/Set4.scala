/*                     __                                               *\
**     ________ ___   / /  ___     Scala API                            **
**    / __/ __// _ | / /  / _ |    (c) 2003-2009, LAMP/EPFL             **
**  __\ \/ /__/ __ |/ /__/ __ |    http://scala-lang.org/               **
** /____/\___/_/ |_/____/_/ | |                                         **
**                          |/                                          **
\*                                                                      */

// $Id: Set1.scala 16893 2009-01-13 13:09:22Z cunei $



package scalax.collection.immutable

import collection.generic.Builder

/** This class implements immutable sets with exactly one element.
 *  @author  Martin Oderskty
 *  @version 1.0, 019/01/2007
 */
@serializable
class Set4[A](elem1: A, elem2: A, elem3: A, elem4: A) extends Set[A] {

  def size: Int = 4

  def contains(elem: A): Boolean = 
    elem == elem1 || elem == elem2 || elem == elem3 || elem == elem4

  def + (elem: A): Set[A] = 
    if (contains(elem)) this
    else new DefaultSet[A] + (elem1, elem2, elem3, elem4, elem)

  def - (elem: A): Set[A] = 
    if (elem == elem1) new Set3(elem2, elem3, elem4)
    else if (elem == elem2) new Set3(elem1, elem3, elem4)
    else if (elem == elem3) new Set3(elem1, elem2, elem4)
    else if (elem == elem4) new Set3(elem1, elem2, elem3)
    else this

  def elements: Iterator[A] = 
    Iterator(elem1, elem2, elem3, elem4)

  override def foreach(f: A => Unit): Unit = {
    f(elem1); f(elem2); f(elem3); f(elem4)
  }
}
  

