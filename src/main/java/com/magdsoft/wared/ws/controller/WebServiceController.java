package com.magdsoft.wared.ws.controller;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.magdsoft.sindbad.ws.helpers.password.DefaultPasswordHasher;
import com.magdsoft.wared.ws.form.ChangePassword;
import com.magdsoft.wared.ws.form.DriverProfit;
import com.magdsoft.wared.ws.form.GetHistory;
import com.magdsoft.wared.ws.form.TimedLocation;
import com.magdsoft.wared.ws.model.Contactus;
import com.magdsoft.wared.ws.form.UpdateData;
import com.magdsoft.wared.ws.model.Driver;
import com.magdsoft.wared.ws.model.Madina;
//import com.magdsoft.wared.ws.model.Driver.Status;
import com.magdsoft.wared.ws.model.Request;
import com.magdsoft.wared.ws.model.TheOrder;
import com.magdsoft.wared.ws.model.User;

import com.magdsoft.wared.ws.form.ApiSendMessage;
import com.magdsoft.wared.ws.sockets.Asyncs;
import com.magdsoft.wared.ws.utility.Utility;

@Controller
@RequestMapping("/api")
public class WebServiceController {

	@Autowired
	Asyncs asyncs;

	@Autowired
	SendMessageApi sendMessageApi;
	
	
	ApiSendMessage apiSend ;
	
	@Autowired
	private EntityManagerFactory entityManagerFactory;
	public static final String PATH = "/home/waredmag/public_html/uploads";

	public static final int PAGINATION = 10;
	// public static final Map<String,Object> point=new HashMap<>();
	public static final int PAGINATION_GET_HELP = PAGINATION;
	public static final int PAGINATION_GET_HISTORY = PAGINATION;
	
	public static final byte[] userName = new byte[]{'t', 'h', 'a', 'm', 'e', 'r'};
    public static final byte[] password = new byte[]{'1', '2', '3', '3', '2', '1'};
    
	// @Autowired
	// WebSocketController webSocketController;
	// @RequestMapping("/authenticate")
	// public @ResponseBody Map<String, Object> authenticate(User user,
	// @RequestBody(required = false) User userBody) {
	// EntityManager em = entityManagerFactory.createEntityManager();
	// Map<String, Object> map = new HashMap<>();
	// try {
	// if (userBody != null) {
	// user = userBody;
	// }
	//
	// if (user.getPhone() == null) {
	// map.put("status", 402);
	// return map;
	//
	// } else if (user.getPhone().length() < 10) {
	// map.put("status", 400);
	// return map;
	// }
	// boolean isUserAvailable = false;
	//
	// String name = null;
	// Integer userId = null;
	// String apiToken = null;
	// String apiToken2=null;
	// em.getTransaction().begin();
	// Query q = em.createQuery("from User where phone=?");
	// q.setParameter(1, user.getPhone());
	//
	// // String user_id = null;
	// List<User> use = q.getResultList();
	// for (User u : use) {
	// isUserAvailable = true;
	// name = u.getName();
	// userId = u.getId();
	// apiToken2 = u.getApiToken();
	// }
	// if (!isUserAvailable) {
	// SecureRandom random = new SecureRandom();
	// apiToken = new BigInteger(500, random).toString(32);
	// SecureRandom rand = new SecureRandom();
	// int num = rand.nextInt(100000);
	// String formatted = String.format("%05d", num);
	// // int veryficationCode1 = Integer.valueOf(formatted);
	// int veryficationCode1 = 12345;
	// User newUser = new User();
	// newUser.setPhone(user.getPhone());
	// newUser.setVeryficationCode(veryficationCode1);
	// newUser.setApiToken(apiToken);
	// em.persist(newUser);
	//
	// map.put("status", 300);
	// map.put("apiToken", apiToken);
	// // #
	// map.put("userId", newUser.getId());
	// return map;
	// } else if (isUserAvailable) {
	//
	//// SecureRandom random = new SecureRandom();
	//// apiToken = new BigInteger(500, random).toString(32);
	// // User newUser = new User();
	// // newUser.setApiToken(apiToken);
	// // em.persist(newUser);
	//// q = em.createQuery("update User set apiToken=:api where phone=:ph");
	//// q.setParameter("api", apiToken);
	//// q.setParameter("ph", user.getPhone());
	//// q.executeUpdate();
	// map.put("status", 200);
	// map.put("name", name);
	// map.put("userId", userId);
	// map.put("apiToken", apiToken2);
	// return map;
	// }
	//
	// } catch (Exception e) {
	// // Map<String,Object> map=new HashMap<>();
	// map.put("status", 404);
	// em.getTransaction().rollback();
	// throw e;
	// // return map;
	// } finally {
	// if (em.getTransaction().isActive()) {
	// em.getTransaction().commit();
	// }
	// em.close();
	// }
	// return map;
	//
	// }
	//

    private static String prepareTelephoneNumber(String telNum) {
	    return new BigInteger(telNum).toString();
    }

	@RequestMapping("/register")
	public @ResponseBody Map<String, Object> register(User user, @RequestBody(required = false) User userBody) {
		EntityManager em = entityManagerFactory.createEntityManager();
		Map<String, Object> map = new HashMap<>();
		// em.getTransaction().begin();
		try {
			if (userBody != null) {
				user = userBody;
			}

			if (user.getPhone() == null) {
				map.put("status", 402);
				return map;

			} else if (user.getPhone().length() < 9) {
				map.put("status", 400);
				return map;
				// }else if(user.getEmail().length()<6){
				// map.put("status", 403);
				// return map;
			}
			boolean isUserAvailable = false;

			// String name = null;
			// Integer userId = null;
			String apiToken = null;
			// String apiToken2=null;
			em.getTransaction().begin();
			Query q = em.createQuery("from User where phone=?");
			q.setParameter(1, prepareTelephoneNumber(user.getPhone()));

			// String user_id = null;
			List<User> use = q.getResultList();
			isUserAvailable = !use.isEmpty();
//			for (User u : use) {
//				isUserAvailable = true;
				// name = u.getName();
				// userId = u.getId();
				// apiToken2 = u.getApiToken();
//			}
			if (!isUserAvailable) {
				SecureRandom random = new SecureRandom();
				apiToken = new BigInteger(500, random).toString(32);
				SecureRandom rand = new SecureRandom();
		    	 int veryficationCode1 = 0 ;
		 		while(Integer.toString(veryficationCode1).length() <5)
		 {
		 				int num = rand.nextInt(100000);
		 				String formatted = String.format("%05d", num);
		 				veryficationCode1 = Integer.valueOf(formatted);
		 				 
		 }
				//int veryficationCode1 = 12345;
				 
				 
				
				User newUser = new User();
				newUser.setPhone(prepareTelephoneNumber(user.getPhone()));
				newUser.setVeryficationCode(veryficationCode1);
				newUser.setApiToken(apiToken);
				newUser.setPassword(DefaultPasswordHasher.getInstance().hashPassword(user.getPassword()));
				newUser.setIsActive(false);
				
				
				apiSend= new ApiSendMessage();
				
				apiSend.setUserName(new String(userName));
				apiSend.setNumbers(prepareTelephoneNumber(user.getPhone()));
				apiSend.setMessage("Your Verification Code Is : "+veryficationCode1);
				apiSend.setPassword("123321");
				apiSend.setSender("admin");
//				SimpleDateFormat timeDate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS");
//				apiSend.setDatetime(timeDate.format(new Date()));
				apiSend.setUnicode("E");
				apiSend.setReturnValue("full");
				
				

				sendMessageApi.sendSms(apiSend);
				
				em.persist(newUser);

				map.put("status", 200);
				map.put("apiToken", apiToken);
				// #
				map.put("userId", newUser.getId());
				return map;
			} else if (isUserAvailable) {

				// SecureRandom random = new SecureRandom();
				// apiToken = new BigInteger(500, random).toString(32);
				// User newUser = new User();
				// newUser.setApiToken(apiToken);
				// em.persist(newUser);
				// q = em.createQuery("update User set apiToken=:api where
				// phone=:ph");
				// q.setParameter("api", apiToken);
				// q.setParameter("ph", user.getPhone());
				// q.executeUpdate();
				// map.put("status", 200);
				// map.put("name", name);
				// map.put("userId", userId);
				// map.put("apiToken", apiToken2);
				map.put("status", 401);
				return map;
			}

		} catch (Exception e) {
			// Map<String,Object> map=new HashMap<>();
			map.put("status", 404);
			em.getTransaction().rollback();
			// throw e;
			return map;
		} finally {
			if (em.getTransaction().isActive()) {
				em.getTransaction().commit();
			}
			em.close();
		}
		return map;

	}

