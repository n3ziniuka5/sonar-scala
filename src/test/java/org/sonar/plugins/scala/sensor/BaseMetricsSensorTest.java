/*
 * Sonar Scala Plugin
 * Copyright (C) 2011 - 2014 All contributors
 * dev@sonar.codehaus.org
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */
package org.sonar.plugins.scala.sensor;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.internal.DefaultFileSystem;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.Measure;
import org.sonar.api.measures.Metric;
import org.sonar.api.resources.Project;
import org.sonar.plugins.scala.language.Scala;
import org.sonar.plugins.scala.util.FileTestUtils;

public class BaseMetricsSensorTest {

  private static final int NUMBER_OF_FILES = 3;

  private BaseMetricsSensor baseMetricsSensor;

  private FileSystem fileSystem;
  private Project project;
  private SensorContext sensorContext;

  @Before
  public void setUp() {
    fileSystem = new DefaultFileSystem();
    baseMetricsSensor = new BaseMetricsSensor(fileSystem, mock(Scala.class));
    project = mock(Project.class);
    sensorContext = mock(SensorContext.class);
  }

  @Test
  public void shouldIncrementFileMetricForOneScalaFile() {
    analyseOneScalaFile();
    verifyMeasuring(CoreMetrics.FILES, 1.0);
  }

  @Test
  public void shouldIncreaseFileMetricForAllScalaFiles() throws IOException {
    analyseAllScalaFiles();
    verifyMeasuring(CoreMetrics.FILES, NUMBER_OF_FILES, 1.0);
  }

  @Test
  public void shouldMeasureNothingWhenNoFiles() {
    analyseScalaFiles(0);
    verifyNoMoreInteractions(sensorContext);
  }

  @Test
  public void shouldMeasureFunctionComplexityDistributionForOneScalaFileOnlyOnce() {
    analyseOneScalaFile();
    verify(sensorContext).saveMeasure(eq(new Measure(CoreMetrics.FUNCTION_COMPLEXITY_DISTRIBUTION)));
  }

  @Test
  public void shouldMeasureFunctionComplexityDistributionForAllScalaFilesOnlyOnce() {
    analyseAllScalaFiles();
    verify(sensorContext).saveMeasure(eq(new Measure(CoreMetrics.FUNCTION_COMPLEXITY_DISTRIBUTION)));
  }

  @Test
  public void shouldMeasureLineMetricsForOneScalaFile() {
    analyseOneScalaFile();
    verifyMeasuring(CoreMetrics.LINES);
    verifyMeasuring(CoreMetrics.NCLOC);
  }

  @Test
  public void shouldMeasureLineMetricsForAllScalaFiles() {
    analyseAllScalaFiles();
    verifyMeasuring(CoreMetrics.LINES, NUMBER_OF_FILES);
    verifyMeasuring(CoreMetrics.NCLOC, NUMBER_OF_FILES);
  }

  @Test
  public void shouldMeasureCommentMetricsForOneScalaFile() {
    analyseOneScalaFile();
    verifyMeasuring(CoreMetrics.COMMENT_LINES);
  }

  @Test
  public void shouldMeasureCommentMetricsForAllScalaFiles() {
    analyseAllScalaFiles();
    verifyMeasuring(CoreMetrics.COMMENT_LINES, NUMBER_OF_FILES);
  }

  @Test
  public void shouldMeasureCodeMetricsForOneScalaFile() {
    analyseOneScalaFile();
    verifyMeasuring(CoreMetrics.CLASSES);
    verifyMeasuring(CoreMetrics.STATEMENTS);
    verifyMeasuring(CoreMetrics.FUNCTIONS);
    verifyMeasuring(CoreMetrics.COMPLEXITY);
    verifyMeasuringMeasure(CoreMetrics.FILE_COMPLEXITY_DISTRIBUTION);
  }

  @Test
  public void shouldMeasureCodeMetricsForAllScalaFiles() {
    analyseAllScalaFiles();
    verifyMeasuring(CoreMetrics.CLASSES, NUMBER_OF_FILES);
    verifyMeasuring(CoreMetrics.STATEMENTS, NUMBER_OF_FILES);
    verifyMeasuring(CoreMetrics.FUNCTIONS, NUMBER_OF_FILES);
    verifyMeasuring(CoreMetrics.COMPLEXITY, NUMBER_OF_FILES);
    verifyMeasuringMeasure(CoreMetrics.FILE_COMPLEXITY_DISTRIBUTION, NUMBER_OF_FILES);
  }

  @Test
  public void shouldMeasurePublicApiMetricsForOneScalaFile() {
    analyseOneScalaFile();
    verifyMeasuring(CoreMetrics.PUBLIC_API);
    verifyMeasuring(CoreMetrics.PUBLIC_UNDOCUMENTED_API);
  }

  @Test
  public void shouldMeasurePublicApiMetricsForAllScalaFiles() {
    analyseAllScalaFiles();
    verifyMeasuring(CoreMetrics.PUBLIC_API, NUMBER_OF_FILES);
    verifyMeasuring(CoreMetrics.PUBLIC_UNDOCUMENTED_API, NUMBER_OF_FILES);
  }

  private void verifyMeasuring(Metric metric) {
    verifyMeasuring(metric, 1);
  }

  private void verifyMeasuring(Metric metric, int numberOfCalls) {
    verify(sensorContext, times(numberOfCalls)).saveMeasure(eq(FileTestUtils.SCALA_SOURCE_FILE),
        eq(metric), any(Double.class));
  }

  private void verifyMeasuring(Metric metric, double value) {
    verifyMeasuring(metric, 1, value);
  }

  private void verifyMeasuring(Metric metric, int numberOfCalls, double value) {
    verify(sensorContext, times(numberOfCalls)).saveMeasure(eq(FileTestUtils.SCALA_SOURCE_FILE),
        eq(metric), eq(value));
  }

  private void verifyMeasuringMeasure(Metric metric) {
    verifyMeasuringMeasure(metric, 1);
  }

  private void verifyMeasuringMeasure(Metric metric, int numberOfCalls) {
    ArgumentCaptor<Measure> argument = ArgumentCaptor.forClass(Measure.class);
    verify(sensorContext, times(numberOfCalls)).saveMeasure(eq(FileTestUtils.SCALA_SOURCE_FILE),
        argument.capture());
    assertEquals(metric, argument.getValue().getMetric());
  }

  private void analyseOneScalaFile() {
    analyseScalaFiles(1);
  }

  private void analyseAllScalaFiles() {
    analyseScalaFiles(NUMBER_OF_FILES);
  }

  private void analyseScalaFiles(int numberOfFiles) {
    FileTestUtils.addMainFiles(fileSystem, FileTestUtils.getInputFiles("/baseMetricsSensor/", "ScalaFile", numberOfFiles));
    baseMetricsSensor.analyse(project, sensorContext);
  }
}