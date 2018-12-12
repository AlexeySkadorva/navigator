/*
 * (C) Copyright 2007-2018, by France Telecom and Contributors.
 *
 * JGraphT : a free Java graph-theory library
 *
 * See the CONTRIBUTORS.md file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0, or the
 * GNU Lesser General Public License v2.1 or later
 * which is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1-standalone.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR LGPL-2.1-or-later
 */
package com.itrex.navigator.graph.path.element;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;

import java.util.AbstractList;
import java.util.ArrayList;

/*
 *  Analog of org.jgrapht.alg.shortestpath.AbstractPathElementList class.
 *  Changed class modifier to public and deleted maxSize field.
 */
public abstract class AbstractPathElementList<V, E, T extends AbstractPathElement<V, E>> extends AbstractList<T> {

    protected Graph<V, E> graph;

    public ArrayList<T> pathElements = new ArrayList<>();

    protected V vertex;


    protected AbstractPathElementList(Graph<V, E> graph, AbstractPathElementList<V, E, T> elementList, E edge) {
        this.graph = graph;
        this.vertex = Graphs.getOppositeVertex(graph, edge, elementList.getVertex());
    }

    protected AbstractPathElementList(Graph<V, E> graph, T pathElement) {
        this.graph = graph;
        this.vertex = pathElement.getVertex();

        this.pathElements.add(pathElement);
    }

    protected AbstractPathElementList(Graph<V, E> graph, V vertex) {
        this.graph = graph;
        this.vertex = vertex;
    }

    @Override
    public T get(int index) {
        return this.pathElements.get(index);
    }

    public V getVertex() {
        return this.vertex;
    }

    @Override
    public int size() {
        return this.pathElements.size();
    }

}