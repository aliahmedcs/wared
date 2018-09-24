package com.magdsoft.wared.ws;

public class LatLngBearing implements Comparable<LatLngBearing> {    

	/*
	 * Calculate distance between two points in latitude and longitude taking
	 * into account height difference. If you are not interested in height
	 * difference pass 0.0. Uses Haversine method as its base.
	 *
	 * lat1, lon1 Start point lat2, lon2 End point el1 Start altitude in meters
	 * el2 End altitude in meters
	 * @returns Distance in Meters
	 */
	public static double distance(double lat1, double lat2, double lon1,
	                              double lon2) {
	    
	    final int R = 6371; // Radius of the earth
	    
	    Double latDistance = Math.toRadians(lat2 - lat1);
	    Double lonDistance = Math.toRadians(lon2 - lon1);
	    Double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
	    + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
	    * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
	    Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
	    double distance = R * c * 1000; // convert to meters
	    
	    //	    double height = el1 - el2;
	    
	    //	    distance = Math.pow(distance, 2) + Math.pow(height, 2);
	    
	    return distance;
	}

	private int bearing;
	private double longitude;
	private double latitude;

	/**
	 * @param bearing
	 * @param longitude
	 * @param latitude
	 */
	public LatLngBearing(int bearing, double latitude, double longitude) {
		this.bearing = bearing;
		this.longitude = longitude;
		this.latitude = latitude;
	}

	/**
	 *
	 */
	public LatLngBearing() {
	}

	/**
	 * @return the bearing
	 */
	public int getBearing() {
		return bearing;
	}

	/**
	 * @param bearing the bearing to set
	 */
	public void setBearing(int bearing) {
		this.bearing = bearing;
	}

	/**
	 * @return the longitude
	 */
	public double getLongitude() {
		return longitude;
	}

	/**
	 * @param longitude the longitude to set
	 */
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	/**
	 * @return the latitude
	 */
	public double getLatitude() {
		return latitude;
	}

	/**
	 * @param latitude the latitude to set
	 */
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	@Override
	public int compareTo(LatLngBearing o) {
		if (this.longitude < o.longitude) return -1;
		if (this.longitude > o.longitude) return 1;
		if (this.latitude < o.latitude) return -1;
		if (this.latitude > o.latitude) return 1;
		if (this.bearing < o.bearing) return -1;
		if (this.bearing > o.bearing) return 1;

		return 0;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + bearing;
		long temp;
		temp = Double.doubleToLongBits(latitude);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(longitude);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LatLngBearing other = (LatLngBearing) obj;
		if (bearing != other.bearing)
			return false;
		if (Double.doubleToLongBits(latitude) != Double.doubleToLongBits(other.latitude))
			return false;
		if (Double.doubleToLongBits(longitude) != Double.doubleToLongBits(other.longitude))
			return false;
		return true;
	}
	
}