	@RequestMapping("/login")
	public @ResponseBody Map<String, Object> login(User user, @RequestBody(required = false) User userBody) {
		EntityManager em = entityManagerFactory.createEntityManager();
		Map<String, Object> map = new HashMap<>();
		// em.getTransaction().begin();
		try {
			if (userBody != null) {
				user = userBody;
			}

			if (user.getPhone() == null || user.getPassword() == null || user.getPassword().length() < 6) {
				map.put("status", 402);
				return map;

			} else if (user.getPhone().length() < 9) {
				map.put("status", 402);
				return map;
			}
//			boolean isUserAvailable = false;

			// String name = null;
			// Integer userId = null;
			// String apiToken = null;
			// String apiToken2=null;
			em.getTransaction().begin();
			Query q = em.createQuery("from User where phone=:p or phone=:pPExt");
			q.setParameter("p", prepareTelephoneNumber(user.getPhone()));
			q.setParameter("pPExt", "966" + prepareTelephoneNumber(user.getPhone()));

			// String user_id = null;
			List<User> use = q.getResultList();
			for (User u : use) {
//				isUserAvailable = true;
				if (u == null) {
					map.put("status", 403);
					return map;
				} else if (u != null) {
					if (DefaultPasswordHasher.getInstance().isPasswordValid(user.getPassword(), u.getPassword())
							&& u.getIsActive()) {
						map.put("status", 200);
						map.put("userId", u.getId());
						map.put("apiToken", u.getApiToken());
						map.put("name", u.getName());
//						map.put("password", u.getPassword());
						map.put("phone", prepareTelephoneNumber(u.getPhone()));
						map.put("userImage", u.getUserImage());
						map.put("isActive", u.getIsActive());
						map.put("veryficationCode", u.getVeryficationCode());
						map.put("email", u.getEmail());
						map.put("address", u.getAddress());
						map.put("latitude", u.getLatitude());
						map.put("longitude", u.getLongitude());
						// map.put("theOrder_id",u.getTheOrder_id().get(0).getUser_id());
						map.put("createdAt", u.getCreatedAt());
						map.put("updatedAt", u.getUpdatedAt());
						return map;
					} else if (DefaultPasswordHasher.getInstance().isPasswordValid(user.getPassword(),
							u.getPassword())) {
						map.put("status", 400);
						return map;
					}
					//
				}

			}

		} catch (Exception e) {
			// Map<String,Object> map=new HashMap<>();
			map.put("status", 404);
			em.getTransaction().rollback();
			 throw e;
			//return map;
		} finally {
			if (em.getTransaction().isActive()) {
				em.getTransaction().commit();
			}
			em.close();
		}
		map.put("status", "you are not registered");
		return map;

	}

	//@RequestMapping("/validateSMS")
	/*public @ResponseBody Map<String, Object> validateSMS(User user, @RequestBody(required = false) User userBody) {
		EntityManager em = entityManagerFactory.createEntityManager();
		Map<String, Object> map = new HashMap<>();
//		Boolean isAvailable = false;
//		Integer verficationCode = null;
		try {
			if (userBody != null) {
				user = userBody;
			}
			if (user.getPhone().length() < 9) {
				map.put("status", 403);
				return map;
			}
			//if (user.getApiToken() == null) {
			//	map.put("status", 402);
			//	return map;
			//}
			if (user.getVeryficationCode() == null || user.getPhone() == null) {
				map.put("status", 402);
				return map;
			}
			em.getTransaction().begin();
			Query q = em.createQuery("from User where phone=:p or phone=:pPExt");
			q.setParameter("p", prepareTelephoneNumber(user.getPhone()));
			q.setParameter("pPExt", "966" + prepareTelephoneNumber(user.getPhone()));

			// String user_id = null;

			List<User> use = q.getResultList();
			if (null == use || use.isEmpty()) {
				map.put("status", 400);
				return map;
			}
			User u = use.get(0);

			
//				isAvailable = true;
//				verficationCode = u.getVeryficationCode();
			if (user.getVeryficationCode().equals(u.getVeryficationCode())) {
				
				// No api token?
				if (null == user.getApiToken() || user.getApiToken().isEmpty()) {
					// It is change password, so new password must be given
					if (null != user.getPassword() && !user.getPassword().isEmpty()) {
						// If there is a password, set it
						u.setPassword(DefaultPasswordHasher.getInstance().hashPassword(user.getPassword()));
					} else {
						map.put("status", 402);
						return map;
					}
				} else {
					// There is api token?
					// Compare it to the user's
					if (!u.getApiToken().equals(user.getApiToken())) {
						map.put("status", 400);
						return map;
					}
				}

				u.setVeryficationCode(null);
				u.setIsActive(true);
				em.persist(u);
			} else {
				map.put("status", 401);
				return map;
			}
			if (prepareTelephoneNumber(user.getPhone()).equals(prepareTelephoneNumber(u.getPhone()))) {
				em.persist(u);
				map.put("status", 200);
				map.put("apiToken", u.getApiToken());
				return map;
			} else if (!prepareTelephoneNumber(user.getPhone()).equals(prepareTelephoneNumber(u.getPhone()))) {
				u.setPhone(prepareTelephoneNumber(user.getPhone()));
				u.setIsActive(true);
				em.persist(u);
				map.put("status", 300);
				map.put("apiToken", u.getApiToken());
				// } else if
				// (!u.getVeryficationCode().equals(user.getVeryficationCode()))
				// {
				// map.put("status", 300);
				// map.put("apiToken", u.getApiToken());
				// }
			} else if (!user.getVeryficationCode().equals(u.getVeryficationCode())) {
				map.put("status", 401);
			}
			// #
			// map.put("code",u.getVeryficationCode());
			return map;
		} catch (Exception e) {
			// Map<String,Object> map=new HashMap<>();
			map.put("status", 404);
			e.printStackTrace();
			em.getTransaction().rollback();
			// throw e;
			return map;
		} finally {
			if (em.getTransaction().isActive()) {
				em.getTransaction().commit();
			}
			em.close();
		}
	}*/
	
	/* Paul's additions. */
	private static boolean isEmpty(String str) {
		return null == str || str.isEmpty();
	}
	
	private static boolean isEmpty(Integer i) {
		return null == i || i == 0;
	}
	
	private static Map<String, Object> retStatus(EntityManager em, int value, Pair<String, Object>... pairs) {
		if (em != null) {
			try {
				em.close();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			}
		}
		
		Map<String, Object> map = new HashMap<>();
		map.put("status", value);
		for (Pair<String, Object> p : pairs) {
			map.put(p.getFirst(), p.getSecond());
		}
		return map;
	}
	
