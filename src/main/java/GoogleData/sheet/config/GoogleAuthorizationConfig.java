package GoogleData.sheet.config;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.Permission;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.slides.v1.SlidesScopes;

import GoogleData.sheet.impl.GoogleImpl;
import GoogleData.sheet.impl.GoogleSlideImpl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

@Configuration
public class GoogleAuthorizationConfig {

    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    @Value("${application.name}")
    private String applicationName;
    @Value("${credentials.file.path}")
    private static String credentialsFilePathFile;

    
    private static final List<String> SCOPES = Collections.singletonList(SheetsScopes.SPREADSHEETS);
    private static final List<String> SCOPESSLIDE = Collections.singletonList(SlidesScopes.PRESENTATIONS);
    private static final List<String> SCOPESDRIVE = Collections.singletonList(DriveScopes.DRIVE_FILE);
    
	public static Credential getCredentialsServiceAccount(NetHttpTransport httpTransport, JsonFactory jsonFactory,
			String credentialsFilePath) throws IOException {
		InputStream in = GoogleImpl.class.getResourceAsStream(credentialsFilePath);
		return GoogleCredential.fromStream(in, httpTransport, jsonFactory).createScoped(SCOPES);
	}
    
	public static Credential getCredentialsServiceAccountSlide(NetHttpTransport HTTP_TRANSPORT, JsonFactory jsonFactory,
			String credentialsFilePath) throws IOException {
		String TOKENS_DIRECTORY_PATH = "tokens";

		// Load client secrets.
		InputStream in = GoogleSlideImpl.class.getResourceAsStream(credentialsFilePath);
		if (in == null) {
			throw new FileNotFoundException("Resource not found: " + credentialsFilePath);
		}
		return GoogleCredential.fromStream(in, HTTP_TRANSPORT, jsonFactory).createScoped(SCOPESSLIDE);
	}
	
    public static Credential getCredentialsDrive(NetHttpTransport httpTransport, JsonFactory jsonFactory,
			String credentialsFilePath) throws IOException {
    	try {
    		String TOKENS_DIRECTORY_PATH = "tokens";

    		// Load client secrets.
    		InputStream in = GoogleSlideImpl.class.getResourceAsStream(credentialsFilePath);
    		if (in == null) {
    			throw new FileNotFoundException("Resource not found: " + credentialsFilePath);
    		}
    		return GoogleCredential.fromStream(in, httpTransport, jsonFactory).createScoped(SCOPESDRIVE);
		} catch (Exception ex) {
			return null;
		}
    }
}
