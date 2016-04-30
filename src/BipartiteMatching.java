import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;


public class BipartiteMatching {
	private static int msize = 0;
	public static ArrayList<Edge> matchingEdges = new ArrayList<Edge>();

	public static int matching(Graph g) {
		if(!labelVerticesAsInnerOrOuter(g)){
			System.out.println("G is not bipartite");
			return -1;
		}
		initialGreedyMatch(g);
		boolean noAugPath = false;
		while(!noAugPath){
			Queue<Vertex> Q = new LinkedList<Vertex>();
			for (Vertex v : g) {
				v.seen = false;
				v.parent = null;
				v.root = null;
				if(v.mate== null && v.level == 'o'){
					v.seen = true;
					Q.add(v);
				}
			}
			noAugPath = true;
			while(!Q.isEmpty()){
				Vertex u = Q.poll();
				Vertex v=null;
				for (Edge e : u.Adj) {
					v= e.otherEnd(u);
					if(!v.seen)
					{
						v.parent = u;
						v.seen = true;
						if(v.mate == null){
							if(processAugPath(v)){
								noAugPath = false;
							}
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
			}
			if(Q.isEmpty() && noAugPath)
				break;
		}
		return msize;
	}

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

	private static boolean labelVerticesAsInnerOrOuter(Graph g) {
		Vertex first = g.verts.get(1);
		first.level = 'o';
		LinkedList<Vertex> queue = new LinkedList<Vertex>();
		queue.add(first);
		while(!queue.isEmpty()){
			Vertex v = queue.removeFirst();
			for (Edge e : v.Adj) {
				Vertex u = e.otherEnd(v);
				if(u.level == 'u') {//unseen
					if(v.level == 'i')
						u.level = 'o';
					else if(v.level == 'o')
						u.level = 'i';
					queue.add(u);
				}
				else if((v.level == 'o' && u.level == 'o') || (v.level == 'i' && u.level == 'i'))//not bipartite
					return false;
			}
		}
		return true;
	}

}
