/*     ____ ____  ____ ____  ______                                     *\
**    / __// __ \/ __// __ \/ ____/    SOcos COmpiles Scala             **
**  __\_ \/ /_/ / /__/ /_/ /\_ \       (c) 2002, LAMP/EPFL              **
** /_____/\____/\___/\____/____/                                        **
\*                                                                      */

// $Id$

package meta.scalac.ast;

public class MetaDefaultTreeFactory extends AbstractTreeMethodExpander {

    //########################################################################
    // Public Methods

    public void printTreeMethod(TreeNode node) {
        printTreeMethodHeader(node, "int pos");
        writer.lbrace();
        node.printNew(writer.print(node.name + " t = ")).println(";");
        writer.println("t.pos = pos;");
        writer.println("return t;");
        writer.rbrace();
    }

    //########################################################################
}
