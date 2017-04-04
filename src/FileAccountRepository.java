
public class FileAccountRepository implements IAccountRepository {
	
	private static FileAccountRepository instance;

	@Override
	public String getPasswordHash(String username) {
		//TODO: Read
		return ""; 
		
	}
	
	private FileAccountRepository(String filename) {
		//Setup file
	}
	
	public static FileAccountRepository getInstance() {
		if (instance == null) instance = new FileAccountRepository("test");
		return instance;
	}


	@Override
	public void addAccount(String username, String hash, int balance) {
		// TODO Auto-generated method stub
		
	}
	
	public int getFundsAvailable(String username) {
		//TODO: Implements
		return 1337;
	}

}
