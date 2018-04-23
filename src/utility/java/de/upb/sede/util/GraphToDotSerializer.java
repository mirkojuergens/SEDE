package de.upb.sede.util;

import de.upb.sede.composition.graphs.CompositionGraph;
import de.upb.sede.composition.graphs.Edge;
import de.upb.sede.composition.graphs.GraphTraversal;
import de.upb.sede.composition.graphs.nodes.BaseNode;
import info.leadinglight.jdot.Graph;
import info.leadinglight.jdot.Node;
import info.leadinglight.jdot.enums.Shape;

public class GraphToDotSerializer {

	public static String getDOTForGraph(CompositionGraph compGraph) {
		Graph graph = new Graph();
		for (BaseNode baseNode : GraphTraversal.iterateNodes(compGraph)) {
			Node dotNode = new Node(baseNode.toString()).setShape(Shape.circle);
			graph.addNode(dotNode);
		}
		for (Edge edge : GraphTraversal.iterateEdges(compGraph)) {
			BaseNode fromNode = edge.getFrom();
			BaseNode toNode = edge.getTo();
			info.leadinglight.jdot.Edge dotEdge = new info.leadinglight.jdot.Edge();
			dotEdge.addNode(fromNode.toString()).addNode(toNode.toString());
			graph.addEdge(dotEdge);
		}
		return graph.toDot();
	}
}
