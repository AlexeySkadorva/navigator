package com.itrex.navigator.graph.path.element;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;

import java.util.List;

/*
 *  Analog of org.jgrapht.alg.shortestpath.RankingPathElement class.
 *  Changed class modifier to public.
 */
public final class RankingPathElement<V, E> extends AbstractPathElement<V, E> implements GraphPath<V, E> {

    private double weight;

    private Graph<V, E> graph;


    public RankingPathElement(Graph<V, E> graph, RankingPathElement<V, E> pathElement, E edge, double weight) {
        super(graph, pathElement, edge);
        this.weight = weight;
        this.graph = graph;
    }

    public RankingPathElement(V vertex) {
        super(vertex);
        this.weight = 0;
    }

    public double getWeight() {
        return this.weight;
    }

    @Override
    public RankingPathElement<V, E> getPrevPathElement() {
        return (RankingPathElement<V, E>) super.getPrevPathElement();
    }

    @Override
    public Graph<V, E> getGraph() {
        return this.graph;
    }

    @Override
    public V getStartVertex() {
        if (getPrevPathElement() == null) {
            return super.getVertex();
        }
        return getPrevPathElement().getStartVertex();
    }

    @Override
    public V getEndVertex() {
        return super.getVertex();
    }

    @Override
    public List<E> getEdgeList() {
        return super.createEdgeListPath();
    }

}