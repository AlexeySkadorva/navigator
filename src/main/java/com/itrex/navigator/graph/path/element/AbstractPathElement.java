/*
 * (C) Copyright 2006-2018, by France Telecom and Contributors.
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/*
 *  Analog of org.jgrapht.alg.shortestpath.AbstractPathElement class. Changed class modifier to public.
 */
public abstract class AbstractPathElement<V, E> {

    private int nHops;

    private E prevEdge;

    private AbstractPathElement<V, E> prevPathElement;

    private V vertex;


    protected AbstractPathElement(Graph<V, E> graph, AbstractPathElement<V, E> pathElement, E edge) {
        this.vertex = Graphs.getOppositeVertex(graph, edge, pathElement.getVertex());
        this.prevEdge = edge;
        this.prevPathElement = pathElement;

        this.nHops = pathElement.getHopCount() + 1;
    }

    protected AbstractPathElement(V vertex) {
        this.vertex = vertex;
        this.prevEdge = null;
        this.prevPathElement = null;

        this.nHops = 0;
    }

    public List<E> createEdgeListPath() {
        List<E> path = new ArrayList<>();
        AbstractPathElement<V, E> pathElement = this;

        while (pathElement.getPrevEdge() != null) {
            path.add(pathElement.getPrevEdge());

            pathElement = pathElement.getPrevPathElement();
        }

        Collections.reverse(path);

        return path;
    }

    public int getHopCount() {
        return this.nHops;
    }

    public E getPrevEdge() {
        return this.prevEdge;
    }

    public AbstractPathElement<V, E> getPrevPathElement() {
        return this.prevPathElement;
    }

    public V getVertex() {
        return this.vertex;
    }

}