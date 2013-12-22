package fi.paivola.mapserver.utils;

/**
 * Latitude/Longitude for everybody.
 * @author juhani
 */
public class LatLng {
    public double latitude;
    public double longitude;
    public LatLng(double lat, double lng) {
        this.latitude = lat;
        this.longitude = lng;
    }
    
    /**
     * Implementation of the haversine formula for calculating distances.
     * @param other The other point of calculation.
     * @return distance in km.
     */
    public double distanceTo(LatLng other) {
        double radius = 6371; // Earths radius in km.
        double a = Math.pow(Math.sin(Math.toRadians(other.latitude-this.latitude)/2), 2) +
                Math.pow(Math.sin(Math.toRadians(other.longitude-this.longitude)/2), 2) *
                Math.cos(Math.toRadians(this.latitude)) * Math.cos(Math.toRadians(other.latitude));
        return 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a)) * radius;
    }
}
