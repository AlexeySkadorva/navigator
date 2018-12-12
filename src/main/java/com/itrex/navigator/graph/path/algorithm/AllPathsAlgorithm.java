package com.itrex.navigator.graph.path.algorithm;

import com.itrex.navigator.graph.path.iterator.DefaultAllPathsIterator;
import com.itrex.navigator.graph.path.element.RankingPathElementList;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.graph.GraphWalk;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/*
 *  Analog of org.jgrapht.alg.shortestpath.KShortestSimplePaths class.
 *  Deleted pathValidator field and k attribute for getPaths method.
 */
public class AllPathsAlgorithm<V, E> {

    public List<GraphPath<V, E>> getPaths(Graph<V, E> graph, V startVertex, V endVertex) {
        DefaultAllPathsIterator<V, E> iterator = new DefaultAllPathsIterator<>(graph, startVertex, endVertex);
        int nMaxHops = graph.vertexSet().size() - 1;

        for (int passNumber = 1; passNumber <= nMaxHops && iterator.hasNext(); passNumber++) {
            iterator.next();
        }

        RankingPathElementList<V, E> pathElements = iterator.getPathElements(endVertex);

        if (pathElements == null) {
            return Collections.emptyList();
        }

        return pathElements.stream()
            .map(e -> new GraphWalk<V, E>(graph, startVertex, e.getVertex(), null, e.createEdgeListPath(), e.getWeight()))
            .collect(Collectors.toList());
    }

}