package br.com.omni.services.sample.user;

import br.com.omni.services.sample.Functionality;
import br.com.omni.services.sample.GenericService;

@Functionality("team")
public class TeamService extends GenericService {
	private static final int FIND_ID = 1;
	private static final String FIND_NAME = "Time de Retenção 2";

	public static void main(String[] args) {
		TeamService service = new TeamService();
		
		System.out.println("Requesting all");
		service.requestAll();
		
		System.out.format("\n\nRequesting by ID (%1$1s)\n",FIND_ID);
		service.request(FIND_ID);

		System.out.format("\n\nRequesting by Name (%1$1s)\n",FIND_NAME);
		service.request(FIND_NAME);

		System.out.println("\n\nRequesting 2 of 3");
		service.request(2,2);

		System.out.println("\n\nRequesting all active");
		service.request(false);

		System.out.println("\n\nRequesting all inactive");
		service.request(true);
		
		//System.out.println("\n\nCreating a new");
		//service.create();
	}

}