	/* Gets a user by phone number from the database. */
	private User getUser(EntityManager em, String phone) {
		User user = null;
		try {
			Query q = em.createQuery("from User where phone=:p or phone=:pPExt");
			q.setParameter("p", prepareTelephoneNumber(phone));
			q.setParameter("pPExt", "966" + prepareTelephoneNumber(phone));
			List<User> results = q.getResultList();
			if (null != results && !results.isEmpty()) {
				user = results.get(0);
			} else {
				System.out.println("Phone number [966](" + phone + "not found in database.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return user;
	}
	
	@RequestMapping("/validateSMS")
	public @ResponseBody Map<String, Object> validateSMS(User user, @RequestBody(required = false) User userBody) {
		if (userBody != null) {
			user = userBody;
		}
		
		/* NOTE: u is the user from the db, user is not used after these variables. */
		String phone = user.getPhone();
		Integer code = user.getVeryficationCode();
		String pass = user.getPassword();
		String token = user.getApiToken();
		
		if(isEmpty(phone) || isEmpty(code)) {
			System.out.println("Phone, or verificationCode is null or empty.");
			return retStatus(null, 402);
		}
		
		if (phone.length() < 7) {
			System.out.println("Phone number (" + phone + ") is too short.");
			return retStatus(null, 403);
		}
		
		EntityManager em = entityManagerFactory.createEntityManager();
		em.getTransaction().begin();
		User u = getUser(em, phone);
		if (null == u) {
			return retStatus(em, 400);
		}
		
		/* Verification code is not correct. */
		if (!code.equals(u.getVeryficationCode())) {
			System.out.println("Verification code (" + code + "does not match: " + u.getVeryficationCode());
			return retStatus(em, 401);
		}
		
		/* This is the case for resetting password. */
		if (!isEmpty(token)) {
			if (isEmpty(pass)) {
				System.out.println("Password is null or empty");
				return retStatus(em, 402);
			}
			u.setPassword(DefaultPasswordHasher.getInstance().hashPassword(pass));
		} else {	   /* This is the case for validating account. */
			if (!token.equals(u.getApiToken())) {
				System.out.println("Token does not match value in database");
				return retStatus(em, 400);
			}
			u.setPhone(prepareTelephoneNumber(phone));
		}

		u.setVeryficationCode(null);
		u.setIsActive(true);
		
		try {
			em.persist(u);
			em.getTransaction().commit();
			System.out.println("New user details updated");
		} catch (EntityExistsException | IllegalArgumentException e) {
			e.printStackTrace();
			em.getTransaction().rollback();
			System.out.println("Unable to commit database transaction");
			return retStatus(em, 404);
		}
		
		return retStatus(em, isEmpty(token) ? 300 : 200, Pair.of("apiToken", u.getApiToken()));
	}

	@RequestMapping("/updateData")
	public @ResponseBody Map<String, Object> updateData(UpdateData user) {
		EntityManager em = entityManagerFactory.createEntityManager();
		Map<String, Object> map = new HashMap<>();
		Boolean isAvailable = false;
		em.getTransaction().begin();
		// Integer verficationCode=null;
		try {
			// if(user.getUserImage()==null||user.getUserImage().isEmpty())
			// if(userBody != null) {
			// user = userBody;
			// }
			if (user.getApiToken() == null) {
				map.put("status", 402);
				map.put("message", "The ApiToken mustn't be null ");
				return map;
			}
			if (user.getApiToken().isEmpty()) {
				map.put("status", 402);
				map.put("message", "The ApiToken mustn't be null ");
				return map;
			}

			Query q = em.createQuery("from User where apiToken=:Api");
			q.setParameter("Api", user.getApiToken());

			// String user_id = null;

			List<User> use = q.getResultList();
			if (use.isEmpty()) {
				map.put("status", 400);
				return map;
			}
			for (User newUser : use) {
				isAvailable = true;

				if (user.getName() != null)
					newUser.setName(user.getName());
				if (user.getPhone() != null)
					newUser.setPhone(prepareTelephoneNumber(user.getPhone()));
				if (user.getEmail() != null && user.getEmail().length() > 4) {
					try {

						if (user.getEmail().equals(newUser.getEmail())) {
							map.put("status", 403);
							return map;
						}

						newUser.setEmail(user.getEmail());
					} catch (Exception e) {
						map.put("status", 401);
						return map;
						// throw e;
					}
				}
				if (user.getLatitude() != null)
					newUser.setLatitude(user.getLatitude());
				if (user.getLongitude() != null)
					newUser.setLongitude(user.getLongitude());
				if (user.getAddress() != null)
					newUser.setAddress(user.getAddress());
				try {

					if (user.getUserImage() != null)
						newUser.setUserImage(Utility.uploadFile(user.getUserImage()));

				} catch (Exception ex) {
					ByteArrayOutputStream os = new ByteArrayOutputStream();
					ex.printStackTrace(new PrintWriter(os));
					Map<String, Object> json = new HashMap<>();
					json.put("status", "upload error");
					os.flush();
					json.put("error", os.toString());
					return json;
				}
				em.persist(newUser);
				if (user.getUserImage() != null) {
					map.put("status", 300);
					map.put("imageURL", newUser.getUserImage());
				} else
					map.put("status", 200);
				// map.put("imageURL", newUser.getUserImage());
				return map;
			}

			if (!isAvailable) {
				map.put("status", 400);
				return map;
			}

		} catch (Exception e) {
			// Map<String,Object> map=new HashMap<>();
			map.put("status", 404);
			em.getTransaction().rollback();
			// throw e;
			return map;
		} finally {
			if (em.getTransaction().isActive()) {
				em.getTransaction().commit();
			}
			em.close();
		}
		return null;
	}

	@RequestMapping("/updatePhone")
	public @ResponseBody Map<String, Object> updatePhone(UpdateData user,
			@RequestBody(required = false) UpdateData userBody) {
		EntityManager em = entityManagerFactory.createEntityManager();
		em.getTransaction().begin();
		Map<String, Object> map = new HashMap<>();
//		Boolean isAvailable = false;
		// Integer verficationCode=null;
		try {
			if (userBody != null) {
				user = userBody;
			}
			if (user.getApiToken() == null) {
				map.put("status", 402);
				map.put("message", "The ApiToken mustn't be null ");
				return map;
			}
			if (user.getApiToken().isEmpty() || user.getPhone() == null) {
				map.put("status", 402);
				map.put("message", "The ApiToken mustn't be null ");
				return map;
			}
			if (user.getPhone().length() < 9) {
				map.put("status", 401);
				return map;
			}
			Query q = em.createQuery("from User where apiToken=:Api");
			q.setParameter("Api", user.getApiToken());

			// String user_id = null;

			List<User> use = q.getResultList();
			if (use.isEmpty()) {
				map.put("status", 400);
				return map;
			}
			for (User u : use) {
//				isAvailable = true;
				if (u.getPhone() == null) {
					map.put("error", "user has not phone from before");
					return map;
				}

				if (prepareTelephoneNumber(u.getPhone()).equals(prepareTelephoneNumber(user.getPhone()))) {
					map.put("status", 403);
					return map;
				}
				// } else if (!u.getPhone().equals(user.getPhone())) {
				// u.setPhone(user.getPhone());

				SecureRandom rand = new SecureRandom();
				int num = rand.nextInt(100000);
				String formatted = String.format("%05d", num);
//				int veryficationCode1 = Integer.valueOf(formatted);
				u.setVeryficationCode(12345);
				em.persist(u);
				map.put("status", 200);
				return map;
			}

		} catch (Exception e) {
			// Map<String,Object> map=new HashMap<>();
			map.put("status", 404);
			em.getTransaction().rollback();
			throw e;
			// return map;
		} finally {
			if (em.getTransaction().isActive()) {
				em.getTransaction().commit();
			}
			em.close();
		}
		return null;
	}

	@RequestMapping("/contactUs")
	public @ResponseBody Map<String, Object> contactUs(Contactus contactus,
			@RequestBody(required = false) Contactus contactusBody) {
		EntityManager em = entityManagerFactory.createEntityManager();
		Map<String, Object> map = new HashMap<>();
		// Integer verficationCode=null;
		em.getTransaction().begin();
		try {
			if (contactusBody != null) {
				contactus = contactusBody;
			}
			if (contactus.getApiToken() == null) {
				map.put("status", 402);
				map.put("message", "The ApiToken mustn't be null ");
				return map;
			}
			if (contactus.getApiToken().isEmpty() || contactus.getTitle() == null || contactus.getMessage() == null) {
				map.put("status", 402);
				return map;
			}
			Query q = em.createQuery("from User where apiToken=:Api");
			q.setParameter("Api", contactus.getApiToken());

			// String user_id = null;

			List<User> use = q.getResultList();

			if (use.isEmpty()) {
				map.put("status", 400);
				return map;
			}

			if (contactus.getTitle().length() > 256 && contactus.getMessage().length() > 1000) {
				map.put("status", 402);
				map.put("reason", "title should be lower than 256 and message lower than 1000");
				return map;
			}
			Contactus newContactus = new Contactus();
			newContactus.setApiToken(contactus.getApiToken());
			newContactus.setTitle(contactus.getTitle());
			newContactus.setMessage(contactus.getMessage());
			newContactus.setType("user");
			newContactus.setUserId(use.get(0));
			em.persist(newContactus);
			em.getTransaction().commit();
			map.put("status", 200);
			return map;
		} catch (Exception e) {
			// Map<String,Object> map=new HashMap<>();
			map.put("status", 404);
			em.getTransaction().rollback();
			// throw e;
			return map;
		} finally {
			if (em.getTransaction().isActive()) {
				em.getTransaction().commit();
			}
			em.close();
		}
	}

	@RequestMapping("/getHistory")
	public @ResponseBody Map<String, Object> getHistory(GetHistory theOrder,
			@RequestBody(required = false) GetHistory theOrderBody) {
		EntityManager em = entityManagerFactory.createEntityManager();
		Map<String, Object> map = new HashMap<>();
		em.getTransaction().begin();
//		Boolean isAvailable = false;
		// Integer verficationCode=null;
		try {
			if (theOrderBody != null) {
				theOrder = theOrderBody;
			}
			if (theOrder.getApiToken() == null) {
				map.put("status", 402);
				map.put("message", "The ApiToken mustn't be null ");
				return map;
			}

			if (theOrder.getApiToken().isEmpty() || theOrder.getRunning() == null) {
				map.put("status", 402);
				return map;
			}
			Query q = em.createQuery("from User where apiToken=:Api");
			q.setParameter("Api", theOrder.getApiToken());

			// String user_id = null;

			List<User> use = q.getResultList();

			if (use.isEmpty()) {
				Map<String, Object> map1 = new HashMap<>();
				map1.put("status", 400);
				return map1;
			}
			// List<Map<String, Object>> questions = new ArrayList<>();
			List<Map<String, Object>> list = new ArrayList<>();
			q = null;
			if (theOrder.getRunning() == 1) {
				q = em.createQuery("from TheOrder where user_id.apiToken=:Api and status=:a");
			} else if (theOrder.getRunning() == 0) {
				q = em.createQuery("from TheOrder where user_id.apiToken=:Api and status!=:a");
			}
			q.setParameter("a", "جارى");
			if (theOrder.getPage() == null)
				theOrder.setPage(1);
			q.setParameter("Api", theOrder.getApiToken());
			q.setFirstResult((theOrder.getPage() - 1) * PAGINATION_GET_HELP);
			q.setMaxResults(PAGINATION_GET_HELP);

			// String user_id = null;

			List<TheOrder> ord = q.getResultList();
			if (ord == null) {
				map.put("status", 400);
				map.put("history", list);
				return map;
			}
			for (TheOrder o : ord) {
				// map.put("staus", 200);
				Map<String, Object> historyOne = new HashMap<>();
				historyOne.put("DriverName", o.getDriver_id().getName());
				historyOne.put("Status", o.getStatus());
				historyOne.put("date", o.getCreatedAt());
				list.add(historyOne);
			}
			Map<String, Object> map1 = new HashMap<>();
			map1.put("status", 200);
			map1.put("History", list);
			return map1;
		} catch (Exception e) {
			// Map<String,Object> map=new HashMap<>();
			map.put("status", 404);
			em.getTransaction().rollback();
			throw e;
			// return map;
		} finally {
			if (em.getTransaction().isActive()) {
				em.getTransaction().commit();
			}
			em.close();
		}
	}

	@RequestMapping("/search")
	public @ResponseBody Map<String, Object> search(TimedLocation timedLocation,
			@RequestBody(required = false) TimedLocation timedLocationBody) {
		EntityManager em = entityManagerFactory.createEntityManager();
		em.getTransaction().begin();
		Map<String, Object> ret = new HashMap<>();
		if (timedLocationBody != null) {
			timedLocation = timedLocationBody;
		}

		if (timedLocation.getApiToken() == null) {
			ret.put("status", 402);
			ret.put("message", "The ApiToken mustn't be null ");
			return ret;
		}

		if (timedLocation.getApiToken().isEmpty() || timedLocation.getLatitude() == null
				|| timedLocation.getLongitude() == null) {
			ret.put("status", 402);
			ret.put("reason", "parameters shouldn't be null");
			return ret;
		}
		Query q = em.createQuery("from User where apiToken=:Api");
		q.setParameter("Api", timedLocation.getApiToken());

		// String user_id = null;

		List<User> use = q.getResultList();

		if (use.isEmpty()) {
			Map<String, Object> map = new HashMap<>();
			map.put("status", 400);
			return map;
		}

		if (timedLocation.getLatitude() == null || timedLocation.getLongitude() == null) {
			ret.put("status", 402);
			return ret;
		}

		// EntityManager em = entityManagerFactory.createEntityManager();
		try {

//			Map<String, Object> point = new HashMap<>();
			// point.put("latitude", timedLocation.getLatitude());
			// point.put("longitude",timedLocation.getLongitude());
			// Map<Double, Car> distances = getNearCars(em, timedLocation);

			Map<Double, Driver> distances = Utility.getNearCars(em, timedLocation);
			// query.

			int i = 0;

			ret.put("status", 200);

			List<Map<String, Object>> cars = new ArrayList<>();

			for (Entry<Double, Driver> entry : distances.entrySet()) {
				Map<String, Object> car = new HashMap<>();
				car.put("id", entry.getValue().getId());
				// car.put("distance", entry.getKey());
				car.put("latitude", entry.getValue().getLatitude());
				car.put("longitude", entry.getValue().getLongitude());
				// car.put("car_type", entry.getValue().getCarType());
				cars.add(car);

				if (++i == 10)
					break;
			}

			ret.put("cars", cars);

			em.getTransaction().commit();

			return ret;
		} catch (Exception e) {
			// Map<String,Object> map=new HashMap<>();
			ret.put("status", 404);
			em.getTransaction().rollback();
			// throw e;
			return ret;
		} finally {
			if (em.getTransaction().isActive()) {
				em.getTransaction().commit();
			}
			em.close();
		}
	}

	@RequestMapping("/userInfo")
	public @ResponseBody Map<String, Object> userInfo(User user, @RequestBody(required = false) User userBody) {
		EntityManager em = entityManagerFactory.createEntityManager();
		Map<String, Object> map = new HashMap<>();
		Boolean isAvailable = false;

		em.getTransaction().begin();
		try {

			if (userBody != null) {
				user = userBody;
			}

			if (user.getApiToken() == null) {
				map.put("status", 402);
				return map;
			}
			if (user.getApiToken().isEmpty()) {
				map.put("status", 402);
				map.put("message", "The ApiToken mustn't be null or empty ");
				return map;
			}

			Query q = em.createQuery("from User where apiToken=:Api");
			q.setParameter("Api", user.getApiToken());

			// String user_id = null;

			List<User> use = q.getResultList();
			if (use.isEmpty()) {
				map.put("status", 400);
				return map;
			}
			for (User u : use) {
				isAvailable = true;
				map.put("name", u.getName());
				map.put("phone", prepareTelephoneNumber(u.getPhone()));
				map.put("email", u.getEmail());
				map.put("address", u.getAddress());
				map.put("latitude", u.getLatitude());
				map.put("longitude", u.getLongitude());
				map.put("profilePicture", u.getUserImage());

			}
			if (!isAvailable) {
				map.put("status", 400);
				return map;
			}
			Map<String, Object> ret = new HashMap<>();
			ret.put("status", 200);
			ret.put("user", map);
			return ret;

		} catch (Exception e) {
			// Map<String,Object> map=new HashMap<>();
			map.put("status", 404);
			em.getTransaction().rollback();
			// throw e;
			return map;
		} finally {
			if (em.getTransaction().isActive()) {
				em.getTransaction().commit();
			}
			em.close();
		}

	}

	@RequestMapping("/updatePassword")
	public @ResponseBody Map<String, Object> updatePassword(User user, @RequestBody(required = false) User userBody) {
		EntityManager em = entityManagerFactory.createEntityManager();
		Map<String, Object> map = new HashMap<>();
		// em.getTransaction().begin();
		try {
			if (userBody != null) {
				user = userBody;
			}

			if (user.getApiToken() == null || user.getApiToken().length() < 10) {
				map.put("status", 402);
				return map;

			} else if (user.getPassword().length() < 6) {
				map.put("status", 403);
				return map;
			}
			boolean isUserAvailable = false;

			em.getTransaction().begin();
			Query q = em.createQuery("from User where apiToken=:api");
			q.setParameter("api", user.getApiToken());

			// String user_id = null;
			List<User> use = q.getResultList();
			isUserAvailable = !use.isEmpty();
//			for (User u : use) {
//				isUserAvailable = true;
//
//			}
			if (!isUserAvailable) {
				map.put("status", 400);
				return map;
			}
			q = em.createQuery("update User set password=:pass where apiToken=:api");
			q.setParameter("api", user.getApiToken());
			q.setParameter("pass", DefaultPasswordHasher.getInstance().hashPassword(user.getPassword()));
			q.executeUpdate();
			map.put("status", 200);
			return map;
		} catch (Exception e) {
			// Map<String,Object> map=new HashMap<>();
			map.put("status", 404);
			em.getTransaction().rollback();
			// throw e;
			return map;
		} finally {
			if (em.getTransaction().isActive()) {
				em.getTransaction().commit();
			}
			em.close();
		}
	}

		@RequestMapping("/forgetPassword")
	public @ResponseBody Map<String, Object> forgetPassword(User user, @RequestBody(required = false) User userBody) {
		EntityManager em = entityManagerFactory.createEntityManager();
		Map<String, Object> map = new HashMap<>();
		// em.getTransaction().begin();
		try {
			if (userBody != null) {
				user = userBody;
			}

			if (user.getPhone() == null || user.getPhone().length() < 9) {
				map.put("status", 402);
				return map;
			}

			boolean isUserAvailable = false;
			
			String apiToken = null;

			em.getTransaction().begin();
			Query q = em.createQuery("from User where phone=:ph");
			q.setParameter("ph", prepareTelephoneNumber(user.getPhone()));

			// String user_id = null;
			List<User> use = q.getResultList();
			isUserAvailable = !use.isEmpty();
//			for (User u : use) {
//				isUserAvailable = true;
//				//apiToken = u.getApiToken();
//			}
			if (!isUserAvailable) {
				map.put("status", 401);
				return map;
			}
			// q = em.createQuery("update User set password=:pass where
			// apiToken=:ph");
			// q.setParameter("api", user.getApiToken());
			// q.setParameter("pass",
			// DefaultPasswordHasher.getInstance().hashPassword(user.getPassword()));
			// q.executeUpdate();
			
			User use1 = use.get(0);
			SecureRandom random = new SecureRandom();
			SecureRandom rand = new SecureRandom();
	    	 int veryficationCode1 = 0 ;
	 		while(Integer.toString(veryficationCode1).length() <5)
	 {
	 				int num = rand.nextInt(100000);
	 				String formatted = String.format("%05d", num);
	 				veryficationCode1 = Integer.valueOf(formatted);
	 				 
	 }
			use1.setApiToken(new BigInteger(500, random).toString(32));
			use1.setVeryficationCode(veryficationCode1);
			
			
			apiSend= new ApiSendMessage();
			
			apiSend.setUserName(new String(userName));

			apiSend.setNumbers(prepareTelephoneNumber(user.getPhone()));
			apiSend.setMessage("Your Verification Code Is : "+veryficationCode1);
			apiSend.setPassword("123321");
			apiSend.setSender("admin");
//			SimpleDateFormat timeDate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS");
//			apiSend.setDatetime(timeDate.format(new Date()));
			apiSend.setUnicode("E");
			apiSend.setReturnValue("full");
			
			sendMessageApi.sendSms(apiSend);
			em.persist(use1);
			map.put("status", 200);
			map.put("apiToken", use1.getApiToken());
		
			return map;

		} catch (Exception e) {
			// Map<String,Object> map=new HashMap<>();
			map.put("status", 404);
			em.getTransaction().rollback();
			// throw e;
			return map;
		} finally {
			if (em.getTransaction().isActive()) {
				em.getTransaction().commit();
			}
			em.close();
		}

	}

	// @RequestMapping("/request")
	// public @ResponseBody Map<String, Object> request(User user,
	// @RequestBody(required = false) User userBody) {
	// EntityManager em = entityManagerFactory.createEntityManager();
	// Map<String, Object> map = new HashMap<>();
	// Boolean isAvailable=false;
	// Integer userId=0;
	// TimedLocation timedLocation;
	// try {
	// if (userBody != null) {
	// user = userBody;
	// }
	// if(user.getApiToken()==null || user.getLatitude()==null ||
	// user.getLongtude()==null ||
	// user.getAddress()==null){
	// map.put("status", 402);
	// return map;
	// }
	// em.getTransaction().begin();
	// Query q = em.createQuery("from User where apiToken=:Api");
	// q.setParameter("Api", user.getApiToken());
	//
	// // String user_id = null;
	//
	// List<User> use = q.getResultList();
	// if (use == null) {
	// map.put("status", 400);
	// return map;
	// }
	// for (User u : use) {
	// isAvailable = true;
	// userId=u.getId();
	// }
	// User newuser=em.find(User.class, userId);
	// TheOrder theOrder=new TheOrder();
	// theOrder.setUser_id(newuser);
	// theOrder.setLatitude(user.getLatitude());
	// theOrder.setLongitude(user.getLatitude());
	// theOrder.setAddress(user.getAddress());
	// em.persist(theOrder);
	// timedLocation.setLatitude(user.getLatitude());
	// timedLocation.setLongitude(user.getLongtude());
	//
	// Map<Double,Driver> distances=Utility.getNearCars(em,timedLocation);
	//
	//
	//
	// }

	@RequestMapping("/authenticateDriver")
	public @ResponseBody Map<String, Object> authenticateDriver(Driver driver,
			@RequestBody(required = false) Driver driverBody) {
		EntityManager em = entityManagerFactory.createEntityManager();
		Map<String, Object> map = new HashMap<>();
		String status = null;
		try {
			if (driverBody != null) {
				driver = driverBody;
			}

			if (driver.getPhone() == null || driver.getPassword() == null || driver.getPhone().isEmpty()
					|| driver.getPassword().isEmpty()) {
				map.put("status", 402);
				return map;

			} else if (driver.getPhone().length() < 9 || driver.getPassword().length() < 6) {
				map.put("status", 400);
				return map;
			}
			boolean isUserAvailable = false;

			String apiToken = null;
			em.getTransaction().begin();
			Query q = em.createQuery("from Driver where phone=:driv or phone=:drivExt or phone=:drivExt2");
			q.setParameter("driv", prepareTelephoneNumber(driver.getPhone()));
			q.setParameter("drivExt", "00966" + prepareTelephoneNumber(driver.getPhone()));
			q.setParameter("drivExt2", "00" + prepareTelephoneNumber(driver.getPhone()));
//			q.setParameter("pass", driver.getPassword());
			// String user_id = null;
			List<Driver> driv = q.getResultList();
			
// >>>>
			
			Driver newDriver = null;

// <<<<
			
			boolean isActive = false;

			for (Driver d : driv) {
 				if(DefaultPasswordHasher.getInstance().isPasswordValid(driver.getPassword(), d.getPassword())){
 					isUserAvailable = true;
				}

				if (!d.getIsActive()) {
					isUserAvailable = false;
					break;
				}

//				status = d.getStatus().toString();
				apiToken = d.getApiToken();

// >>>>
				
				newDriver = d;
				
// <<<<
			}
			if (isUserAvailable) {
// >>>>
				// ALI: DO YOU CREATE A NEW DRIVER WHEN YOU LOGIN?
				
//				Driver newDriver = new Driver();
				
// <<<<
				
				SecureRandom random = new SecureRandom();
				apiToken = new BigInteger(500, random).toString(32);
				
				newDriver.setApiToken(apiToken);
				em.persist(newDriver);
				map.put("status", 200);
				map.put("apiToken", newDriver.getApiToken());
				map.put("name", newDriver.getName());
				map.put("phone", prepareTelephoneNumber(newDriver.getPhone()));
				map.put("userImage", newDriver.getUserImage());
				map.put("isActive", newDriver.getIsActive());
				map.put("veryficationCode", newDriver.getVeryficationCode());
				map.put("email", newDriver.getEmail());
				map.put("latitude", newDriver.getLatitude());
				map.put("longitude", newDriver.getLongitude());
				map.put("createdAt", newDriver.getCreatedAt());
				map.put("updatedAt", newDriver.getUpdatedAt());
				map.put("driverId", newDriver.getId());
				return map;
			} else if (!isUserAvailable) {

				map.put("status", 400);
				// map.put("apiToken", apiToken);
				return map;
			}

		} catch (Exception e) {
			// Map<String,Object> map=new HashMap<>();
			map.put("status", 404);
			em.getTransaction().rollback();
			throw e;
			// return map;
		} finally {
			if (em.getTransaction().isActive()) {
				em.getTransaction().commit();
			}
			em.close();
		}
		return map;

	}

	@RequestMapping("/changeStatus")
	public @ResponseBody Map<String, Object> changeStatus(Driver driver,
			@RequestBody(required = false) Driver driverBody) {
		EntityManager em = entityManagerFactory.createEntityManager();
		em.getTransaction().begin();
		Map<String, Object> map = new HashMap<>();
		String status = null;
		try {
			if (driverBody != null) {
				driver = driverBody;
			}

			if (driver.getApiToken() == null || driver.getApiToken().isEmpty()) {
				map.put("status", 402);
				return map;
			}
			Query q = em.createQuery("from Driver where apiToken=:Api");
			q.setParameter("Api", driver.getApiToken());

			// String user_id = null;

			List<Driver> driv = q.getResultList();

			if (driv.isEmpty()) {
				map.put("status", 400);
				return map;
			}
			for (Driver d : driv) {
				if (d.getStatus() == null) {
//					d.setStatus(Status.غير_موجود);
					d.setStatus("غيرـمتاح");
					em.persist(d);
				}
				status = d.getStatus().toString();
//				if (status.equals("غير_موجود")) {
//				d.setStatus(Status.موجود);
				if (status.equals("غيرـمتاح")) {
					d.setStatus("متاح");
					em.persist(d);
					map.put("status", 200);
					return map;
				} else if (status.equals("متاح")) {
					d.setStatus("غيرـمتاح");
					em.persist(d);
					map.put("status", 300);
				}
			}

		} catch (Exception e) {
			// Map<String,Object> map=new HashMap<>();
			map.put("status", 404);
			em.getTransaction().rollback();
			throw e;
			// return map;
		} finally {
			if (em.getTransaction().isActive()) {
				em.getTransaction().commit();
			}
			em.close();
		}
		return map;

	}

	@RequestMapping("/driverInfo")
	public @ResponseBody Map<String, Object> driverInfo(Driver driver,
			@RequestBody(required = false) Driver driverBody) {
		EntityManager em = entityManagerFactory.createEntityManager();
		Map<String, Object> map = new HashMap<>();
		Boolean isAvailable = false;
		Integer verficationCode = null;

		em.getTransaction().begin();
		try {

			if (driverBody != null) {
				driver = driverBody;
			}

			if (driver.getApiToken() == null) {
				map.put("status", 402);
				return map;
			}
			if (driver.getApiToken().isEmpty()) {
				map.put("status", 402);
				map.put("message", "The ApiToken mustn't be null or empty ");
				return map;
			}

			Query q = em.createQuery("from Driver where apiToken=:Api");
			q.setParameter("Api", driver.getApiToken());

			// String user_id = null;

			List<Driver> driv = q.getResultList();
			if (driv.isEmpty()) {
				map.put("status", 400);
				return map;
			}
			for (Driver d : driv) {
				isAvailable = true;
				map.put("name", d.getName());
				map.put("phone", prepareTelephoneNumber(d.getPhone()));
				map.put("email", d.getEmail());
				map.put("profilePicture", d.getUserImage());

			}
			if (!isAvailable) {
				map.put("status", 400);
				return map;
			}
			Map<String, Object> ret = new HashMap<>();
			ret.put("status", 200);
			ret.put("driver", map);
			return ret;

		} catch (Exception e) {
			// Map<String,Object> map=new HashMap<>();
			map.put("status", 404);
			em.getTransaction().rollback();
			// throw e;
			return map;
		} finally {
			if (em.getTransaction().isActive()) {
				em.getTransaction().commit();
			}
			em.close();
		}

	}

	@RequestMapping("/validateDriverSMS")
	public @ResponseBody Map<String, Object> validateDriverSMS(Driver driver,
			@RequestBody(required = false) Driver driverBody) {
		EntityManager em = entityManagerFactory.createEntityManager();
		Map<String, Object> map = new HashMap<>();
		Boolean isAvailable = false;
		Integer verficationCode = null;
		try {
			if (driverBody != null) {
				driver = driverBody;
			}
			if (driver.getPhone().length() < 10) {
				map.put("status", 403);
				return map;
			}
			if (driver.getApiToken() == null) {
				map.put("status", 402);
				return map;
			}
			if (driver.getApiToken() == null || driver.getApiToken().isEmpty() || driver.getVeryficationCode() == null
					|| driver.getPhone() == null || driver.getPhone().isEmpty()
					|| driver.getVeryficationCode().toString().length() < 5) {
				map.put("status", 402);
				return map;
			}
			em.getTransaction().begin();
			Query q = em.createQuery("from Driver where apiToken=:Api");
			q.setParameter("Api", driver.getApiToken());

			// String user_id = null;

			List<Driver> driv = q.getResultList();
			if (driv.isEmpty()) {
				map.put("status", 400);
				return map;
			}
			for (Driver d : driv) {
				isAvailable = true;
				verficationCode = d.getVeryficationCode();
				if (driver.getVeryficationCode().equals(d.getVeryficationCode())) {
					d.setVeryficationCode(null);
				}
				if (prepareTelephoneNumber(driver.getPhone()).equals(prepareTelephoneNumber(d.getPhone()))) {

					em.persist(d);
					map.put("status", 200);
					map.put("apiToken", d.getApiToken());
					return map;
				} else if (!prepareTelephoneNumber(driver.getPhone()).equals(prepareTelephoneNumber(d.getPhone()))) {

					d.setPhone(prepareTelephoneNumber(driver.getPhone()));
					em.persist(d);
					map.put("status", 300);
					map.put("apiToken", d.getApiToken());
					// } else if
					// (!u.getVeryficationCode().equals(user.getVeryficationCode()))
					// {
					// map.put("status", 300);
					// map.put("apiToken", u.getApiToken());
					// }
				} else if (!driver.getVeryficationCode().equals(d.getVeryficationCode())) {
					map.put("status", 401);
				}
				// #
				// map.put("code",u.getVeryficationCode());
				return map;

			}
		} catch (Exception e) {
			// Map<String,Object> map=new HashMap<>();
			// map.put("status",404);
			em.getTransaction().rollback();
			throw e;
			// return map;
		} finally {
			if (em.getTransaction().isActive()) {
				em.getTransaction().commit();
			}
			em.close();
		}
		return null;
	}

	@RequestMapping("/updateDriverData")
	public @ResponseBody Map<String, Object> updateDriverData(UpdateData driver) {
		EntityManager em = entityManagerFactory.createEntityManager();
		Map<String, Object> map = new HashMap<>();
		Boolean isAvailable = false;
		em.getTransaction().begin();
		// Integer verficationCode=null;
		try {
			// if (userBody != null) {
			// user = userBody;
			// }

			if (driver.getApiToken() == null || driver.getApiToken().isEmpty()) {
				map.put("status", 402);
				map.put("message", "The ApiToken mustn't be null ");
				return map;
			}

			Query q = em.createQuery("from Driver where apiToken=:Api");
			q.setParameter("Api", driver.getApiToken());

			// String user_id = null;

			List<Driver> driv = q.getResultList();
			if (driv == null) {
				map.put("status", 400);
				return map;
			}
			for (Driver newDriver : driv) {
				isAvailable = true;

				if (driver.getName() != null)
					newDriver.setName(driver.getName());
				if (driver.getPhone() != null)
					newDriver.setPhone(prepareTelephoneNumber(driver.getPhone()));
				if (driver.getEmail() != null && driver.getEmail().length() > 4) {
					try {

						if (driver.getEmail().equals(newDriver.getEmail())) {
							map.put("status", 403);
							return map;
						}

						newDriver.setEmail(driver.getEmail());
					} catch (Exception e) {
						map.put("status", 401);
						return map;
						// throw e;
					}
				}
				if (driver.getLatitude() != null)
					newDriver.setLatitude(driver.getLatitude());
				if (driver.getLongitude() != null)
					newDriver.setLongitude(driver.getLongitude());

				try {

					if (driver.getUserImage() != null)
						newDriver.setUserImage(Utility.uploadFile(driver.getUserImage()));

				} catch (Exception ex) {
					ByteArrayOutputStream os = new ByteArrayOutputStream();
					ex.printStackTrace(new PrintWriter(os));
					Map<String, Object> json = new HashMap<>();
					json.put("status", "upload error");
					json.put("error", os.toString());
					return json;
				}
				em.persist(newDriver);
				if (driver.getUserImage() != null) {
					map.put("status", 300);
					map.put("imageURL", newDriver.getUserImage());
				} else
					map.put("status", 200);
				// map.put("imageURL", newUser.getUserImage());
				return map;
			}

			if (!isAvailable) {
				map.put("status", 400);
				return map;
			}

		} catch (Exception e) {
			// Map<String,Object> map=new HashMap<>();
			map.put("status", 404);
			em.getTransaction().rollback();
			throw e;
			// return map;
		} finally {
			if (em.getTransaction().isActive()) {
				em.getTransaction().commit();
			}
			em.close();
		}
		return null;
	}

	@RequestMapping("/updateDriverPhone")
	public @ResponseBody Map<String, Object> updateDriverPhone(UpdateData driver,
			@RequestBody(required = false) UpdateData driverBody) {
		EntityManager em = entityManagerFactory.createEntityManager();
		em.getTransaction().begin();
		Map<String, Object> map = new HashMap<>();
		// Integer verficationCode=null;
		try {
			if (driverBody != null) {
				driver = driverBody;
			}
			if (driver.getApiToken() == null) {
				map.put("status", 402);
				map.put("message", "The ApiToken mustn't be null ");
				return map;
			}
			if (driver.getApiToken().isEmpty() || driver.getPhone() == null) {
				map.put("status", 402);
				map.put("message", "The ApiToken mustn't be null ");
				return map;
			}
			if (driver.getPhone().length() < 9) {
				map.put("status", 401);
				return map;
			}
			Query q = em.createQuery("from Driver where apiToken=:Api");
			q.setParameter("Api", driver.getApiToken());

			// String user_id = null;

			List<Driver> driv = q.getResultList();
			if (driv.isEmpty()) {
				map.put("status", 400);
				return map;
			}
			for (Driver d : driv) {
//				isAvailable = true;
				if (prepareTelephoneNumber(d.getPhone()).equals(prepareTelephoneNumber(driver.getPhone()))) {
					map.put("status", 403);
					return map;
				} else if (!prepareTelephoneNumber(d.getPhone()).equals(prepareTelephoneNumber(driver.getPhone()))) {
					d.setPhone(prepareTelephoneNumber(driver.getPhone()));

					SecureRandom rand = new SecureRandom();
					int num = rand.nextInt(100000);
					String formatted = String.format("%05d", num);
					int veryficationCode1 = Integer.valueOf(formatted);
					d.setVeryficationCode(veryficationCode1);
					em.persist(d);
					map.put("status", 200);
					return map;
				}
			}
		} catch (Exception e) {
			// Map<String,Object> map=new HashMap<>();
			map.put("status", 404);
			em.getTransaction().rollback();
			throw e;
			// return map;
		} finally {
			if (em.getTransaction().isActive()) {
				em.getTransaction().commit();
			}
			em.close();
		}
		return null;
	}

	@RequestMapping("/driverContactUs")
	public @ResponseBody Map<String, Object> driverContactUs(Contactus contactus,
			@RequestBody(required = false) Contactus contactusBody) {
		EntityManager em = entityManagerFactory.createEntityManager();
		Map<String, Object> map = new HashMap<>();
		// Integer verficationCode=null;
		em.getTransaction().begin();
		try {
			if (contactusBody != null) {
				contactus = contactusBody;
			}

			if (contactus.getApiToken() == null || contactus.getApiToken().isEmpty() || contactus.getTitle() == null
					|| contactus.getMessage() == null) {
				map.put("status", 402);
				return map;
			}
			Query q = em.createQuery("from Driver where apiToken=:Api");
			q.setParameter("Api", contactus.getApiToken());

			// String user_id = null;

			List<Driver> driv = q.getResultList();

			if (driv.isEmpty()) {
				map.put("status", 400);
				return map;
			}

			if (contactus.getTitle().length() > 256 && contactus.getMessage().length() > 1000) {
				map.put("status", 402);
				map.put("reason", "title should be lower than 256 and message lower than 1000");
				return map;
			}
			Contactus newContactus = new Contactus();
			newContactus.setApiToken(contactus.getApiToken());
			newContactus.setTitle(contactus.getTitle());
			newContactus.setMessage(contactus.getMessage());
			newContactus.setType("driver");
			newContactus.setDriverId(driv.get(0));
			//newContactus.set
			em.persist(newContactus);
			em.getTransaction().commit();
			map.put("status", 200);
			return map;
		} catch (Exception e) {
			// Map<String,Object> map=new HashMap<>();
			map.put("status", 404);
			em.getTransaction().rollback();
			// throw e;
			return map;
		} finally {
			if (em.getTransaction().isActive()) {
				em.getTransaction().commit();
			}
			em.close();
		}
	}

	@RequestMapping("/changePassword")
	public @ResponseBody Map<String, Object> changePassword(ChangePassword driver,
			@RequestBody(required = false) ChangePassword driverBody) {
		EntityManager em = entityManagerFactory.createEntityManager();
		Map<String, Object> map = new HashMap<>();
		// Integer verficationCode=null;
		em.getTransaction().begin();
		try {
			if (driverBody != null) {
				driver = driverBody;
			}

			if (driver.getApiToken() == null || driver.getApiToken().isEmpty() || driver.getNewPassword() == null || driver.getNewPassword().isEmpty()) { 
				map.put("status", 402);
				return map;
			}
			if (driver.getNewPassword().length() < 6) {
				map.put("status", 403);
				return map;
			}
			Query q = em.createQuery("from Driver where apiToken=:Api");
			q.setParameter("Api", driver.getApiToken());
			// String user_id = null;

			List<Driver> driv = q.getResultList();

			if (driv.isEmpty()) {
				map.put("status", 400);
				return map;
			}
			for (Driver d : driv) {
				if (d.getStatus().equals("متاح") && 
						((d.getVeryficationCode() != null) || DefaultPasswordHasher.getInstance().isPasswordValid(
								driver.getCurrentPassword(), d.getPassword()))) {
					d.setPassword(DefaultPasswordHasher.getInstance().hashPassword(driver.getNewPassword()));
					d.setVeryficationCode(null);
					em.persist(d);
					map.put("status", 200);
					return map;
				} else {
					map.put("status", 405);
					map.put("reason", "current password is error");
					return map;
				}
			}
			

		} catch (Exception e) {
			// Map<String,Object> map=new HashMap<>();
			map.put("status", 404);
			em.getTransaction().rollback();
			throw e;
			// return map;
		} finally {
			if (em.getTransaction().isActive()) {
				em.getTransaction().commit();
			}
			em.close();
		}

		map.put("status", 501);
		map.put("driver_status", "غير_متاح");
		return map;
	}

	@RequestMapping("/getDriverHistory")
	public @ResponseBody Map<String, Object> getDriverHistory(GetHistory theOrder,
			@RequestBody(required = false) GetHistory theOrderBody) {
		EntityManager em = entityManagerFactory.createEntityManager();
		Map<String, Object> map = new HashMap<>();
		em.getTransaction().begin();
		// Integer verficationCode=null;
		try {
			if (theOrderBody != null) {
				theOrder = theOrderBody;
			}
			if (theOrder.getApiToken() == null || theOrder.getApiToken().isEmpty()) {
				map.put("status", 402);
				return map;
			}
			Query q = em.createQuery("from Driver where apiToken=:Api");
			q.setParameter("Api", theOrder.getApiToken());

			// String user_id = null;

			List<Driver> driv = q.getResultList();

			if (driv.isEmpty()) {
				Map<String, Object> map1 = new HashMap<>();
				map1.put("status", 400);
				return map1;
			}
			// List<Map<String, Object>> questions = new ArrayList<>();
			List<Map<String, Object>> list = new ArrayList<>();
			// q = null;

			if (theOrder.getRunning() == 0) {
				q = em.createQuery("from TheOrder where driver_id.apiToken=:Api and status!=:b");
			} else {
				q = em.createQuery("from TheOrder where driver_id.apiToken=:Api and status=:b");
			}

			if (theOrder.getPage() == null)
				theOrder.setPage(1);
			q.setParameter("Api", theOrder.getApiToken());
			q.setParameter("b", "جارى");
			q.setFirstResult((theOrder.getPage() - 1) * PAGINATION_GET_HELP);
			q.setMaxResults(PAGINATION_GET_HELP);

			// String user_id = null;

			List<TheOrder> ord = q.getResultList();
			if (ord == null) {
				map.put("status", 400);
				map.put("history", list);
				return map;
			}
			for (TheOrder o : ord) {
				// map.put("staus", 200);
				Map<String, Object> historyOne = new HashMap<>();
				historyOne.put("DriverName", o.getDriver_id().getName());
				historyOne.put("Status", o.getStatus());
				historyOne.put("date", o.getCreatedAt());
				list.add(historyOne);
			}
			Map<String, Object> map1 = new HashMap<>();
			map1.put("status", 200);
			map1.put("History", list);
			return map1;
		} catch (Exception e) {
			// Map<String,Object> map=new HashMap<>();
			map.put("status", 404);
			em.getTransaction().rollback();
			throw e;
			// return map;
		} finally {
			if (em.getTransaction().isActive()) {
				em.getTransaction().commit();
			}
			em.close();
		}
	}

	@RequestMapping("/driverProfit")
	public @ResponseBody Map<String, Object> driverProfit(DriverProfit driverProfit,
			@RequestBody(required = false) DriverProfit driverProfitBody) {
		EntityManager em = entityManagerFactory.createEntityManager();
		Map<String, Object> map = new HashMap<>();
		em.getTransaction().begin();
		Boolean isAvailable = false;
		int month = 0;
		int year = 0;
		// Integer verficationCode=null;
		try {
			if (driverProfitBody != null) {
				driverProfit = driverProfitBody;
			}
			if (driverProfit.getApiToken() == null || driverProfit.getApiToken().isEmpty()) {
				map.put("status", 402);
				return map;
			}
			Query q = em.createQuery("from Driver where apiToken=:Api");
			q.setParameter("Api", driverProfit.getApiToken());

			// String user_id = null;

			List<Driver> driv = q.getResultList();

			if (driv.isEmpty()) {
				// Map<String, Object> map1 = new HashMap<>();
				map.put("status", 400);
				return map;
			}
			if (driverProfit.getMonth() != null) {
				if (driverProfit.getMonth() < 0 || driverProfit.getMonth() > 13) {
					map.put("status", 403);
					return map;
				}
			} else {
				Date date = new Date();
				Calendar cal = Calendar.getInstance();
				cal.setTime(date);
				month = cal.get(Calendar.MONTH) + 1;
				// driverProfit.setMonth(month);
			}

			if (driverProfit.getYear() != null) {
				if (driverProfit.getYear() < 2016) {
					map.put("status", 401);
					return map;
				}
			} else {
				Date date = new Date();
				Calendar cal = Calendar.getInstance();
				cal.setTime(date);
				year = cal.get(Calendar.YEAR);
				// driverProfit.setMonth(year);
			}

			q = em.createQuery(
					"select sum(money) from Profit p where p.driver_id.apiToken=:Api and month(p.createdAt)=:mon "
							+ "and year(p.createdAt)=:year");
			q.setParameter("Api", driverProfit.getApiToken());
			if (driverProfit.getMonth() == null)
				q.setParameter("mon", month);
			else
				q.setParameter("mon", driverProfit.getMonth());
			if (driverProfit.getYear() == null)
				q.setParameter("year", year);
			else
				q.setParameter("year", driverProfit.getYear());
			Double prof = (Double) q.getSingleResult();
			map.put("status", 200);
			map.put("profit", prof);
			return map;
		} catch (Exception e) {
			// Map<String,Object> map=new HashMap<>();
			map.put("status", 404);
			em.getTransaction().rollback();
			throw e;
			// return map;
		} finally {
			if (em.getTransaction().isActive()) {
				em.getTransaction().commit();
			}
			em.close();
		}
	}

	@RequestMapping("/request")
	@SuppressWarnings(value="unchecked")
	public @ResponseBody Map<String, Object> request(Request request,
			@RequestBody(required = false) Request requestBody) {
		EntityManager em = entityManagerFactory.createEntityManager();
		em.getTransaction().begin();
		if (request != null) {
			request = requestBody;
		}

		Query q = em.createQuery("select user from User user where apiToken = :a", User.class);
		q.setParameter("a", request.getApiToken());
		User user = (User) q.getSingleResult();

		RestTemplate temp = new RestTemplate();
		Map<String, Object> decodingRes = temp.getForObject("https://maps.googleapis.com/maps/api/geocode/json?latlng="
			       	+ user.getLatitude() + "," + user.getLongitude()
				+ "&language=en&key=AIzaSyD3zFkFvHEySIQ7Md3mEPbCoRATs8kUhDA", Map.class);
		List<Map<String, Object>> results = (List<Map<String, Object>>) decodingRes.get("results");
		String adminLevel1 = null;
		String locality = null;
		for(Map<String, Object> ent : results) {
			List<Map<String, Object>> addressComponents = (List<Map<String, Object>>) ent.get("address_components");
			for(Map<String, Object> component : addressComponents) {
				List<String> types = (List<String>) component.get("types");
				if (types.contains("locality")) {
					locality = component.get("long_name").toString();
				}
				if (types.contains("administrative_area_level_1")) {
					adminLevel1 = component.get("long_name").toString();
				}
			}
		}
		if (adminLevel1 != null) {
			request.setCity(adminLevel1);
		}
		if (locality != null) {
			request.setCity(locality);
		}

		q = em.createQuery("select madina from Madina madina where name = :n", Madina.class);
		q.setParameter("n", request.getCity());

		Madina city = (Madina) q.getSingleResult();
		if (city == null) {
			Map<String, Object> map = new HashMap<>();
			map.put("status", "no such city");
			return map;
		}

		TheOrder order = new TheOrder();
		order.setUser_id(user);
		order.setLatitude(request.getLatitude());
		order.setLongitude(request.getLongitude());
		order.setAddress(request.getAddress());
		order.setCost(city.getPrice());

		em.persist(order);
		em.getTransaction().commit();
		asyncs.carGet(user, order);
		Map<String, Object> map = new HashMap<>();
		map.put("status", "done");
		return map;
	}
	
	
	

	
//	@SuppressWarnings("unchecked")
//	@RequestMapping(path = "/api/sendMessage")
//	public @ResponseBody Map< String , Object> restClient(@RequestBody apiSendMessage api) {
//		Map<String, Object> ret = new HashMap<>();
//		
//		if(api.getUserName()==null || Integer.valueOf(api.getPassword()) == null 
//			|| Integer.valueOf(api.getNumbers()) == null || api.getMessage() == null 
//			||api.getSender() == null){
//				ret.put("errorCode",101);
//				return ret;
//			}
//		try{
//		ret.put("username",api.getUserName());
//		ret.put("password", api.getPassword());
//		ret.put("message", api.getMessage());
//		ret.put("numbers",api.getNumbers());
//		ret.put("sender", api.getSender());
//		ret.put("return",api.getReturnValue());
//		ret.put("unicode",api.getUnicode());
//		ret.put("datetime",api.getDatetime());
//		
//	
//		Integer apiSend =  new RestTemplate().postForObject(
//				"http://www.ksa-sms.com/api/sendsms.php",
//				ret,
//				Integer.class, //apiSendMessage.class,
//				new HashMap<>());
//		ret.put("errorCode", apiSend);
//		
//		//return  apiSend;
//		}
//		catch(Exception e)
//		{
//			ret.put("errorCode",110 );
//			
//		}
//		ret.put("errorCode",100);
//		return ret;
//		
//	}
	
	
	 
}
