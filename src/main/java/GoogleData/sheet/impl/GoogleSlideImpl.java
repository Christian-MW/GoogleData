package GoogleData.sheet.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonError;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.slides.v1.Slides;
import com.google.api.services.slides.v1.SlidesScopes;
import com.google.api.services.slides.v1.model.BatchUpdatePresentationRequest;
import com.google.api.services.slides.v1.model.BatchUpdatePresentationResponse;
import com.google.api.services.slides.v1.model.CreateSlideRequest;
import com.google.api.services.slides.v1.model.CreateSlideResponse;
import com.google.api.services.slides.v1.model.DeleteTextRequest;
import com.google.api.services.slides.v1.model.InsertTextRequest;
import com.google.api.services.slides.v1.model.LayoutReference;
import com.google.api.services.slides.v1.model.Page;
import com.google.api.services.slides.v1.model.Presentation;
import com.google.api.services.slides.v1.model.Range;
import com.google.api.services.slides.v1.model.ReplaceAllTextRequest;
import com.google.api.services.slides.v1.model.Request;
import com.google.api.services.slides.v1.model.SubstringMatchCriteria;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;

import GoogleData.sheet.config.GoogleAuthorizationConfig;
import GoogleData.sheet.dto.request.*;
import GoogleData.sheet.dto.response.*;
import GoogleData.sheet.service.GoogleSlideService;
import GoogleData.sheet.utils.Utilities;

@Service("GoogleSlideImpl")
public class GoogleSlideImpl implements GoogleSlideService {
	private static Logger log = Logger.getLogger(GoogleImpl.class);
    @Value("${credentials.file.path}")
    private String credentialsFilePath;
    @Value("${application.name}")
    private String APPLICATIONNAME;
    @Value("${gslides.range.variables}")
    private String RANGE_VARIABLES;
    @Value("${gslides.name.range}")
    private String NAME_SHEET;
    @Autowired
    private static GoogleAuthorizationConfig googleAuthorizationConfig;
    @Autowired
    Utilities utilities;
    @Autowired
    GoogleImpl googleSheetImpl;
    
    public SlideResponse updateDataSlide (SlideRequest request) {
    	log.info("################################################################");
    	log.info("################___UPDATE-DATA-SLIDES-GOOGLE___####################");
    	log.info("=> SLIDE_ID: " + request.getSlide_id());
    	log.info("=> SPREADSHEET_ID: " + request.getSpreadsheet_id());
    	SlideResponse result = new SlideResponse();
    	try {
    		//Obtener credenciales para Google Slides
    		JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
			NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
			JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
			Slides service = new Slides.Builder(HTTP_TRANSPORT, JSON_FACTORY, googleAuthorizationConfig
					.getCredentialsServiceAccountSlide(HTTP_TRANSPORT, jsonFactory, credentialsFilePath))
					.setApplicationName(APPLICATIONNAME).build();
    		
    		//Obtener la información del archivo Sheet
    		String[] arrRangeSH = RANGE_VARIABLES.split(",");
    		SheetRequest reqSh = new SheetRequest();
    		SheetResponse resSh = new SheetResponse();
    		reqSh.setSpreadsheet_id(request.getSpreadsheet_id());
    		reqSh.setRange(NAME_SHEET);
    		reqSh.setColumns(RANGE_VARIABLES);
    		resSh = googleSheetImpl.getDataSheet(reqSh);
    		if (resSh.getCode()==200) {
				log.info("==> Se obtuvo correctamente la información del archivo Sheet: " + request.getSpreadsheet_id());
				for (List<Object> itemObj : resSh.getObjectResult()) {
					if (!itemObj.get(0).toString().toLowerCase().equals(arrRangeSH[0].toLowerCase())) {
						System.out.println("key: " + itemObj.get(0));
						System.out.println("value: " + itemObj.get(1));
						
						List<Request> requests = new ArrayList<>();
						requests.add(new Request().setReplaceAllText(new ReplaceAllTextRequest()
								.setContainsText(new SubstringMatchCriteria().setText(itemObj.get(0).toString().trim()).setMatchCase(true))
								.setReplaceText(itemObj.get(1).toString())));

						BatchUpdatePresentationResponse response = null;
						BatchUpdatePresentationRequest body = new BatchUpdatePresentationRequest().setRequests(requests);
						response = service.presentations().batchUpdate(request.getSlide_id(), body).execute();
						System.out.println(response);
					}
				}
			}
    		else{
    			log.error("==> PROBLEMAS AL OBTENER LA INFORMACIÓN DEL SHEET: " + request.getSpreadsheet_id());
    			log.error(resSh.getCode());
    			log.error(resSh.getMessage());
    		}
    		
    		result.setCode(200);
    		result.setMessage("OK");
    		return result;
		} catch (Exception ex) {
			log.error("**************ERROR__GET-DATA-SLIDES-GOOGLE_**********");
			log.error(ex.getMessage());
			result.setCode(500);
			result.setMessage("ERROR: " + ex.getMessage());
			return result;
		}
    }
    
    
    
	public Slides getServiceSlide() {
		try {
			//log.info("#########_Current Working Directory is = " +this.getClass().getClassLoader().getResource("").getPath() + credentialsFilePath);
			
			NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
			JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
			Slides service = new Slides.Builder(httpTransport, jsonFactory, 
					googleAuthorizationConfig.getCredentialsServiceAccountSlide(httpTransport, jsonFactory, credentialsFilePath))
					.setApplicationName(APPLICATIONNAME).build();
			
			return service;
		} catch (Exception e) {
			System.out.println("=> ERROR__getServiceSheet__: " + e.getMessage());
			return new Slides(null, null, null);
		}
	}
}
