import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;


public class Matching {
	private static int msize = 0;
	public static ArrayList<Edge> matchingEdges = new ArrayList<Edge>();

	public static int matching(Graph g) {
		msize = 0;
		initialGreedyMatch(g);
		labelVerticesAsInnerOrOuter(g);
		Queue<Vertex> Q = new LinkedList<>();
		initializeGraph(g, Q);
		while(!Q.isEmpty()){
			Vertex u = Q.poll();
			for (Edge e : u.Adj) {
				Vertex v = e.otherEnd(u);
				if(v.mate == null){//case 1
					v.type = 'i';
					v.parent = u;
					processAugPath(v);
				}
				if(!v.seen && v.mate != null){//case 2
					v.type = 'i';
					v.parent = u;
					v.root = u;
					Vertex x = v.mate;
					x.type = 'o';
					x.parent = v;
					x.root = u;
					v.seen = true;
					x.seen = true;
					Q.add(x);
				}
				if(v.seen && v.type == 'i') // case 3
					continue;
				if(v.type == 'o' && v.root != u.root){ //case 4
					processAugPath(u,v);
				}
				if(v.type == 'o' && v.root == u.root){//case 5
					formBlossom(u,v);
				}
			}
		}
		expandBlossoms(g);
		return msize;
	}
	
	private static void expandBlossoms(Graph g) {
		// TODO Auto-generated method stub
		
	}

	private static void formBlossom(Vertex u, Vertex v) {
		// TODO Auto-generated method stub
		
	}

	private static void processAugPath(Vertex u, Vertex v) {
		// TODO Auto-generated method stub
		
	}

	private static void processAugPath(Vertex v) {
		// TODO Auto-generated method stub
		
	}

	private static void initializeGraph(Graph g, Queue<Vertex> Q) {
		for (Vertex v : g) {
			v.seen = false;
			v.parent = null;
			v.root = null;
			if(v.mate== null && v.type == 'o'){
				v.seen = true;
				v.root = v;
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
