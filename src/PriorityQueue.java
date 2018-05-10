import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

public class PriorityQueue{
    ArrayList<Business> queue = new ArrayList<>();
    ArrayList<Business> businesses = new ArrayList<>();


    public void push(Business b){
        queue.add(b);
    }

    public Business pop (){
        Business b = queue.get(0);
        queue.remove(0);
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

    public static void main(String[] args){
        PriorityQueue p = new PriorityQueue();

        Business a = new Business();
        a.key = 1;
        a.longitude = -115.1715512;
        a.latitude = 36.1272241;

        Business b = new Business();
        b.key = 2;
        b.longitude = -115.168321542;
        b.latitude = 36.1238687505;

        //NV
        Business c = new Business();
        c.key = 3;
        c.longitude = -115.167986;
        c.latitude = 36.127962;

        Business d = new Business();
        d.key = 4;
        d.longitude = -80.8321253;
        d.latitude = 35.1523852;

        //AZ
        Business e = new Business();
        e.key = 5;
        e.longitude = -111.8884619;
        e.latitude = 33.6997745;

        //OH
        Business f = new Business();
        f.key = 6;
        f.longitude = -81.388759;
        f.latitude = 41.353396;

        p.businesses.add(a);
        p.businesses.add(b);
        p.businesses.add(c);
        p.businesses.add(d);
        p.businesses.add(e);
        p.businesses.add(f);

        p.setNeighbors(a);
    }


}
