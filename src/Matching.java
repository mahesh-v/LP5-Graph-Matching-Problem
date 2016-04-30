import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;


public class Matching {
	private static int msize = 0;
	public static ArrayList<Edge> matchingEdges = new ArrayList<Edge>();

	public static int matching(Graph g) {
		labelVerticesAsInnerOrOuter(g);
		msize = 0;
		initialGreedyMatch(g);
		Queue<Vertex> Q = new LinkedList<>();
		initializeGraph(g, Q);
		return msize;
	}
	
	private static void initializeGraph(Graph g, Queue<Vertex> Q) {
		for (Vertex v : g) {
			v.seen = false;
			v.parent = null;
			v.root = null;
			if(v.mate== null && v.type == 'o'){
				v.seen = true;
				Q.add(v);
			}
		}
	}
	
	private static void initialGreedyMatch(Graph g) {
		for (Vertex v : g) {
			for (Edge e : v.Adj) {
				Vertex u = e.otherEnd(v);
				if(u.mate == null && v.mate == null){
					u.mate = v;
					v.mate = u;
					matchingEdges.add(e);
					msize++;
					break;
				}
			}
		}
	}

	private static void labelVerticesAsInnerOrOuter(Graph g) {
		Vertex first = g.verts.get(1);
		first.type = 'o';
		Queue<Vertex> queue = new LinkedList<Vertex>();
		queue.add(first);
		while(!queue.isEmpty()){
			Vertex v = queue.poll();
			for (Edge e : v.Adj) {
				Vertex u = e.otherEnd(v);
				if(u.type != 'u')//already set. Not unknown.
					continue;
				if(v.type == 'i')
					u.type = 'o';
				else if(v.type == 'o')
					u.type = 'i';
				queue.add(u);
			}
		}
	}
}
