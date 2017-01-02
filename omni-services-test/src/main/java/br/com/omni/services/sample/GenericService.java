package br.com.omni.services.sample;

import static br.com.omni.services.sample.RequestMethod.GET;
import static br.com.omni.services.sample.RequestMethod.POST;
import static br.com.omni.services.sample.RequestMethod.PUT;
import static br.com.omni.services.sample.ServiceHelper.isSucess;
import static br.com.omni.services.sample.ServiceHelper.getConn;
import static br.com.omni.services.sample.ServiceHelper.getUrl;
import static br.com.omni.services.sample.ServiceHelper.receiveAndShowJsonArray;
import static br.com.omni.services.sample.ServiceHelper.receiveAndShowJsonObject;
import static br.com.omni.services.sample.ServiceHelper.sendJson;

import java.net.HttpURLConnection;
import java.util.Optional;
import java.util.function.Function;

/**
 * A Generic class with the commons requests for all REST services, such as list all objects, get a specific object based on his ID
 * create update and delete as well.
 * @author Plusoft
 *
 */
public abstract class GenericService {
	/**
	 * Call a HTTP service
	 * 
	 * @param url - REST URL to call
	 * @param requestMethod - Which HTTP METHOD shoud be used
	 * @param postCall - Function to be executed after the call
	 */
	protected static void httpCall(final String url, RequestMethod requestMethod) {
		httpCall(url, requestMethod, Optional.empty(), Optional.empty());
	}
	/**
	 * Call a HTTP and having as response code SUCCESS, invoke the post call function
	 * 
	 * @param url - REST URL to call
	 * @param requestMethod - Which HTTP METHOD shoud be used
	 * @param postCall - Function to be executed after the call
	 */
	protected static void httpCall(final String url, RequestMethod requestMethod, Function<HttpURLConnection, Void> postCall) {
		httpCall(url, requestMethod, Optional.empty(), Optional.of(postCall));
	}
	/**
	 * Call a HTTP and execute (if present) the pre function, this may be used to send values to your requisition 
	 * and having as response code SUCCESS, invoke the post call function
	 * 
	 * @param url - REST URL to call
	 * @param requestMethod - Which HTTP METHOD shoud be used
	 * @param preCall - Function to be executed before the call
	 * @param postCall - Function to be executed after the call
	 */
	protected static void httpCall(final String url, RequestMethod requestMethod, Optional<Function<HttpURLConnection, Void>> preCall, Optional<Function<HttpURLConnection, Void>> postCall) {
		HttpURLConnection con = getConn(url, requestMethod);
		
		if (preCall.isPresent()) preCall.get().apply(con);
		
		if (isSucess(con))
			if (postCall.isPresent()) postCall.get().apply(con);
	}
	
	/**
	 * Invokes the specific service in listing all
	 */
	protected static void requestAll() {
		Function<HttpURLConnection, Void> func = con -> {
			receiveAndShowJsonArray(con);
			return null;
		};
		
		httpCall(getUrl(), GET, func);
	}

	/**
	 * Invokes the specific service in get a specific object
	 * @param id - Object to get
	 */
	protected static void request(int id) {
		Function<HttpURLConnection, Void> func = con -> {
			receiveAndShowJsonObject(con);
			return null;
		};
		
		httpCall(String.format("%1$1s/%2$1s",getUrl(),id), GET, func);
	}

	/**
	 * Invokes the specific service in listing all with pagination
	 * @param offset - Initial register  
	 * @param limit - How many registers
	 */
	protected static void request(int offset, int limit) {
		Function<HttpURLConnection, Void> func = con -> {
			receiveAndShowJsonArray(con);
			return null;
		};
		
		httpCall(String.format("%1$1s?offset=%2$1s&limit=%3$1s", getUrl(),offset,limit).toString(), GET, func);
	}

	/**
	 * Invokes the specific service to create
	 */
	protected static void create() {
		Function<HttpURLConnection, Void> funcPre = con -> {
			sendJson(con, Optional.empty());
			return null;
		};
		Function<HttpURLConnection, Void> funcPos = con -> {
			receiveAndShowJsonObject(con);
			return null;
		};
		
		httpCall(getUrl(), POST, Optional.of(funcPre), Optional.of(funcPos));
	}

	/**
	 * Invokes the specific service in update an existing register
	 * @param method - Method to be used (compatibility with services which use POST/PUT methods)
	 */
	protected static void update(Optional<RequestMethod> method) {
		Function<HttpURLConnection, Void> funcPre = con -> {
			sendJson(con, Optional.of("_update"));
			return null;
		};
		Function<HttpURLConnection, Void> funcPos = con -> {
			receiveAndShowJsonObject(con);
			return null;
		};
		
		httpCall(getUrl(), method.orElse(PUT), Optional.of(funcPre), Optional.of(funcPos));
	}

	/**
	 * Invokes the specific service to delete an existing register
	 */
	protected static void delete(int id) {
		httpCall(String.format("%1$1s/%2$1s",getUrl(),id), RequestMethod.DELETE);
	}
}
