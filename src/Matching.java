import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;


public class Matching {
	private static int msize = 0;
	public static ArrayList<Edge> matchingEdges = new ArrayList<Edge>();

	public static int matching(Graph g) {
		msize = 0;
		initialGreedyMatch(g);
		System.out.println(msize);
		labelVerticesAsInnerOrOuter(g);
		boolean noAugPath = false;
		while(!noAugPath){
			Queue<Vertex> Q = new LinkedList<>();
			initializeGraph(g, Q);
			noAugPath = true;
			while(!Q.isEmpty()){
				Vertex u = Q.poll();
				for (Edge e : u.Adj) {
					Vertex v = e.otherEnd(u);
					if(!v.seen && v.mate == null){//case 1
						v.parent = u;
						v.seen = true;
						if(canAugmentPath(v)){
							System.out.println("Aug");
							v.type = 'i';
							noAugPath = false;
							processAugPath(v);
							break;
						}
					}
					else if(!v.seen && v.mate != null){//case 2
						v.type = 'i';
						v.parent = u;
						Vertex x = v.mate;
						x.type = 'o';
						x.parent = v;
						v.seen = true;
						x.seen = true;
						Q.add(x);
					}
					else if(v.seen && v.type == 'i'){ // case 3
						continue;
					}
					else if(v.type == 'o' && v.root != u.root){ //case 4
						v.seen = true;
						noAugPath = false;
						processAugPath(u,v);
					}
					else if(v.type == 'o' && v.root == u.root){//case 5
						formBlossom(u,v,g);
					}
					else{
						System.out.println("Unknown case");
					}
				}
			}
			if(Q.isEmpty() && noAugPath)
				break;
		}
		expandBlossoms(g);
		return msize;
	}
	
	private static void expandBlossoms(Graph g) {
		// TODO Auto-generated method stub
		
	}

	private static void formBlossom(Vertex u, Vertex v, Graph g) {
		Vertex lca = findLCA(u,v);
		Vertex blossom = new Vertex(++g.numNodes);
		g.verts.add(blossom);
		Vertex p = u;
		while(p!=lca){
			blossom.innerVerts.add(p);
			p = p.parent;
		}
		p = v;
		while(p!=lca){
			blossom.innerVerts.add(p);
			p = p.parent;
		}
		blossom.innerVerts.add(p);
		for (Vertex vert : blossom.innerVerts) {
			for (Edge e : vert.Adj) {
				Vertex vert2 = e.otherEnd(vert);
				if(!blossom.innerVerts.contains(vert2))
					g.addEdge(blossom.name, vert2.name, e.Weight);
			}
		}
	}

	private static Vertex findLCA(Vertex u, Vertex v) {
		ArrayList<Vertex> ancestors = new ArrayList<Vertex>();
		ancestors.add(u);
		ancestors.add(v);
		Vertex pu = u.parent;
		Vertex pv = v.parent;
		while(pu!= null && pv!= null){
			if(ancestors.contains(pu))
				return pu;
			if(ancestors.contains(pv))
				return pv;
			ancestors.add(pu);
			ancestors.add(pv);
			pu = pu.parent;
			pv = pv.parent;
		}
		while(pu!=null){
			if(ancestors.contains(pu))
				return pu;
			pu = pu.parent;
		}
		while(pv!= null){
			if(ancestors.contains(pv))
				return pv;
			pv = pv.parent;
		}
		return null;
	}

	private static void processAugPath(Vertex u, Vertex v) {
		System.out.println("hmm");
		u.mate = v;
		v.mate = u;
		u.root = null;
		v.root = null;//remember to check this.
		Vertex pu = u.parent;
		Vertex pv = v.parent;
		if(pu!=null && canAugmentPath(pu)){
			processAugPath(pu);
			msize--;
		}
		
		if(pv!=null && canAugmentPath(pv)){
			processAugPath(pv);
			msize--;
		}
		msize++;
	}

	private static boolean canAugmentPath(Vertex pu) {
		Vertex p = pu;
		while(p!= null){
			if(p.root != null)
				return false;
			p = p.parent;
		}
		return true;
	}

	private static void processAugPath(Vertex v) {
		Vertex p = v.parent;
		v.mate = p;
		p.mate = v;
		Vertex x = p.parent;
		v.root = v; 
		p.root = v;
		while(x!= null){
			Vertex nmx = x.parent;
			x.root = v;
			nmx.root = v;
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
