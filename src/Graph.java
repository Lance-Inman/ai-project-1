package graphcoloring;

import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Random;

// ***********************************************************
// For this project, you need to write a “problem generator”
// whereby you will create several graphs of various sizes
// to solve instances of the graph-coloring problem. For
// your graph generator, scatter n points on the unit square
// (where n will be provided as input), select some point X
// at random and connectXby a straight line to the nearest
// point Y such thatXis not already connected to Y and line
// crosses no other line. Repeat the previous step until no
// more connections are possible. The points represent
// regions on the map, and the lines connect neighbors.
// ***********************************************************

/**
 * The {@code Graph} class generates an uncolored graph
 * and connects vertices to their closes neighbor at
 * random until no more connections are possible.
 *
 * @author Lance Inman
 * @version 0.1.0
 * @since 2016-09-05
 */
public class Graph {
    private ArrayList<Vertex> vertices;

    public Graph(int size) {
        populateGraph(size);
        populateNeighbors();
        connectClosestNeighbors();
    }
    public Graph()
    {
        vertices = new ArrayList<Vertex>();
    }
    
    public ArrayList<Vertex> getVertices()
    {
        return vertices;
    }

    public void setColor(Vertex v, int n) {
        v.setColor(n);
    }

    private void populateGraph(int n) {
        Random rnd = new Random();
        for(int i = 0; i < n; i++) {
            Vertex v = new Vertex(rnd.nextInt(n), rnd.nextInt(n));
            vertices.add(v);
        }
    }

    private double distance(Vertex v1, Vertex v2) {
        return Math.sqrt(Math.pow((v1.getX() - v2.getX()), 2) + Math.pow((v1.getY() - v2.getY()), 2));
    }

    /** Finds the closest neighbor to each vertex in the graph.
     */
    private void populateNeighbors() {
        for(int i = 0; i < vertices.size(); i++) {
            Vertex v1 = vertices.get(i);
            Vertex v1Neighbor = null;
            double shortestDist = -1;

            for(int j = 0; j < vertices.size(); j++) {
                if(j != i) {
                    Vertex v2 = vertices.get(j);
                    if(distance(v1, v2) < shortestDist || shortestDist == -1) {
                        v1Neighbor = v2;
                        shortestDist = distance(v1, v2);
                    }
                }
            }

            v1.setClosestNeighbor(v1Neighbor);
        }
    }

    private boolean connect(Vertex v1, Vertex v2) {
        if(!intersects(v1, v2)) {
            v1.getNeighbors().add(v2);
            v2.getNeighbors().add(v1);

            if (v2 == v1.getClosestNeighbor()) {
                v1.setConnectedToClosestNeighbor(true);
            }
            if (v1 == v2.getClosestNeighbor()) {
                v2.setConnectedToClosestNeighbor(true);
            }
            return true;
        } else {
            return false;
        }
    }

    private void connectClosestNeighbors() {
        for(Vertex v: vertices) {
            if(v.getNeighbors().contains(v.getClosestNeighbor())) {
                v.setConnectedToClosestNeighbor(true);
            } else {
                connect(v, v.getClosestNeighbor());
            }
        }
    }

    /** Checks if a line segment from v1 to v2 would
     * intersect an existing connection in the graph.
     *
     * @param v1 the first vertex in the connection
     * @param v2 the second vertex in the connection
     * @return Return true if a line segment from v1 to v2 would
     * intersect an existing connection in g, false otherwise.
     */
    private boolean intersects(Vertex v1, Vertex v2) {
        for(Vertex v3: vertices) {
            if(v1 != v3 && v2 != v3) {
                for(Vertex v4: v3.getNeighbors()) {
                    if(Line2D.linesIntersect(
                            v1.getX(), v1.getY(),
                            v2.getX(), v2.getY(),
                            v3.getX(), v3.getY(),
                            v4.getX(), v4.getY())) {
                        return true;
                    }
                }
            }
        }

        return false;
    }
}

