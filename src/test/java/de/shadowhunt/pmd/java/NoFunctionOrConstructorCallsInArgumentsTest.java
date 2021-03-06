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

import net.sourceforge.pmd.testframework.SimpleAggregatorTst;

public class NoFunctionOrConstructorCallsInArgumentsTest extends SimpleAggregatorTst {

    @Override
    protected void setUp() {
        addRule("rulesets/java/shadowhunt.xml", "NoFunctionOrConstructorCallsInArguments");
    }
}
