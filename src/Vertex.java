/**
 * Class to represent a vertex of a graph
 * 
 *
 */

import java.util.ArrayList;
import java.util.List;

public class Vertex {
    public int name; // name of the vertex
    public boolean seen; // flag to check if the vertex has already been visited
    public Vertex parent; // parent of the vertex
    public int distance; // distance to the vertex from the source vertex
    public List<Edge> Adj, revAdj; // adjacency list; use LinkedList or ArrayList
    public char type;//takes 'i' for inner and 'o' for outer and 'u' for unknown
    public Vertex mate;
    public Vertex root;
    public boolean active;
    public ArrayList<Vertex> innerVerts;
    public int L;//label weight of node
    public char card_type;

    /**
     * Constructor for the vertex
     * 
     * @param n
     *            : int - name of the vertex
     */
    Vertex(int n) {
	name = n;
	seen = false;
	parent = null;
	Adj = new ArrayList<Edge>();
	revAdj = new ArrayList<Edge>();   /* only for directed graphs */
	type = 'u'; //initially unknown. Used for BiPartite graphs
	mate = null;
	active = true;
	innerVerts = new ArrayList<Vertex>();
    }

    /**
     * Method to represent a vertex by its name
     */
    public String toString() {
	return Integer.toString(name);
    }
}