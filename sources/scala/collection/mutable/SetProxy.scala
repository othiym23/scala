/*                     __                                               *\
**     ________ ___   / /  ___     Scala API                            **
**    / __/ __// _ | / /  / _ |    (c) 2003, LAMP/EPFL                  **
**  __\ \/ /__/ __ |/ /__/ __ |                                         **
** /____/\___/_/ |_/____/_/ | |                                         **
**                          |/                                          **
** $Id$
\*                                                                      */

package scala.collection.mutable;


/** This is a simple wrapper class for <code>scala.collection.mutable.Set</code>.
 *  It is most useful for assembling customized set abstractions
 *  dynamically using object composition and forwarding.
 *
 *  @author  Matthias Zenger
 *  @version 1.1, 09/05/2004
 */
class SetProxy[A](set: Set[A]) extends Set[A]
                               with scala.collection.SetProxy[A](set) {

    override def update(elem: A, included: Boolean): Unit = set(elem) = included;
    
    def +=(elem: A): Unit = set += elem;
    
    override def ++=(that: Iterable[A]): Unit = set ++= that;
    
    override def ++=(it: Iterator[A]): Unit = set ++= it;
    
    override def incl(elems: A*): Unit = set ++= elems;
    
    def -=(elem: A): Unit = set -= elem;

    override def --=(that: Iterable[A]): Unit = set --= that;

    override def --=(it: Iterator[A]): Unit = set --= it;
    
    override def excl(elems: A*): Unit = set --= elems;
    
    override def intersect(that: Set[A]): Unit = set.intersect(that);
    
    def clear: Unit = set.clear;
    
    override def filter(p: A => Boolean): Unit =  set.filter(p);

    override def <<(cmd: Message[A]): Unit = set << cmd;
    
    override def clone(): Set[A] = new SetProxy(set.clone());
}
