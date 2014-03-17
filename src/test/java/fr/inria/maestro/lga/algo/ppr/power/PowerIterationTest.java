package fr.inria.maestro.lga.algo.ppr.power;

/*
 * Initial Software by Marina Sokol and Alexey Mishenin,
 * Copyright © Inria (MAESTRO research team), All Rights Reserved, 2009-2014.
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses/>.
 */

import fr.inria.maestro.lga.TestUtils;
import fr.inria.maestro.lga.algo.ppr.ICalculator;
import fr.inria.maestro.lga.algo.ppr.params.CalculatorInputDataSigma;
import fr.inria.maestro.lga.graph.model.IGraph;
import fr.inria.maestro.lga.utils.formatters.DoubleFormatter;
import fr.inria.maestro.lga.utils.io.Encoding;
import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

import static fr.inria.maestro.lga.algo.ppr.CalculatorFactory.*;


@RunWith(Parameterized.class)
public class PowerIterationTest {

    private final ICalculator calculator;
    private final double[] goldResult;
    private final Integer startNode;

    public PowerIterationTest(final ICalculator calculator, final double[] goldResult, final Integer startNode) {
        this.calculator = calculator;
        this.goldResult = goldResult;
        this.startNode = startNode;
    }

    @Test
    public void testPageRank() {
        final double[] result = startNode == null ? calculator.compute() : calculator.compute(100);

        Assert.assertArrayEquals(result, goldResult, 0.0001);
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() throws IOException {
        final IGraph graph = TestUtils.loadMatLabGraph("/graph/graph.txt");
        final CalculatorInputDataSigma defaultInput = new CalculatorInputDataSigma(0.7);

        Object[][] data = new Object[][]
        {   {  getStandardLaplacianCalculator(graph, defaultInput), loadResult("std_laplacian_0.7.txt"), null },
            {  getStandardLaplacianCalculator(graph, defaultInput), loadResult("std_laplacian_0.7_100.txt"), 100 },
            {  getPageRankCalculator(graph, defaultInput), loadResult("page_rank_0.7.txt"), null },
            {  getPageRankCalculator(graph, defaultInput), loadResult("page_rank_0.7_100.txt"), 100 },
            {  getGeneralCalculator(graph, defaultInput), loadResult("general_0.7.txt"), null },
            {  getGeneralCalculator(graph, defaultInput), loadResult("general_0.7_100.txt"), 100 },
            {  getNormLaplacianCalculator(graph, defaultInput), loadResult("norm_lapl_0.7.txt"), null },
            {  getNormLaplacianCalculator(graph, defaultInput), loadResult("norm_lapl_0.7_100.txt"), 100 },
        };

        return Arrays.asList(data);

    }

    private static double[] loadResult(final String name) throws IOException {
        final File folder = FileUtils.toFile(PowerIterationTest.class.getResource("/pr_gold"));
        return loadFromFile(new File(folder, name));
    }

    private static double[] loadFromFile(final File file) throws IOException {
        final String[] values = FileUtils.readFileToString(file, Encoding.DEFAULT_CHARSET_NAME).split(" ");
        final double[] doubleValues = new double[values.length];

        for (int i = 0; i < values.length; i++) {
            doubleValues[i] = DoubleFormatter.parseDouble(values[i]);
        }

        return doubleValues;
    }
}
