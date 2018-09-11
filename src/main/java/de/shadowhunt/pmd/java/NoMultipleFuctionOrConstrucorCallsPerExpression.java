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

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.java.ast.ASTArguments;
import net.sourceforge.pmd.lang.java.ast.ASTExplicitConstructorInvocation;
import net.sourceforge.pmd.lang.java.ast.ASTPrimaryExpression;
import net.sourceforge.pmd.lang.java.ast.ASTPrimaryPrefix;
import net.sourceforge.pmd.lang.java.ast.ASTPrimarySuffix;
import net.sourceforge.pmd.lang.java.ast.JavaNode;
import net.sourceforge.pmd.lang.java.rule.AbstractJavaRule;

public class NoMultipleFuctionOrConstrucorCallsPerExpression extends AbstractJavaRule {

    private <T> List<T> getChildrenOfType(final Node node, final Class<T> childType) {
        final int children = node.jjtGetNumChildren();
        final List<T> result = new ArrayList<>(children);
        for (int child = 0; child < children; child++) {
            final Node childNode = node.jjtGetChild(child);
            if (childType.isInstance(childNode)) {
                result.add(childType.cast(childNode));
            }
        }
        return result;
    }

    @Override
    public Object visit(final ASTPrimaryExpression node, final Object data) {
        final List<ASTExplicitConstructorInvocation> parents = node.getParentsOfType(ASTExplicitConstructorInvocation.class);
        if (!parents.isEmpty()) {
            // allow in this and super calls
            return visit((JavaNode) node, data);
        }

        // constructor call has arguments in prefix
        int counter = 0;
        final List<ASTPrimaryPrefix> prefixes = getChildrenOfType(node, ASTPrimaryPrefix.class);
        for (final ASTPrimaryPrefix prefix : prefixes) {
            if (prefix.hasDescendantOfType(ASTArguments.class)) {
                counter++;
            }
        }

        // function call has arguments in suffix
        final List<ASTPrimarySuffix> suffixes = getChildrenOfType(node, ASTPrimarySuffix.class);
        for (final ASTPrimarySuffix suffix : suffixes) {
            if (suffix.hasDescendantOfType(ASTArguments.class)) {
                counter++;
            }
        }

        // only one constructor or function call allowed
        if (counter > 1) {
            addViolationWithMessage(data, node, "Do not chain multiple functioncalls.");
        }
        return visit((JavaNode) node, data);
    }
}
