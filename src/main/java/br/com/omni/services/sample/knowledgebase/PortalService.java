package br.com.omni.services.sample.knowledgebase;

import br.com.omni.services.sample.Functionality;
import br.com.omni.services.sample.GenericService;

@Functionality("portal")
public class PortalService extends GenericService {
	private static final int FIND_ID = 1;
	private static final String FIND_NAME = "Global Portal";
	private static final int DELETE_ID = 10;

	public static void main(String[] args) {
		PortalService service = new PortalService();
		
		System.out.println("Requesting all");
		service.requestAll();
		
		System.out.format("\n\nRequesting by ID (%1$1s)\n",FIND_ID);
		service.request(FIND_ID);

		System.out.format("\n\nRequesting by Name (%1$1s)\n",FIND_NAME);
		service.request(FIND_NAME);

		//System.out.println("\n\nRequesting 2 of 3");
		//service.request(2,2);

		System.out.println("\n\nRequesting all active");
		service.request(false);

		System.out.println("\n\nRequesting all inactive");
		service.request(true);
		
		//System.out.println("\n\nCreating a new");
		//service.create();

		//System.out.println("\n\nUpdating");
		//service.update(Optional.empty());

		//System.out.format("\n\nDeleting (%1$1s)\n",DELETE_ID);
		//service.delete(DELETE_ID);
	}

}