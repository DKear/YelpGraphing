//Edge class, edges are between businesses, so they keep a reference of the source and the destination. Weight is the similarity between nodes.
public class Edge {
    public Business source;
    public Business destination;
    public double weight;

    public Edge(Business s, Business d, double w){
        source = s;
        destination = d;
        weight = w;
    }

}
