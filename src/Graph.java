import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;

public class Graph {
    ArrayList<Double> distance = new ArrayList<>();
    ArrayList<HashSet<Integer>> disjointSets = new ArrayList<>();
    HashSet<Integer> tempSet = new HashSet<>();

    PriorityQueue queue = new PriorityQueue();
    HashSet set = new HashSet<Business>();
    //source node
    Business source;
    //destination node
    Business destination = new Business();

    private HashSet createDisjointSet(Business b) {
        int added = 0;
        if (!b.inSet){
        tempSet.add(b.key);
        b.inSet = true;

            for (Business.BusinessRef n : b.neighbors) {
                do {
                    if (tempSet.add(n.key)) {
                        queue.businesses.get(n.key).inSet = true;
                        added++;
                        for (Business.BusinessRef nn : queue.businesses.get(n.key).neighbors) {
                            createDisjointSet(queue.businesses.get(nn.key));
                        }
                    } else {
                        added++;
                    }
                } while (added < 4);
                added = 0;

            }
    }

        return tempSet;
    }

    public void setDisjointSets(){

        //ArrayList<DisjointSet> ds = new ArrayList<>();
        //int dsCount = 0;
        System.out.println("setting neighbors");
        for(Business b : queue.businesses){
            queue.setNeighbors(b);


        }
        System.out.println("done");
        System.out.println("setting disjoint sets");

        for(Business b : queue.businesses){
            if(b.inSet == false){
                //HashSet<Integer> ds = new HashSet<>();
                createDisjointSet(b);
                HashSet<Integer> ds =  (HashSet)tempSet.clone();
                disjointSets.add(ds);
                b.disjoinKey = disjointSets.size() - 1;
                tempSet.clear();



            }
        }


    }

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

    public static void main(String[] args){
        PriorityQueue pq = new PriorityQueue();
        Graph g = new Graph();
        g.queue = pq;
        File json = new File("C:\\Users\\Jamie\\Documents\\GitHub\\YelpGraphing\\business.json");
        int keyCount = 0;
        String line;
        try{
            Scanner scanner = new Scanner(json);
            while(scanner.hasNextLine() && keyCount < 10000){

                line = scanner.nextLine();
                JSONObject obj = new JSONObject(line);
                String name = obj.getString("name");
                double stars = obj.getDouble("stars");
                int reviewCount = obj.getInt("review_count");
                double lat = obj.getDouble("latitude");
                double lon = obj.getDouble("longitude");
                Business b = new Business(keyCount, name, stars, reviewCount, lon, lat);
                keyCount++;
                g.queue.businesses.add(b);

            }
            g.setDisjointSets();

            System.out.println("6336 neighbor keys: ");
            System.out.println(g.queue.businesses.get(6336).neighbors.get(0).key);
            System.out.println(g.queue.businesses.get(6336).neighbors.get(1).key);
            System.out.println(g.queue.businesses.get(6336).neighbors.get(2).key);
            System.out.println(g.queue.businesses.get(6336).neighbors.get(3).key);

            System.out.println("3544 neighbor keys: ");
            System.out.println(g.queue.businesses.get(3544).neighbors.get(0).key);
            System.out.println(g.queue.businesses.get(3544).neighbors.get(1).key);
            System.out.println(g.queue.businesses.get(3544).neighbors.get(2).key);
            System.out.println(g.queue.businesses.get(3544).neighbors.get(3).key);

            System.out.println("4 neighbor keys: ");
            System.out.println(g.queue.businesses.get(4).neighbors.get(0).key);
            System.out.println(g.queue.businesses.get(4).neighbors.get(1).key);
            System.out.println(g.queue.businesses.get(4).neighbors.get(2).key);
            System.out.println(g.queue.businesses.get(4).neighbors.get(3).key);

            System.out.println("3205 neighbor keys: ");
            System.out.println(g.queue.businesses.get(3205).neighbors.get(0).key);
            System.out.println(g.queue.businesses.get(3205).neighbors.get(1).key);
            System.out.println(g.queue.businesses.get(3205).neighbors.get(2).key);
            System.out.println(g.queue.businesses.get(3205).neighbors.get(3).key);

            System.out.println("2069 neighbor keys: ");
            System.out.println(g.queue.businesses.get(2069).neighbors.get(0).key);
            System.out.println(g.queue.businesses.get(2069).neighbors.get(1).key);
            System.out.println(g.queue.businesses.get(2069).neighbors.get(2).key);
            System.out.println(g.queue.businesses.get(2069).neighbors.get(3).key);

            g.setDestination(g.disjointSets.get(g.queue.businesses.get(4).disjoinKey));
            System.out.println("Destination latitude: " + g.destination.latitude + " longitude: " + g.destination.longitude);

            System.out.println("Distance between input and it's set's center " + g.queue.getDistance(g.destination, g.queue.businesses.get(4)));
            System.out.println("Distance between input and it's set's center " + g.queue.getDistance(g.destination, g.queue.businesses.get(3544)));
            System.out.println("Distance between input and it's set's center " + g.queue.getDistance(g.destination, g.queue.businesses.get(6336)));
            System.out.println("Distance between input and it's set's center " + g.queue.getDistance(g.destination, g.queue.businesses.get(3205)));
            System.out.println("Distance between input and it's set's center " + g.queue.getDistance(g.destination, g.queue.businesses.get(2069)));
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
