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
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.MaskSubgraph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/*
 *  Analog of org.jgrapht.alg.shortestpath.RankingPathElementList class.
 *  Changed class modifier to public, deleted maxSize and externalPathValidator fields.
 */
public final class RankingPathElementList<V, E> extends AbstractPathElementList<V, E, RankingPathElement<V, E>> {

    private V guardVertexToNotDisconnect = null;

    private Map<RankingPathElement<V, E>, Boolean> path2disconnect = new HashMap<>();


    public RankingPathElementList(Graph<V, E> graph, RankingPathElement<V, E> pathElement) {
        super(graph, pathElement);
    }

    public RankingPathElementList(Graph<V, E> graph, RankingPathElementList<V, E> elementList, E edge, V guardVertexToNotDisconnect) {
        super(graph, elementList, edge);
        this.guardVertexToNotDisconnect = guardVertexToNotDisconnect;

        for (RankingPathElement<V, E> prevPathElement : elementList) {
            if (isNotValidPath(prevPathElement, edge)) {
                continue;
            }

            double weight = calculatePathWeight(prevPathElement, edge);
            RankingPathElement<V, E> newPathElement =
                new RankingPathElement<>(this.graph, prevPathElement, edge, weight);

            this.pathElements.add(newPathElement);
        }
    }

    public RankingPathElementList(Graph<V, E> graph, V vertex) {
        super(graph, vertex);
    }

    public boolean addPathElements(RankingPathElementList<V, E> elementList, E edge) {
        assert (this.vertex.equals(Graphs.getOppositeVertex(this.graph, edge, elementList.getVertex())));

        boolean pathAdded = false;

        for (int vIndex = 0, yIndex = 0; vIndex < elementList.size(); vIndex++) {
            RankingPathElement<V, E> prevPathElement = elementList.get(vIndex);

            if (isNotValidPath(prevPathElement, edge)) {
                continue;
            }
            double newPathWeight = calculatePathWeight(prevPathElement, edge);
            RankingPathElement<V, E> newPathElement =
                new RankingPathElement<>(this.graph, prevPathElement, edge, newPathWeight);

            RankingPathElement<V, E> yPathElement = null;
            for (; yIndex < size(); yIndex++) {
                yPathElement = get(yIndex);

                if (newPathWeight < yPathElement.getWeight()) {
                    this.pathElements.add(yIndex, newPathElement);
                    pathAdded = true;

                    break;
                }

                if (newPathWeight == yPathElement.getWeight()) {
                    this.pathElements.add(yIndex + 1, newPathElement);
                    pathAdded = true;

                    break;
                }
            }

            if (newPathWeight > yPathElement.getWeight()) {
                this.pathElements.add(newPathElement);
                pathAdded = true;
            } else {
                break;
            }
        }

        return pathAdded;
    }

    private double calculatePathWeight(RankingPathElement<V, E> pathElement, E edge) {
        double pathWeight = this.graph.getEdgeWeight(edge);

        if ((pathElement.getPrevEdge() != null)) {
            pathWeight += pathElement.getWeight();
        }

        return pathWeight;
    }

    private boolean isGuardVertexDisconnected(RankingPathElement<V, E> prevPathElement) {
        if (this.guardVertexToNotDisconnect == null) {
            return false;
        }

        if (this.path2disconnect.containsKey(prevPathElement)) {
            return this.path2disconnect.get(prevPathElement);
        }

        ConnectivityInspector<V, E> connectivityInspector;
        PathMask<V, E> connectivityMask = new PathMask<>(prevPathElement);

        MaskSubgraph<V, E> connectivityGraph = new MaskSubgraph<>(
            this.graph, connectivityMask::isVertexMasked, connectivityMask::isEdgeMasked);
        connectivityInspector = new ConnectivityInspector<>(connectivityGraph);

        if (connectivityMask.isVertexMasked(this.guardVertexToNotDisconnect)) {
            this.path2disconnect.put(prevPathElement, true);
            return true;
        }

        if (!connectivityInspector.pathExists(this.vertex, this.guardVertexToNotDisconnect)) {
            this.path2disconnect.put(prevPathElement, true);
            return true;
        }

        this.path2disconnect.put(prevPathElement, false);
        return false;
    }

    private boolean isNotValidPath(RankingPathElement<V, E> prevPathElement, E edge) {
        return !isSimplePath(prevPathElement, edge) || isGuardVertexDisconnected(prevPathElement);
    }

    private boolean isSimplePath(RankingPathElement<V, E> prevPathElement, E edge) {
        V endVertex = Graphs.getOppositeVertex(this.graph, edge, prevPathElement.getVertex());
        assert (endVertex.equals(this.vertex));

        RankingPathElement<V, E> pathElementToTest = prevPathElement;
        do {
            if (pathElementToTest.getVertex().equals(endVertex)) {
                return false;
            } else {
                pathElementToTest = pathElementToTest.getPrevPathElement();
            }
        } while (pathElementToTest != null);

        return true;
    }

    private static class PathMask<V, E> {
        private Set<E> maskedEdges;

        private Set<V> maskedVertices;

        PathMask(RankingPathElement<V, E> pathElement) {
            this.maskedEdges = new HashSet<>();
            this.maskedVertices = new HashSet<>();

            while (pathElement.getPrevEdge() != null) {
                this.maskedEdges.add(pathElement.getPrevEdge());
                this.maskedVertices.add(pathElement.getVertex());
                pathElement = pathElement.getPrevPathElement();
            }
            this.maskedVertices.add(pathElement.getVertex());
        }

        public boolean isEdgeMasked(E edge) {
            return this.maskedEdges.contains(edge);
        }

        public boolean isVertexMasked(V vertex) {
            return this.maskedVertices.contains(vertex);
        }
    }

}