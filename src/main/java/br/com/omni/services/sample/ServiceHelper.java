package br.com.omni.services.sample;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.Base64;
import java.util.Optional;
import java.util.Properties;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;

/**
 * Knows the URL of your REST service to call? Get the HttpUrlConnection? 
 * Receive and send Json, or, encode a string? This is the class.
 * @author Plusoft
 *
 */
public class ServiceHelper {
	private static Properties properties;
	private static String serviceUrl;
	
	static {
		properties = new Properties();
		try {
			properties.load(new FileInputStream("src/main/java/config.properties"));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Return the URL of the service to be called (determined by Functionality annotation)
	 * 
	 * @see Functionality
	 * @return - URL to be called
	 */
	public static String getUrl() {
		if (serviceUrl == null) {
			serviceUrl = properties.getProperty("service.api");
		}
		return MessageFormat.format(getFunctionalityPropertie("url"),serviceUrl);
	}
	
	/**
	 * Open connection with a specific URL
	 * 
	 * @param url - URL to be connected
	 * @param requestMethod - Method to be used
	 * @return - Connection
	 */
	public static HttpURLConnection getConn(final String url, RequestMethod requestMethod) {
		URL obj;
		HttpURLConnection con;
		
		try {
			obj = new URL(url);
			con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod(requestMethod.name());
			con.setRequestProperty("User-Agent", properties.getProperty("config.user_agent"));
			con.setDoOutput(true);
			con.setRequestProperty("Accept", "application/json");
			
			String username = properties.getProperty("config.username");
			String password = properties.getProperty("config.password");
			if ((username == null || username.equals("")) || (password == null || password.equals("")))
				throw new RuntimeException("Please set your username/password in config.properties");
			
			String userPassword = username + ":" + password;
			String encoding = Base64.getEncoder().encodeToString(userPassword.getBytes());
			
			con.setRequestProperty("Authorization", "Basic " + encoding);
			
			if (requestMethod == RequestMethod.POST || requestMethod == RequestMethod.PUT) {
				con.setDoInput(true);
				con.setRequestProperty("Content-Type", "application/json");
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		return con;
	}
	
	/**
	 * If the Connection had not returned a success code, an exception will be throw
	 * @param con - Connection
	 */
	public static boolean isSucess(final HttpURLConnection con) {
		boolean success = false;
		try {
			int responseCode = con.getResponseCode();
			System.out.println("responseCode: "+responseCode);
			if (responseCode>=200 && responseCode<=299) {
				success = true;
			}
		} catch (Exception e) {
			success = false;
		}
		return success;
	}

	/**
	 * Send the JSON defined in src/resources to your Functionality, determined by Functionality annotation
	 * 
	 * @see Functionality
	 * @param con - Connection used to send the JSON
	 * @param extension - Will be concatenate at the file name
	 * @param log - Log the JSON sent?
	 */
	public static void sendJson(final HttpURLConnection con, Optional<String> extension) {
		String file = getFunctionality().concat(extension.orElse(""));
		try (OutputStreamWriter out = new OutputStreamWriter(con.getOutputStream())) {
		
			String content = new String(Files.readAllBytes(Paths.get("src/resources/"+file+".json")));
			out.write(content);
			
			System.out.println("sent: "+content.replaceAll("(\n|\t|\r)", ""));
		} catch (Exception e) {
			throw new RuntimeException(e); 
		}
	}
	
	/**
	 * Read the JSON received
	 * 
	 * @param con - Connection
	 * @return JSON object
	 */
	public static JsonObject receiveJsonObject(final HttpURLConnection con) {
		try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
			return Json.createReader(in).readObject();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Read the JSON received and log
	 * 
	 * @param con - Connection
	 */
	public static void receiveAndShowJsonObject(final HttpURLConnection con) {
		JsonObject jObj = receiveJsonObject(con);
		
		System.out.println("received: "+jObj);
	}

	/**
	 * Read the JSON received
	 * 
	 * @param con - Connection
	 * @return JSON array
	 */
	public static JsonArray receiveJsonArray(final HttpURLConnection con) {
		try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
			return Json.createReader(in).readArray();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Read the JSON received and log
	 * 
	 * @param con - Connection
	 */
	public static void receiveAndShowJsonArray(final HttpURLConnection con) {
		JsonArray jArray = receiveJsonArray(con);
		
		jArray.forEach(j->System.out.println("received: "+j));
	}

	/**
	 * Encode to URL call
	 * @param value - Value to be encoded
	 * @returnValue encoded
	 */
	public static String encode(final String value) {
		try {
			return URLEncoder.encode(value, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Read the functionality defined in the caller class
	 *  
	 * @return Functionality
	 */
	private static String getFunctionality() {
		String functionality = null;
	
		for (StackTraceElement stackTraceElement : Thread.currentThread().getStackTrace()) {
			Class<?> class1 = null;
			try {
				class1 = Class.forName(stackTraceElement.getClassName());
			} catch (ClassNotFoundException e) {
				continue;
			}
			if (class1.isAnnotationPresent(Functionality.class)) {
				functionality = class1.getAnnotation(Functionality.class).value();
				break;
			}
		}
		return functionality;
	}
	
	public static String getFunctionalityPropertie(String name) {
		return properties.getProperty("service.".concat(getFunctionality()).concat(".").concat(name));
	}
}
