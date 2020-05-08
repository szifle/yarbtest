package seleniumFirstTest;

import java.util.Arrays;

import javax.ws.rs.client.ClientBuilder;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import de.zifle.meintestprojekt.api.BearerAuthentication;
import de.zifle.meintestprojekt.api.DefaultApi;
import de.zifle.meintestprojekt.api.YarbJacksonProvider;
import de.zifle.meintestprojekt.model.Board;
import de.zifle.meintestprojekt.model.BoardColumn;
import de.zifle.meintestprojekt.model.BoardNote;
import de.zifle.meintestprojekt.model.CreateBoard;
import de.zifle.meintestprojekt.model.CreateBoardNote;
import de.zifle.meintestprojekt.model.CreatedResponse;
import de.zifle.meintestprojekt.model.LoginData;
import de.zifle.meintestprojekt.model.UserCredentials;

public class YarbApi {

	private final DefaultApi api;
	private static YarbApi instance;
	private String token = null;

	public YarbApi() {	
		ResteasyClient client = (ResteasyClient) ClientBuilder.newClient().register(new YarbJacksonProvider());
		ResteasyWebTarget target = client.target("https://192.168.178.32:9443/yarb");

		BearerAuthentication bearerAuthentication = new BearerAuthentication(this::getToken);
		target.register(bearerAuthentication);

		api = target.proxy(DefaultApi.class);
	}

	public static YarbApi getInstance() {
		if (instance == null) {
			instance = new YarbApi();
		}
		return instance;
	}

	public void createUser(String userName, String password) throws Throwable {
		UserCredentials userCredentials = new UserCredentials();
		userCredentials.setUsername(userName);
		userCredentials.setPassword(password);

		api.createUser(userCredentials);

	}

	public Integer createBoard(String boardName, String... columnNames) {
		CreateBoard board = new CreateBoard();

		board.setName(boardName);
		board.setColumnNames(Arrays.asList(columnNames));

		return api.createBoard(board).getId();
	}
	
	public Board getBoard(Integer boardID) {
		return api.getBoard(boardID);
	}
	
	public Integer createNote(BoardColumn column, String content) {
		CreateBoardNote note = new CreateBoardNote();
		note.setContent(content);
		note.setBoardColumnId(column.getId());
		
		CreatedResponse resp = api.createNote(note);
		return resp.getId();
	}
	
	public void setVote(Integer noteID) {
		api.postVote(noteID);
	}

	public void login(String userName, String password) throws Throwable {
		UserCredentials userCredentials = new UserCredentials();
		userCredentials.setUsername(userName);
		userCredentials.setPassword(password);
		
		LoginData loginData = api.login(userCredentials); 
		token = loginData.getToken();
	}

	private String getToken() {
		return token;
	}

	
}
