package br.com.omni.services.sample.knowledgebase;

import static br.com.omni.services.sample.RequestMethod.GET;
import static br.com.omni.services.sample.ServiceHelper.encode;

import java.util.Optional;

import br.com.omni.services.sample.Functionality;
import br.com.omni.services.sample.GenericService;
import br.com.omni.services.sample.ServiceHelper;

@Functionality("portal")
public class PortalService extends GenericService {
	private static final int FIND_ID = 1;
	private static final String FIND_NAME = "Global Portal";
	private static final int DELETE_ID = 10;

	public static void main(String[] args) throws Exception {
		//System.out.println("Requesting all");
		//requestAll();
		
		//System.out.format("\n\nRequesting by ID (%1$1s)\n",FIND_ID);
		//request(FIND_ID);

		//System.out.format("\n\nRequesting by Name (%1$1s)\n",FIND_NAME);
		//request(FIND_NAME);

		//System.out.println("\n\nRequesting 2 of 3");
		//request(2,2);

		//System.out.println("\n\nRequesting all active");
		//request(false);

		//System.out.println("\n\nRequesting all inactive");
		//request(true);
		
		//System.out.println("\n\nCreating a new");
		//create();

		System.out.println("\n\nUpdating");
		update(Optional.empty());

		//System.out.format("\n\nDeleting (%1$1s)\n",DELETE_ID);
		//delete(DELETE_ID);
	}

	private static void request(String name) {
		httpCall(String.format("?portal=%1$1s",encode(name)), GET, Optional.empty(), Optional.of(ServiceHelper::receiveAndShowJsonArray));
	}

	private static void request(boolean inactive) {
		httpCall(String.format("?inactive=%1$1s",(inactive?"Y":"N")), GET, Optional.empty(), Optional.of(ServiceHelper::receiveAndShowJsonArray));
	}

}