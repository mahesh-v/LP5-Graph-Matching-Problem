import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;


/**
 * Optimal matching in bipartite graphs
 * 
 * @author Darshan Narayana Reddy and Mahesh Venkateswaran
 *
 */
public class BipartiteMatching {
	private static int msize = 0;
	public static ArrayList<Edge> matchingEdges = new ArrayList<Edge>();

	/**
	 * Returns the cardinality of the graph. -1 if not a bipartite graph.
	 * 
	 * @param g Input graph
	 * @return Cardinality of maximum matching for the given graph
	 */
	public static int matching(Graph g) {
		if(!labelVerticesAsInnerOrOuter(g)){
			System.out.println("G is not bipartite");
			return -1;
		}
		msize = 0;
		initialGreedyMatch(g);
		boolean noAugPath = false;
		while(!noAugPath){
			Queue<Vertex> Q = new LinkedList<Vertex>();
			initializeGraph(g, Q);
			noAugPath = true;
			while(!Q.isEmpty()){
				Vertex u = Q.poll();
				for (Edge e : u.Adj) {
					Vertex v= e.otherEnd(u);
					if(v.seen)
						continue;
					v.parent = u;
					v.seen = true;
					if(v.mate == null){
						if(processAugPath(v))//returns false if unable to process due to conflict
							noAugPath = false;
						break;
					}
					else{
						Vertex x = v.mate;
						x.seen = true;
						x.parent = v;
						Q.add(x);
					}
				}
			}
			if(Q.isEmpty() && noAugPath)
				break;
		}
		return msize;
	}

	/**
	 * Set parents as null, seen as false, and root as null.
	 * Add unmatched outer edges to the queue
	 * 
	 * @param g
	 * @param Q
	 */
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

	/**
	 * Returns false if the path contains overlaps with 
	 * another path that has already been augmented
	 * Else, augments the path starting from vertex u.
	 * 
	 * @param u
	 * @return true if successful
	 */
	private static boolean processAugPath(Vertex u) {
		Vertex p = u;
		while(p!= null){
			if(p.root != null)
				return false;
			p = p.parent;
		}
		p=u.parent;
		Vertex x = p.parent;
		u.mate = p;
		p.mate = u;
		u.root = u;
		p.root = u;
		while(x!= null){
			Vertex nmx = x.parent;
			Vertex y = nmx.parent;
			x.mate = nmx;
			nmx.mate = x;
			x.root = u;
			nmx.root = u;
			x=y;
		}
		msize++;
		return true;
	}

	/**
	 * Finds an initial greedy match for the given graph
	 * 
	 * @param g
	 */
	private static void initialGreedyMatch(Graph g) {
		for (Vertex v : g) {
			if(v.mate!=null)
				continue;
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

	/**
	 * Labels vertices as inner or outer.
	 * Returns false if it is not bipartite
	 * 
	 * @param g input graph
	 * @return true if labelling was successful
	 */
	private static boolean labelVerticesAsInnerOrOuter(Graph g) {
		Vertex first = g.verts.get(1);
		first.type = 'o';
		Queue<Vertex> queue = new LinkedList<Vertex>();
		queue.add(first);
		while(!queue.isEmpty()){
			Vertex v = queue.poll();
			for (Edge e : v.Adj) {
				Vertex u = e.otherEnd(v);
				if(u.type == 'u') {//unseen
					if(v.type == 'i')
						u.type = 'o';
					else if(v.type == 'o')
						u.type = 'i';
					queue.add(u);
				}
				else if((v.type == 'o' && u.type == 'o') || (v.type == 'i' && u.type == 'i'))//not bipartite
					return false;
			}
		}
		return true;
	}

}
