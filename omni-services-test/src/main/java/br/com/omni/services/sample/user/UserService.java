package br.com.omni.services.sample.user;

import static br.com.omni.services.sample.RequestMethod.GET;
import static br.com.omni.services.sample.ServiceHelper.encode;
import static br.com.omni.services.sample.ServiceHelper.getUrl;
import static br.com.omni.services.sample.ServiceHelper.receiveAndShowJsonArray;

import java.net.HttpURLConnection;
import java.util.function.Function;

import br.com.omni.services.sample.Functionality;
import br.com.omni.services.sample.GenericService;

@Functionality("user")
public class UserService extends GenericService {
	private static final int FIND_ID = 189;
	private static final String FIND_NAME = "Carlos Nunes";

	public static void main(String[] args) {
		System.out.println("Requesting all");
		requestAll();
		
		System.out.format("\n\nRequesting by ID (%1$1s)\n",FIND_ID);
		request(FIND_ID);

		System.out.format("\n\nRequesting by Name (%1$1s)\n",FIND_NAME);
		request(FIND_NAME);

		System.out.println("\n\nRequesting 2 of 3");
		request(2,2);

		System.out.println("\n\nRequesting all active");
		request(false);

		System.out.println("\n\nRequesting all inactive");
		request(true);
		
		//System.out.println("\n\nCreating a new");
		//create();
	}

	private static void request(String name) {
		Function<HttpURLConnection, Void> func = con -> {
			receiveAndShowJsonArray(con);
			return null;
		};
		
		httpCall(String.format("%1$1s?username=%2$1s",getUrl(),encode(name)), GET, func);
	}

	private static void request(boolean inactive) {
		Function<HttpURLConnection, Void> func = con -> {
			receiveAndShowJsonArray(con);
			return null;
		};
		
		httpCall(String.format("%1$1s?inactiverecord=%2$1s",getUrl(),(inactive?"Y":"N")), GET, func);
	}

}
