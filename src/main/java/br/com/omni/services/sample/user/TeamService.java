package br.com.omni.services.sample.user;

import static br.com.omni.services.sample.RequestMethod.GET;
import static br.com.omni.services.sample.ServiceHelper.encode;
import br.com.omni.services.sample.Functionality;
import br.com.omni.services.sample.GenericService;
import br.com.omni.services.sample.ServiceHelper;

@Functionality("team")
public class TeamService extends GenericService {
	private static final int FIND_ID = 1;
	private static final String FIND_NAME = "Time de Retenção 2";

	public static void main(String[] args) {
		System.out.println("Requesting all");
		requestAll();
		
		System.out.format("\n\nRequesting by ID (%1$1s)\n",FIND_ID);
		request(FIND_ID);

		System.out.format("\n\nRequesting by Name (%1$1s)\n",FIND_NAME);
		request(FIND_NAME);

		System.out.println("\n\nRequesting 2 of 3");
		request(1,2);

		System.out.println("\n\nRequesting all active");
		request(false);

		System.out.println("\n\nRequesting all inactive");
		request(true);
		
		//System.out.println("\n\nCreating a new");
		//create();
	}

	private static void request(String name) {
		httpCall(String.format("?teamname=%1$1s",encode(name)), GET, ServiceHelper::receiveAndShowJsonArray);
	}

	private static void request(boolean inactive) {
		httpCall(String.format("?inactiverecord=%1$1s",(inactive?"Y":"N")), GET, ServiceHelper::receiveAndShowJsonArray);
	}

}
