/**
 * Shadowhunt PMD Rulesets - Extended PMD Rulesets
 * Copyright © 2018 shadowhunt (dev@shadowhunt.de)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package de.shadowhunt.pmd.java;

import java.util.List;

import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.java.ast.ASTArgumentList;
import net.sourceforge.pmd.lang.java.ast.ASTArguments;
import net.sourceforge.pmd.lang.java.ast.ASTExplicitConstructorInvocation;
import net.sourceforge.pmd.lang.java.ast.JavaNode;
import net.sourceforge.pmd.lang.java.rule.AbstractJavaRule;

public class NoFunctionOrConstructorCallsInArguments extends AbstractJavaRule {

    @Override
    public Object visit(final ASTArgumentList node, final Object data) {
        final List<ASTExplicitConstructorInvocation> parents = node.getParentsOfType(ASTExplicitConstructorInvocation.class);
        if (!parents.isEmpty()) {
            // allow in this and super calls
            return visit((JavaNode) node, data);
        }

        final int children = node.jjtGetNumChildren();
        if (children > 0) {
            for (int child = 0; child < children; child++) {
                final Node argument = node.jjtGetChild(child);
                final List<ASTArguments> arguments = argument.findDescendantsOfType(ASTArguments.class);
                if (!arguments.isEmpty()) {
                    addViolationWithMessage(data, node, "argument at position " + (child + 1) + " is a function or constructor call");
                }
            }
        }
        return visit((JavaNode) node, data);
    }
}
