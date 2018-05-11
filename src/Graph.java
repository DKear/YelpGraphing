//Graph class implementing all the other classes to attempt to implement shortest path find
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

public class Graph {
    public ArrayList<HashSet<Integer>> disjointSets = new ArrayList<>();
    public ArrayList<HashSet<Integer>> tempDisjointSet = new ArrayList<>();
    ArrayList<Edge> edges = new ArrayList<>();

    PriorityQueue queue = new PriorityQueue();
    //destination node
    Business destination = new Business();


    //Method that sets the key to the disjoint set it belongs to
    public void setDisjointKey(ArrayList<HashSet<Integer>> v){


        for(int i = 0; i < queue.businesses.size(); i++){
            Business b = queue.businesses.get(i);
            for(int j = 0; j < v.size(); j++){
                Iterator it = v.get(j).iterator();
                boolean found = false;
                while(it.hasNext()){
                if(b.key == (Integer)it.next()){
                    b.disjointKey = j;
                    found = true;
                    break;
                }
            } if(found){
                    break;
                }
            }
        }

    }

    //Recursively creates sets from businesses
    private HashSet<Integer> createDisjointSet(Business b, HashSet h){

        if(!b.inSet ) {
            h.add(b.key);
            b.inSet = true;

            for (Business.BusinessRef n : b.neighbors) {
                    if (h.add(n.key)) {
                        queue.businesses.get(n.key).inSet = true;
                        for (Business.BusinessRef nn : queue.businesses.get(n.key).neighbors) {
                            createDisjointSet(queue.businesses.get(nn.key), h);
                        }

                    }

            }
        }else if(b.inSet ){
            h.add(b.key);
        }
        return h;
    }


    //Checks if there are common keys in the new set and pre-existing ones
    public boolean containsCommonElement(HashSet<Integer> s1, HashSet<Integer> s2){
        for(Integer i : s1){
            for(Integer j : s2){
                if(i == j){
                    return true;
                }
            }
        }
        return false;
    }


    //Sets the disjoint sets for all keys
    public void setDisjointSets(){
        disjointSets.add(new HashSet<Integer>());

        System.out.println("setting neighbors");
        for(Business b : queue.businesses){
            queue.setNeighbors(b);


        }
        System.out.println("done");


        System.out.println("setting disjoint sets");

        for(Business b : queue.businesses){
            HashSet<Integer> hs = new HashSet<>();
            if(b.inSet == false){
                hs = createDisjointSet(b, hs);

                boolean containsDupe = false;
                for(HashSet<Integer> i : disjointSets) {

                    if(!containsCommonElement(i, hs)) {


                    } else{
                        i.addAll(hs);
                        containsDupe = true;
                    }
                }
                if(!containsDupe){
                    tempDisjointSet.add((HashSet<Integer>)hs.clone());

                }
                for(HashSet<Integer> i : (ArrayList<HashSet<Integer>>)tempDisjointSet.clone()){
                    disjointSets.add(i);
                }
                tempDisjointSet.clear();


            }
        }
        ArrayList<Integer> toRemove = new ArrayList<>();
        for(int i = 0; i < disjointSets.size(); i++){
            if(disjointSets.get(i).size() ==0){
                toRemove.add(i);
            }
        }
        for(int i : toRemove){
            disjointSets.remove(i);
        }

        setDisjointKey(disjointSets);




    }

    //Creates the center of a given disjoint set
    public void setDestination(HashSet<Integer> h){
        ArrayList<Business> businesses = new ArrayList<>();
        for(Integer i : h){
            businesses.add(queue.businesses.get(i));
        }
        double x = 0;
        double y = 0;
        for (Business b : businesses){
            x = x + b.latitude;
            y = y + b.longitude;
        }
        destination.latitude = x / businesses.size();
        destination.longitude = y / businesses.size();
        destination.key = 999999;
    }

    //Adds businesses from a set to the queue
    public void setQueue(Business b){
        queue.queue.clear();
        int djKey = b.disjointKey;
        HashSet hs = disjointSets.get(djKey);
        Iterator it = hs.iterator();
        while(it.hasNext()) {
            queue.queue.add(queue.businesses.get((Integer)it.next()));
        }
        setDestination(hs);
        queue.queue.add(destination);

    }

    //Adjusts the neighbors in a set with the new center node
    public void adjustNeighbors(){
        for(int i = 0; i < queue.queue.size(); i++){
            queue.queue.get(i).neighbors.clear();
        }
        for(int i = 0; i < queue.queue.size(); i++){
            for(int j = 0; j < queue.queue.size(); j++){
                if(!queue.queue.get(i).equals(queue.queue.get(j))){
                double d = queue.getDistance(queue.queue.get(i), queue.queue.get(j));
                if(queue.queue.get(i).neighbors.size() < 4) {
                    queue.queue.get(i).addNeighbor(queue.queue.get(j).key, d);
                    queue.queue.get(i).neighbors.sort(new sortBusinessRef());
                }else{
                    queue.queue.get(i).addNeighbor(queue.queue.get(j).key, d);
                    queue.queue.get(i).neighbors.sort(new sortBusinessRef());
                    queue.queue.get(i).neighbors.remove(4);
                }
                }
            }

        }
    }


    //Sets edges of the graph
    public void setEdges(){
        edges.clear();
        for(Business b : queue.queue){
            for(int i = 0; i < 4; i++) {
                if(b.neighbors.get(i).key != 999999) {
                    b.edges.add(new Edge(b, queue.businesses.get(b.neighbors.get(i).key), b.getSimilarity(queue.businesses.get(b.neighbors.get(i).key))));
                } else{
                    b.edges.add(new Edge(b, queue.queue.get(queue.queue.size() - 1 ), 10));
                }

            }
        }
    }

    //Attempt at implementation
    public void dijkstraShortestPath(Business b){
        b.value = 0;
        HashSet<Business> visited = new HashSet<>();


        setQueue(b);
        adjustNeighbors();
        setEdges();
        queue.nextUp.add(b);
        while(!queue.nextUp.isEmpty()) {

            for(Edge e : b.edges){
                if(!visited.contains(e.destination) & !queue.nextUp.contains(e.destination)){
                    queue.nextUp.add(e.destination);
                }
            }

            for (Edge e : b.edges) {
                if(!visited.contains(e.destination)) {
                    if (e.destination.value > b.value + e.weight) {
                        e.destination.value = b.value + e.weight;
                    }
                }
            }
            visited.add(b);
            queue.nextUp.remove(b);
            queue.queue.remove(b);
            double val = Double.POSITIVE_INFINITY;
            double valWeight = 0;
            Business lowestVal = new Business();
            for(Edge e : b.edges){
                if(!visited.contains(e.destination)) {
                    if (e.destination.value < val) {
                        val = e.destination.value;
                        lowestVal = e.destination;
                        valWeight = e.weight;
                    }
                }
            }
            if(val != Double.POSITIVE_INFINITY & b.value < lowestVal.value + valWeight) {
                b = lowestVal;
                b.value = lowestVal.value + valWeight;
                //visited.add(b);
                queue.nextUp.remove(b);
            } else{
                b = queue.pop();
            }

       }

    }


}
