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
package com.itrex.navigator.graph.path.iterator;

import com.itrex.navigator.graph.path.element.RankingPathElement;
import com.itrex.navigator.graph.path.element.RankingPathElementList;
import org.jgrapht.Graph;
import org.jgrapht.Graphs;

import java.util.*;

/*
 *  Analog of org.jgrapht.alg.shortestpath.KShortestSimplePathsIterator class.
 *  Changed class modifier to public and deleted pathValidator field.
 */
public class DefaultAllPathsIterator<V, E> implements Iterator<Set<V>> {

    private V endVertex;

    private Graph<V, E> graph;

    private Set<V> prevImprovedVertices;

    private Map<V, RankingPathElementList<V, E>> prevSeenDataContainer;

    private Map<V, RankingPathElementList<V, E>> seenDataContainer;

    private V startVertex;

    private boolean startVertexEncountered;

    private int passNumber = 1;


    public DefaultAllPathsIterator(Graph<V, E> graph, V startVertex, V endVertex) {
        this.graph = graph;
        this.startVertex = startVertex;
        this.endVertex = endVertex;
        this.seenDataContainer = new HashMap<>();
        this.prevSeenDataContainer = new HashMap<>();
        this.prevImprovedVertices = new HashSet<>();
    }

    @Override
    public boolean hasNext() {
        if (!this.startVertexEncountered) {
            encounterStartVertex();
        }

        return !this.prevImprovedVertices.isEmpty();
    }

    @Override
    public Set<V> next() {
        if (!this.startVertexEncountered) {
            encounterStartVertex();
        }

        if (hasNext()) {
            Set<V> improvedVertices = new HashSet<>();

            for (V vertex : this.prevImprovedVertices) {
                if (!vertex.equals(this.endVertex)) {
                    updateOutgoingVertices(vertex, improvedVertices);
                }
            }

            savePassData(improvedVertices);
            this.passNumber++;

            return improvedVertices;
        }
        throw new NoSuchElementException();
    }

    public RankingPathElementList<V, E> getPathElements(V endVertex) {
        return this.seenDataContainer.get(endVertex);
    }

    private RankingPathElementList createSeenData(V vertex, E edge) {
        V oppositeVertex = Graphs.getOppositeVertex(this.graph, edge, vertex);

        RankingPathElementList<V, E> oppositeData = this.prevSeenDataContainer.get(oppositeVertex);

        return new RankingPathElementList(this.graph, oppositeData, edge, this.endVertex);
    }

    private void encounterStartVertex() {
        RankingPathElementList<V, E> data = new RankingPathElementList(this.graph, new RankingPathElement<>(this.startVertex));

        this.seenDataContainer.put(this.startVertex, data);
        this.prevSeenDataContainer.put(this.startVertex, data);
        this.prevImprovedVertices.add(this.startVertex);
        this.startVertexEncountered = true;
    }

    private void savePassData(Set<V> improvedVertices) {
        for (V vertex : improvedVertices) {
            RankingPathElementList<V, E> pathElementList = this.seenDataContainer.get(vertex);

            RankingPathElementList<V, E> improvedPaths = new RankingPathElementList(this.graph, vertex);

            for (RankingPathElement<V, E> path : pathElementList) {
                if (path.getHopCount() == this.passNumber) {
                    improvedPaths.pathElements.add(path);
                }
            }

            this.prevSeenDataContainer.put(vertex, improvedPaths);
        }

        this.prevImprovedVertices = improvedVertices;
    }

    private boolean tryToAddFirstPaths(V vertex, E edge) {
        RankingPathElementList<V, E> data = createSeenData(vertex, edge);

        if (!data.isEmpty()) {
            this.seenDataContainer.put(vertex, data);
            return true;
        }
        return false;
    }

    private boolean tryToAddNewPaths(V vertex, E edge) {
        RankingPathElementList<V, E> data = this.seenDataContainer.get(vertex);

        V oppositeVertex = Graphs.getOppositeVertex(this.graph, edge, vertex);
        RankingPathElementList<V, E> oppositeData = this.prevSeenDataContainer.get(oppositeVertex);

        return data.addPathElements(oppositeData, edge);
    }

    private void updateOutgoingVertices(V vertex, Set<V> improvedVertices) {
        for (E edge : this.graph.outgoingEdgesOf(vertex)) {
            V vertexReachedByEdge = Graphs.getOppositeVertex(this.graph, edge, vertex);

            if (!vertexReachedByEdge.equals(this.startVertex)) {
                if (this.seenDataContainer.containsKey(vertexReachedByEdge)) {
                    boolean relaxed = tryToAddNewPaths(vertexReachedByEdge, edge);
                    if (relaxed) {
                        improvedVertices.add(vertexReachedByEdge);
                    }
                } else {
                    boolean relaxed = tryToAddFirstPaths(vertexReachedByEdge, edge);
                    if (relaxed) {
                        improvedVertices.add(vertexReachedByEdge);
                    }
                }
            }
        }
    }
}