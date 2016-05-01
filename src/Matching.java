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
				if(v.mate == u)
					continue;
				if(v.mate == null){//case 1
					v.type = 'i';
					v.parent = u;
					processAugPath(v);
				}
				else if(!v.seen && v.mate != null){//case 2
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
				else if(v.seen && v.type == 'i') // case 3
					continue;
				else if(v.type == 'o' && v.root != u.root){ //case 4
					processAugPath(u,v);
				}
				else if(v.type == 'o' && v.root == u.root){//case 5
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
		System.out.println("Blossom");
	}

	private static void processAugPath(Vertex u, Vertex v) {
		u.mate = v;
		v.mate = u;
		u.root = null;
		v.root = null;//remember to check this.
		Vertex pu = u.parent;
		if(pu!=null)
			processAugPath(pu);
		Vertex pv = v.parent;
		if(pv!=null)
			processAugPath(pv);
		msize--;
	}

	private static void processAugPath(Vertex v) {
		Vertex p = v.parent;
		v.mate = p;
		p.mate = v;
		Vertex x = p.parent;
		v.root = null;//check
		p.root = null;
		while(x!= null){
			Vertex nmx = x.parent;
			x.root = null;
			nmx.root = null;
			x.mate = nmx;
			nmx.mate = x;
			x = nmx.parent;
		}
		msize++;
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
//			if(v.name == 1)
//				continue;
			for (Edge e : v.Adj) {
				Vertex u = e.otherEnd(v);
//				if(u.name == 1) continue;
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
