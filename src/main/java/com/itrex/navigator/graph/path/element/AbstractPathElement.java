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