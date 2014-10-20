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
package org.sonar.plugins.scala.util;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.internal.DefaultFileSystem;
import org.sonar.api.batch.fs.internal.DefaultInputFile;
import org.sonar.plugins.scala.language.Scala;
import org.sonar.plugins.scala.language.ScalaFile;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public final class FileTestUtils {

  public static final ScalaFile SCALA_SOURCE_FILE = new DummyScalaFile(false);
  public static final ScalaFile SCALA_TEST_FILE = new DummyScalaFile(true);

  private static final Logger LOGGER = LoggerFactory.getLogger(FileTestUtils.class);

  private FileTestUtils() {
    // to prevent instantiation
  }

  public static void addMainFiles(FileSystem fs, Iterable<InputFile> inputFiles) {
    addFiles(fs, inputFiles, false);
  }

  public static void addTestFiles(FileSystem fs, Iterable<InputFile> inputFiles) {
    addFiles(fs, inputFiles, true);
  }

  private static void addFiles(FileSystem fs, Iterable<InputFile> inputFiles, boolean testFile) {
    if (! (fs instanceof DefaultFileSystem)) return;
    if (inputFiles == null) return;
    for (InputFile inputFile : inputFiles) {
      if (! (inputFile instanceof DefaultInputFile)) continue;
      ((DefaultInputFile) inputFile).setType(testFile ? InputFile.Type.TEST : InputFile.Type.MAIN);
      ((DefaultFileSystem) fs).add(inputFile);
    }
  }

  public static String getRelativePath(String path) {
    return FileTestUtils.class.getResource(path).getFile();
  }

  public static InputFile getInputFile(File baseDir, String relativeFilePath, boolean testFile) {
      String relativePath = baseDir.getPath() + '/' + relativeFilePath;
      DefaultInputFile inputFile = new DefaultInputFile(relativePath);
      inputFile.setAbsolutePath(baseDir.getAbsolutePath() + '/' + relativeFilePath);
      inputFile.setLanguage(Scala.INSTANCE.getKey());
      inputFile.setType(testFile ? InputFile.Type.TEST : InputFile.Type.MAIN);
      return inputFile;
  }

  public static List<InputFile> getInputFiles(String path, String fileNameBase, int numberOfFiles) {
    return getInputFiles(path, fileNameBase, "scala", numberOfFiles);
  }

  public static List<InputFile> getInputFiles(String path, String fileNameBase, String fileSuffix, int numberOfFiles) {
    List<InputFile> inputFiles = new ArrayList<InputFile>();
    URL resourceURL = FileTestUtils.class.getResource(path + fileNameBase + "1." + fileSuffix);
    for (int i = 1; resourceURL != null && i <= numberOfFiles; ) {
      String relativePath = path + fileNameBase + i + "." + fileSuffix;
      if (relativePath.charAt(0) == '/') {
          relativePath = relativePath.substring(1);
      }
      DefaultInputFile inputFile = new DefaultInputFile(relativePath);
      inputFile.setAbsolutePath(resourceURL.getPath());
      inputFile.setLanguage(fileSuffix);
      inputFiles.add(inputFile);
      resourceURL = FileTestUtils.class.getResource(path + fileNameBase + (++i) + "." + fileSuffix);
    }
    return inputFiles;
  }

  public static List<String> getContentOfFiles(String path, String fileNameBase, int numberOfFiles) throws IOException {
    List<String> contentOfFiles = new ArrayList<String>();

    URL resourceURL = FileTestUtils.class.getResource(path + fileNameBase + "1.scala");
    for (int i = 1; resourceURL != null && i <= numberOfFiles; ) {
      try {
        contentOfFiles.add(FileUtils.readFileToString(new File(resourceURL.getFile()), Charset.defaultCharset().toString()));
      } catch (IOException ioe) {
        LOGGER.error("Unexpected I/O exception occurred", ioe);
        throw ioe;
      }
      resourceURL = FileTestUtils.class.getResource(path + fileNameBase + (++i) + ".scala");
    }

    return contentOfFiles;
  }
}
