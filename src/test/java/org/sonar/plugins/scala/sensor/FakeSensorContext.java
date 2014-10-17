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

import com.google.common.collect.Sets;
import org.sonar.api.batch.Event;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.design.Dependency;
import org.sonar.api.measures.Measure;
import org.sonar.api.measures.MeasuresFilter;
import org.sonar.api.measures.Metric;
import org.sonar.api.resources.ProjectLink;
import org.sonar.api.resources.Resource;
import org.sonar.api.rules.Violation;

import javax.annotation.CheckForNull;
import java.io.Serializable;
import java.lang.Deprecated;
import java.lang.Double;
import java.lang.Override;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * This fake helps to detect duplicate resources in tests.
 */
class FakeSensorContext implements SensorContext {

  private final Set<Resource> resources = Sets.newHashSet();

  public void saveSource(Resource reference, String source) {
    if (resources.contains(reference)) {
      throw new RuntimeException("Duplicate resources in sensor context are not allowed!");
    }
    resources.add(reference);
  }

  @Deprecated
  public boolean index(Resource resource) { return false; }

  @Deprecated
  public boolean index(Resource resource, Resource parentReference) {
    return false;
  }

  @Deprecated
  public boolean isExcluded(Resource reference) {
    return false;
  }

  @Deprecated
  public boolean isIndexed(Resource reference, boolean acceptExcluded) {
    return false;
  }

  @Override
  public <R extends Resource> R getResource(R reference) {
    return null;
  }

  @Override
  public Resource getParent(Resource reference) {
    return null;
  }

  @Override
  public Collection<Resource> getChildren(Resource reference) {
    return null;
  }

  @Override
  public <G extends Serializable> Measure<G> getMeasure(Metric<G> metric) { return null; }

  @Override
  public <M> M getMeasures(MeasuresFilter<M> filter) {
    return null;
  }

  @Override
  public Measure saveMeasure(Measure measure) {
    return null;
  }

  @Override
  public Measure saveMeasure(Metric metric, Double value) {
    return null;
  }

  @Override
  public <G extends Serializable> Measure<G> getMeasure(Resource resource, Metric<G> metric) {
    return null;
  }

  @Deprecated
  public String saveResource(Resource resource) {
    return null;
  }

  @Override
  public <M> M getMeasures(Resource resource, MeasuresFilter<M> filter) {
    return null;
  }

  @Override
  public Measure saveMeasure(Resource resource, Metric metric, Double value) {
    return null;
  }

  @Override
  public Measure saveMeasure(Resource resource, Measure measure) {
    return null;
  }

  @Deprecated
  public void saveViolation(Violation violation, boolean force) { }

  @Deprecated
  public void saveViolation(Violation violation) { }

  @Deprecated
  public void saveViolations(Collection<Violation> violations) { }

  @Override
  public Dependency saveDependency(Dependency dependency) {
    return null;
  }

  @Override
  public Set<Dependency> getDependencies() {
    return null;
  }

  @Override
  public Collection<Dependency> getIncomingDependencies(Resource to) {
    return null;
  }

  @Override
  public Collection<Dependency> getOutgoingDependencies(Resource from) {
    return null;
  }

  @Override
  public void saveLink(ProjectLink link) { }

  @Override
  public void deleteLink(String key) { }

  @Override
  public List<Event> getEvents(Resource resource) {
    return null;
  }

  @Override
  public Event createEvent(Resource resource, String name, String description, String category, Date date) { return null; }

  @Override
  public void deleteEvent(Event event) { }

  @Override
  public Measure saveMeasure(InputFile inputFile, Metric metric, Double aDouble) { return null;}

  @Override
  public Measure saveMeasure(InputFile inputFile, Measure measure) { return null; }

}
