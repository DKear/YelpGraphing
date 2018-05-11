//Custom priority queue class for use of Dijkstra's shortest path
import java.util.ArrayList;


public class PriorityQueue{
    ArrayList<Business> queue = new ArrayList<>();
    ArrayList<Business> businesses = new ArrayList<>();
    ArrayList<Business> nextUp = new ArrayList<>();

    //return the first element and remove it
    public Business pop (){
        Business b = nextUp.get(0);
        nextUp.remove(0);
        return b;
    }

    public void setNeighbors(Business b) {
        for (Business currentBusiness : businesses) {

            if (!currentBusiness.equals(b)){
                double d = getDistance(b, currentBusiness);
                if (b.neighbors.size() < 4) {
                    b.addNeighbor(currentBusiness.key, d);
                    b.neighbors.sort(new sortBusinessRef());
                } else {
                    b.addNeighbor(currentBusiness.key, d);
                    b.neighbors.sort(new sortBusinessRef());
                    b.neighbors.remove(4);
                }
            }
        }
    }





    public double getDistance(Business a, Business b){
        double latDistance = toRad(b.latitude) - toRad(a.latitude);
        double lonDistance = toRad(b.longitude)  - toRad( a.longitude);
        double A = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) + Math.cos(toRad(a.latitude)) * Math.cos(toRad(b.latitude)) * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double C = 2 * Math.atan2(Math.sqrt(A), Math.sqrt(1 - A));
        double distance = 6372.8 * C;
        return distance;
    }

    public double toRad(double v){
        return v * (Math.PI / 180);
    }




}
