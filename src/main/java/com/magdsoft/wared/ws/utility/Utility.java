package com.magdsoft.wared.ws.utility;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermissions;
import java.nio.file.attribute.UserPrincipal;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.web.multipart.MultipartFile;


import com.magdsoft.wared.ws.controller.WebServiceController;
import com.magdsoft.wared.ws.form.TimedLocation;
import com.magdsoft.wared.ws.model.Driver;

//import com.magdsoft.sindbad.ws.controller.Register;

public class Utility {

	
	public static String uploadFile(MultipartFile fl) throws NoSuchAlgorithmException, IOException {
		String fileName = "UL" + new Date().getTime() + SecureRandom.getInstanceStrong().nextInt(Integer.MAX_VALUE) + fl.getOriginalFilename();
		Path path = Paths.get(WebServiceController.PATH, fileName);
		File file = path.toFile();
		fl.transferTo(file);

		// Set owner to sindbadm, and attributes to be editable by the dashboard
		UserPrincipal user = FileSystems.getDefault().getUserPrincipalLookupService().lookupPrincipalByName("waredmag");
		Files.setOwner(path, user);
		Files.setPosixFilePermissions(path, PosixFilePermissions.fromString("rwxr--r--"));
		
		return "http://wared.magdsoft.com/uploads/" + fileName;
	}
	
	
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
	
	public static Map<Double,Driver> getNearCars(EntityManager em,TimedLocation timedLocation) {
	
	
	//em.getTransaction().begin();
    Query query = em.createQuery("from Driver driver"
            + " where longitude >= :long1 AND longitude <= :long2"
            + "   AND latitude  >= :lat1  AND latitude  <= :lat2");
	query.setParameter("long1", timedLocation.getLongitude() - 0.1164051);
	query.setParameter("long2", timedLocation.getLongitude() + 0.1164051);
	query.setParameter("lat1",  timedLocation.getLatitude() - 0.1164051);
	query.setParameter("lat2",  timedLocation.getLatitude() + 0.1164051);
	
	List<Driver> results = query.getResultList();
	
	SortedMap<Double, Driver> distances = new TreeMap<>();
	//List<SortedMap<Double, Car>> distances = new ArrayList<>();
	//SortedMap<Double, Car> dist = new TreeMap<>();
	for(Driver oneCar : results) {
		distances.put(distance(
	                                  oneCar.getLatitude(), timedLocation.getLatitude(),
	                                  oneCar.getLongitude(),timedLocation.getLongitude()
	                                  ), oneCar);
	
	}
	return distances;
	
}
	
	
}
