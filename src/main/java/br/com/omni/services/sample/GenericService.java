package br.com.omni.services.sample;

import static br.com.omni.services.sample.RequestMethod.GET;
import static br.com.omni.services.sample.RequestMethod.POST;
import static br.com.omni.services.sample.RequestMethod.PUT;
import static br.com.omni.services.sample.ServiceHelper.getConn;
import static br.com.omni.services.sample.ServiceHelper.getUrl;
import static br.com.omni.services.sample.ServiceHelper.isSucess;
import static br.com.omni.services.sample.ServiceHelper.receiveAndShowJsonArray;
import static br.com.omni.services.sample.ServiceHelper.sendJson;

import java.net.HttpURLConnection;
import java.util.Optional;
import java.util.function.Consumer;

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
	 * @param complement - REST URL to call
	 * @param requestMethod - Which HTTP METHOD shoud be used
	 * @param postCall - Function to be executed after the call
	 */
	protected static void httpCall(final String complement, RequestMethod requestMethod) {
		httpCall(complement, requestMethod, Optional.empty(), Optional.empty());
	}
	/**
	 * Call a HTTP and having as response code SUCCESS, invoke the post call function
	 * 
	 * @param complement - REST URL to call
	 * @param requestMethod - Which HTTP METHOD shoud be used
	 * @param postCall - Function to be executed after the call
	 */
	protected static void httpCall(final String complement, RequestMethod requestMethod, Consumer<HttpURLConnection> postCall) {
		httpCall(complement, requestMethod, Optional.empty(), Optional.of(postCall));
	}
	/**
	 * Call a HTTP and execute (if present) the pre function, this may be used to send values to your requisition 
	 * and having as response code SUCCESS, invoke the post call function
	 * 
	 * @param complement - REST URL to call
	 * @param requestMethod - Which HTTP METHOD shoud be used
	 * @param preCall - Function to be executed before the call
	 * @param postCall - Function to be executed after the call
	 */
	protected static void httpCall(final String complement, RequestMethod requestMethod, Optional<Consumer<HttpURLConnection>> preCall, Optional<Consumer<HttpURLConnection>> postCall) {
		HttpURLConnection con = getConn(getUrl().concat(complement), requestMethod);
		
		if (preCall.isPresent()) preCall.get().accept(con);
		
		if (isSucess(con))
			if (postCall.isPresent()) postCall.get().accept(con);
	}
	
	/**
	 * Invokes the specific service in listing all
	 */
	protected static void requestAll() {
		httpCall("", GET, con -> receiveAndShowJsonArray(con));
	}

	/**
	 * Invokes the specific service in get a specific object
	 * @param id - Object to get
	 */
	protected static void request(int id) {
		httpCall(String.format("/%1$1s",id), GET, ServiceHelper::receiveAndShowJsonObject);
	}

	/**
	 * Invokes the specific service in listing all with pagination
	 * @param offset - Initial register  
	 * @param limit - How many registers
	 */
	protected static void request(int offset, int limit) {
		httpCall(String.format("?offset=%1$1s&limit=%2$1s",offset,limit).toString(), GET, ServiceHelper::receiveAndShowJsonArray);
	}

	/**
	 * Invokes the specific service to create
	 */
	protected static void create() {
		httpCall("", POST, Optional.of(con -> sendJson(con, Optional.empty())), Optional.of(ServiceHelper::receiveAndShowJsonObject));
	}

	/**
	 * Invokes the specific service in update an existing register
	 * @param method - Method to be used (compatibility with services which use POST/PUT methods)
	 */
	protected static void update(Optional<RequestMethod> method) {
		httpCall("", method.orElse(PUT), Optional.of(con -> sendJson(con, Optional.of("_update"))), Optional.of(ServiceHelper::receiveAndShowJsonObject));
	}

	/**
	 * Invokes the specific service to delete an existing register
	 */
	protected static void delete(int id) {
		httpCall(String.format("/%1$1s",id), RequestMethod.DELETE);
	}
}
