package com.agritsik.patterns.cor;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class DependencyBuilder {

    private Executor executor;
    private Map<Node, List<Edge>> stages;

    public DependencyBuilder(Executor executor, Map<Node, List<Edge>> graph) {
        this.executor = executor;
        this.stages = graph;
    }

    int schedule(String req, String res) {
        return executor.execute(stages, req, res);
    }
}

/**
 * Represents a node of the graph
 */
interface Node {
    boolean execute(String req, String res);
}

/**
 * Represents an edge of the graph
 */
class Edge {
    private Node child;
    private EdgeType type;

    public Edge(Node child) {
        this(child, EdgeType.REQUIRED);
    }

    public Edge(Node child, EdgeType type) {
        this.child = child;
        this.type = type;
    }

    public Node getChild() {
        return child;
    }

    public EdgeType getType() {
        return type;
    }
}

enum EdgeType {
    REQUIRED, OPTIONAL
}

/**
 * Traverses the graph and executes each node
 */
interface Executor {
    int execute(Map<Node, List<Edge>> stages, String req, String res);
}


class KahnExecutor implements Executor {

    @Override
    public int execute(Map<Node, List<Edge>> map, String req, String res) {

        // build a counter of incoming edges
        HashMap<Node, Integer> incoming = new HashMap<>();
        for(var node : map.keySet()) incoming.putIfAbsent(node, 0);
        for (Map.Entry<Node, List<Edge>> entry : map.entrySet()) {
            for (Edge edge : entry.getValue()) {
                incoming.putIfAbsent(edge.getChild(), 0);
                incoming.put(edge.getChild(), incoming.get(edge.getChild()) + 1);
            }
        }

        LinkedList<Node> q = new LinkedList<>();

        // add vertices without incoming edges
        for (var entry : incoming.entrySet()) {
            if (entry.getValue() == 0) {
                q.add(entry.getKey());
            }
        }

        // bfs
        int countCompletedNodes = 0;
        while (!q.isEmpty()) {
            Node cur = q.poll();

            // execute each stage
            boolean completed = cur.execute(req, res);
            if (completed) countCompletedNodes++;

            if(!map.containsKey(cur)) continue;
            for (var edge : map.get(cur)) {

                // if the current stage has been failed and the edge is required
                if (!completed && edge.getType() == EdgeType.REQUIRED) {
                    continue;
                }

                Node child = edge.getChild();
                incoming.put(child, incoming.get(child) - 1);
                if (incoming.get(child) == 0) {
                    q.add(child);
                }
            }
        }

        return countCompletedNodes;
    }


    // ========= main =========

    /**
     * A   B
     * \ /
     * C
     * | \
     * D K
     */
    public static void main(String[] args) {

        Map<Node, List<Edge>> graph = new HashMap<>();
        Node a = (req, res) -> {
            System.out.println("A");
            return true;
        };

        Node b = (req, res) -> {
            System.out.println("B");
            return true;
        };

        Node c = (req, res) -> {
            System.out.println("C");
            return true;
        };

        Node d = (req, res) -> {
            System.out.println("D");
            return true;
        };

        Node k = (req, res) -> {
            System.out.println("K");
            return true;
        };

        graph.put(a, List.of(new Edge(c)));
        graph.put(b, List.of(new Edge(c)));
        graph.put(c, List.of(new Edge(d), new Edge(k)));

        // execute
        DependencyBuilder dependencyBuilder = new DependencyBuilder(new KahnExecutor(), graph);
        int count = dependencyBuilder.schedule("", "");

        System.out.println("executed: " + count);
    }

}

