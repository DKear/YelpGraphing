import java.util.ArrayList;
import java.util.Comparator;

public class Business {
    public class BusinessRef{
        int key;
        double distance;
        public BusinessRef(int k, double d){
            key = k;
            distance = d;
        }

    }

    int key;
    String name;
    double stars;
    double similarity;
    int reviewCount;
    double longitude;
    double latitude;
    ArrayList<BusinessRef> neighbors = new ArrayList<>();
    int disjoinKey;
    Boolean inSet = false;

    public void addNeighbor(int k, double d){
        BusinessRef b = new BusinessRef(k, d);
        neighbors.add(b);
    }

    public Business(int k, String n, double s, int r, double lon, double lat){
        key = k;
        name = n;
        stars = s;
        reviewCount = r;
        longitude = lon;
        latitude = lat;

    }

    public Business(){}



}
class sortBusinessRef implements Comparator<Business.BusinessRef>{
    public int compare(Business.BusinessRef b1, Business.BusinessRef b2){
        if(b1.distance - b2.distance > 0 ){
            return 1;
        }else if(b1.distance - b2.distance == 0){
            return 0;
        }else{
            return -1;
        }

    }
}
