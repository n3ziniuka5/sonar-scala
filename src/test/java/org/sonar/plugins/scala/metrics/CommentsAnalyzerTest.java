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
package org.sonar.plugins.scala.metrics;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.sonar.plugins.scala.language.Comment;
import org.sonar.plugins.scala.language.CommentType;

public class CommentsAnalyzerTest {

  @Test
  public void shouldCountAllCommentLines() throws IOException {
    List<String> comments = Arrays.asList(
            "// this a normal comment",
            "/* this is a normal multiline coment\r\n* last line of this comment */",
            "// also a normal comment");
    CommentsAnalyzer commentAnalyzer = new CommentsAnalyzer(asCommentList(comments, CommentType.NORMAL));
    assertThat(commentAnalyzer.countCommentLines(), is(4));
  }

  @Test
  public void shouldCountAllHeaderCommentLines() throws IOException {
    List<String> comments = Arrays.asList(
        "/* this is an one line header comment */",
        "/* this is a normal multiline header coment\r\n* last line of this comment */",
        "/* also a normal header comment */");
    CommentsAnalyzer commentAnalyzer = new CommentsAnalyzer(asCommentList(comments, CommentType.HEADER));
    assertThat(commentAnalyzer.countHeaderCommentLines(), is(4));
  }

  @Test
  public void shouldCountZeroCommentLinesForEmptyCommentsList() {
    CommentsAnalyzer commentAnalyzer = new CommentsAnalyzer(Collections.<Comment>emptyList());
    assertThat(commentAnalyzer.countCommentLines(), is(0));
  }

  @Test
  public void shouldCountZeroHeaderCommentLinesForEmptyCommentsList() {
    CommentsAnalyzer commentAnalyzer = new CommentsAnalyzer(Collections.<Comment>emptyList());
    assertThat(commentAnalyzer.countHeaderCommentLines(), is(0));
  }

  private List<Comment> asCommentList(List<String> commentsContent, CommentType type) throws IOException {
    List<Comment> comments = new ArrayList<Comment>();
    for (String comment : commentsContent) {
      comments.add(new Comment(comment, type));
    }
    return comments;
  }
}