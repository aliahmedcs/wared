package com.magdsoft.wared.ws.sockets;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.TreeMap;

import org.springframework.scheduling.annotation.Async;

import com.magdsoft.wared.ws.LatLngBearing;
import com.magdsoft.wared.ws.LocationAware;

/**
 * DAO data structure (Add, Update, Delete) for the in-memory location aware objects database
 * Indexes the added (or updated) data by id and by location
 */
public class LocationAwareObjectDAO<T extends LocationAware> {
	private final Map<Integer, T> objectsById = new HashMap<>();
	private final NavigableMap<LatLngBearing, T> objectsByLocation = new TreeMap<>();

	public synchronized void addObject(T object) {
		try {
			// If this car has the id before
			if (objectsById.containsKey(object.getId())) {
				// Remove the old one first	
				this.deleteObject(object.getId());
			}
			objectsById.put(object.getId(), object);
			if (null != object.getLocation())
				objectsByLocation.put(object.getLocation(), object);
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}

	@Async
	public synchronized void addObjectAsync(T object) {
		addObject(object);
	}

	public void updateObjectLocation(int id, double latitude, double longitude, int bearing) {
		T object = objectsById.get(id);

		synchronized(this) {
			LatLngBearing pos = object.getLocation();

			// Remove the old index
			if (pos != null)
				objectsByLocation.remove(pos);
			else {
				// If there is no previous position object
				// Create one
				pos = new LatLngBearing();
				object.setLocation(pos);
			}

			// Change the location
			pos.setLatitude(latitude);
			pos.setLongitude(longitude);
			pos.setBearing(bearing);

			// Add the index
			objectsByLocation.put(pos, object);
		}
	}

	@Async
	public void updateObjectLocationAsync(int id, double latitude, double longitude, int bearing) {
		updateObjectLocation(id, latitude, longitude, bearing);
	}

	public void deleteObject(int id) {
		T obj = objectsById.get(id);

		synchronized(this) {
			objectsById.remove(id);
			if (null != obj.getLocation())
				objectsByLocation.remove(obj.getLocation());
		}

	}

	@Async
	public void deleteObjectAsync(int id) {
		deleteObject(id);
	}

	public synchronized T getById(int id) {
		return objectsById.get(id);
	}	

	/**
	 * Searches this DAO for nearest objects by longitude and latitude
	 *
	 * @param range      Range to search in (maximum distance)
	 * @param latitude   Latitude
	 * @param longitude  Longitude
	 * @return Map of distances to objects
	 */
	public List<Distances<T>> getNearObjectFromMemory(double range,
		    double latitude, double longitude) {
    	
	        LatLngBearing minLoc = new LatLngBearing(0, latitude - range, longitude - range);
	    	LatLngBearing maxLoc = new LatLngBearing(0, latitude + range, longitude + range);
    	
		NavigableMap<LatLngBearing, T> objectsByLocation = this.objectsByLocation.subMap(minLoc, false, maxLoc, false);
		if (objectsByLocation == null) { objectsByLocation = new TreeMap<>(); }
		NavigableMap<LatLngBearing, T> locs = objectsByLocation.subMap(minLoc, false, maxLoc, false);
		
		List<Distances<T>> distances = new ArrayList<>();
		for(T oneObject : locs.values()) {
			LatLngBearing location = oneObject.getLocation();
			if (location.getLatitude() < minLoc.getLatitude()) continue;
			if (location.getLongitude() < minLoc.getLongitude()) continue;
			if (location.getLatitude() > maxLoc.getLatitude()) continue;
			if (location.getLongitude() > maxLoc.getLongitude()) continue;
			distances.add(new Distances<>(LatLngBearing.distance(
					location.getLatitude(), latitude,
					location.getLongitude(),longitude
					), oneObject));
		
		}
		// Sort
		distances.sort(Distances.<T>getComparator());
		
		return distances;
	}

	public static class Distances<T extends LocationAware> implements Entry<Double, T>, Comparable<Distances<T>> {
		private Double distance;
		private T object;

		/**
		 * @param distance
		 * @param object
		 */
		public Distances(Double distance, T object) {
			this.distance = distance;
			this.object = object;
		}

		@Override
		public Double getKey() {
			return distance;
		}

		@Override
		public T getValue() {
			return object;
		}

		@Override
		public T setValue(T value) {
			T oldValue = object;
			object = value;
			return oldValue;
		}
		
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((distance == null) ? 0 : distance.hashCode());
			result = prime * result + ((object == null) ? 0 : object.hashCode());
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
			Distances other = (Distances) obj;
			if (distance == null) {
				if (other.distance != null)
					return false;
			} else if (!distance.equals(other.distance))
				return false;
			if (object == null) {
				if (other.object != null)
					return false;
			} else if (!object.equals(other.object))
				return false;
			return true;
		}

		@Override
		public int compareTo(Distances<T> o) {
			// Compare distances
			int distanceCompare = Double.compare(this.distance, o.distance);
			// Different? Return Java compare result
			if (distanceCompare != 0) return distanceCompare;
			// Same? Compare the locations
			int locationCompare = this.object.getLocation().compareTo(o.object.getLocation());
			// Different? Return Java compare result
			if (locationCompare != 0) return locationCompare;

			// Some hack job, if the object is equals return that comparison equal
			// If not this is less than o (a hack job)
			if (this.object.equals(o.object)) return 0; else return -1;
		}

		/**
		 * Gets the comparator to be used with <link>List#sort</link>
		 *
		 * @return The comparator
		 */
		public static <U extends LocationAware> Comparator<Distances<U>> getComparator() {
			return new Comparator<Distances<U>>() {
				@Override
				public int compare(Distances<U> o1, Distances<U> o2) {
					// If the first is null, it is automatically less than the second (if the second is not null)
					return o1 == null ? (o2 == null ? 0 : -1) : o1.compareTo(o2);
				}
			};
		}
	}
}

