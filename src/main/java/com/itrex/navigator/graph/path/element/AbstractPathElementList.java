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