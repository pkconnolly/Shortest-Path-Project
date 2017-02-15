import java.util.ArrayList;

public class Edge{
	ArrayList<String> path;
	int weight;

	public Edge(int w, ArrayList<String> p){
		weight = w;
		path = p;
	}

	public Edge(int w){
		weight = w;
		path = null;
	}

	public int getWeight(){
		//System.out.println("gw: " + weight);
		return weight;
	}

	public ArrayList<String> getPath(){
		return path;
	}

	public String toString(){
		return "w:" + weight;// + " p:" + path;
	}

}