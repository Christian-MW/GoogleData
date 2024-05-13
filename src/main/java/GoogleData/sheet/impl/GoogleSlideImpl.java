package GoogleData.sheet.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

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
import com.google.api.services.slides.v1.model.AffineTransform;
import com.google.api.services.slides.v1.model.BatchUpdatePresentationRequest;
import com.google.api.services.slides.v1.model.BatchUpdatePresentationResponse;
import com.google.api.services.slides.v1.model.CreateImageRequest;
import com.google.api.services.slides.v1.model.CreateImageResponse;
import com.google.api.services.slides.v1.model.CreateSlideRequest;
import com.google.api.services.slides.v1.model.CreateSlideResponse;
import com.google.api.services.slides.v1.model.DeleteTextRequest;
import com.google.api.services.slides.v1.model.Dimension;
import com.google.api.services.slides.v1.model.InsertTextRequest;
import com.google.api.services.slides.v1.model.LayoutReference;
import com.google.api.services.slides.v1.model.Page;
import com.google.api.services.slides.v1.model.PageElementProperties;
import com.google.api.services.slides.v1.model.Presentation;
import com.google.api.services.slides.v1.model.Range;
import com.google.api.services.slides.v1.model.ReplaceAllTextRequest;
import com.google.api.services.slides.v1.model.Request;
import com.google.api.services.slides.v1.model.Size;
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
    @Value("${file.mentionf.imageurl}")
    private String IMAGE_NOT_FOUND_USR;
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
			String NameSheet = NAME_SHEET.toLowerCase().equals("automatizacion") ? "Automatización" : "Automatización";
    		String[] arrRangeSH = RANGE_VARIABLES.split(",");
    		SheetRequest reqSh = new SheetRequest();
    		SheetResponse resSh = new SheetResponse();
    		reqSh.setSpreadsheet_id(request.getSpreadsheet_id());
    		reqSh.setRange(NameSheet);
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
    
    
    
    public SlideResponse addImagesSlide (AddImgSlideRequest request) {
    	
    	SlideResponse result = new SlideResponse();
    	try {
    		
    		String slideID = getSlideId(request.getPresentation_id(),request.getSlide_id());
    		
    		//Obtener credenciales para Google Slides
    		JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
			NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
			JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
			Slides service = new Slides.Builder(HTTP_TRANSPORT, JSON_FACTORY, googleAuthorizationConfig
					.getCredentialsServiceAccountSlide(HTTP_TRANSPORT, jsonFactory, credentialsFilePath))
					.setApplicationName(APPLICATIONNAME).build();
		    
			//=> 1cm en la presentación = setScaleX(360000)
			// imagen X ir sumando setScaleX(658,412) cada 1
			// imagen Y ir sumado setScaleY(748,400) cada 5
			Double TranslateX = 463775.0;
			Double TranslateY = 3033800.0;
			Integer elem = 0;
			
		    for (Object itemAut : request.getAuthors()) {
	        	HashMap<String,String> hashitemAut = new HashMap<>();
	        	hashitemAut = (HashMap<String,String>) itemAut;
	        	String nameSH = "";
	        	elem++;
	        	
	        	if (request.getAuthors().size() <= 8) {
		        	if (elem==5) {
		        		TranslateX = 463775.0;
		        		TranslateY += 703380;
					}
				}
	        	if (request.getAuthors().size() >= 10) {
		        	if (elem==6) {
		        		TranslateX = 463775.0;
		        		TranslateY += 703380;
					}
				}

	        	for ( Entry<String, String> itAlcEn : hashitemAut.entrySet()) {
	        		if (itAlcEn.getKey().toString().toLowerCase().equals("nombre")) {
	        			nameSH = itAlcEn.getValue();
					}
	        		if (itAlcEn.getKey().toString().toLowerCase().equals("imagen")) {
	        			try {
		        			String imageUrl = !itAlcEn.getValue().toString().isEmpty() ? itAlcEn.getValue().toString() : IMAGE_NOT_FOUND_USR;
		        			//String slideId = request.getSlide_id();
		        			//String imageUrl ="https://mirkoreisser.de/wp-content/uploads/2019/10/W2036_daim-dynamic-splash-14x21-1xrun-07.jpg";
		        			//String slideId = "g1554aed1034_0_55";
		        			
		        		    List<Request> requests = new ArrayList<>();
		        		    //String imageId = nameSH;
		        		    String imageId = "item_" + (int)(Math.random()*(1-100000+1));  ;
		        		    Dimension emu4M = new Dimension().setMagnitude(4000000.0).setUnit("EMU");
		        		    requests.add(new Request()
		        		        .setCreateImage(new CreateImageRequest()
		        		            .setObjectId(imageId)
		        		            .setUrl(imageUrl)
		        		            .setElementProperties(new PageElementProperties()
		        		                .setPageObjectId(slideID)
		        		                .setSize(new Size()
		        		                    .setHeight(emu4M)
		        		                    .setWidth(emu4M))
		        		                .setTransform(new AffineTransform()
		        		                    .setScaleX(0.1481)
		        		                    .setScaleY(0.1479)
		        		                    .setTranslateX(TranslateX)
		        		                    .setTranslateY(TranslateY)
		        		                    .setUnit("EMU")))));	

		        		      BatchUpdatePresentationRequest body =
		        		          new BatchUpdatePresentationRequest().setRequests(requests);
		        		      BatchUpdatePresentationResponse responseImg = service.presentations().batchUpdate(request.getPresentation_id(), body).execute();
		        		      CreateImageResponse createImageResponse = responseImg.getReplies().get(0).getCreateImage();
		        		      System.out.println("Created image with ID: " + createImageResponse.getObjectId());
		        		      TranslateX += 708412;
						} catch (Exception ex) {
							//#########################___NO EXISTE LA IMÁGEN
							log.error("NO EXISTE LA IMÁGEN A INSERTAR");
							log.error(ex.getMessage());
		        			String imageUrl = IMAGE_NOT_FOUND_USR;
		        			//String slideId = request.getSlide_id();
		        			//String imageUrl ="https://mirkoreisser.de/wp-content/uploads/2019/10/W2036_daim-dynamic-splash-14x21-1xrun-07.jpg";
		        			//String slideId = "g1554aed1034_0_55";
		        			
		        		    List<Request> requests = new ArrayList<>();
		        		    //String imageId = nameSH;
		        		    String imageId = "item_" + (int)(Math.random()*(1-100000+1));  ;
		        		    Dimension emu4M = new Dimension().setMagnitude(4000000.0).setUnit("EMU");
		        		    requests.add(new Request()
		        		        .setCreateImage(new CreateImageRequest()
		        		            .setObjectId(imageId)
		        		            .setUrl(imageUrl)
		        		            .setElementProperties(new PageElementProperties()
		        		                .setPageObjectId(slideID)
		        		                .setSize(new Size()
		        		                    .setHeight(emu4M)
		        		                    .setWidth(emu4M))
		        		                .setTransform(new AffineTransform()
		        		                    .setScaleX(0.1481)
		        		                    .setScaleY(0.1479)
		        		                    .setTranslateX(TranslateX)
		        		                    .setTranslateY(TranslateY)
		        		                    .setUnit("EMU")))));	

		        		      BatchUpdatePresentationRequest body =
		        		          new BatchUpdatePresentationRequest().setRequests(requests);
		        		      BatchUpdatePresentationResponse responseImg = service.presentations().batchUpdate(request.getPresentation_id(), body).execute();
		        		      CreateImageResponse createImageResponse = responseImg.getReplies().get(0).getCreateImage();
		        		      System.out.println("Created image with ID: " + createImageResponse.getObjectId());
		        		      TranslateX += 708412;
						}
					}
				}
			}
			
    		
    		result.setMessage("OK");
    		result.setCode(200);
    		return result;
		} catch (Exception ex) {
    		result.setMessage("ERROR: " + ex.getMessage());
    		result.setCode(500);
    		return result;
		}
    }
    
    public String getSlideId(String presentationID, String numSlide) {
    	String result ="";
    	try {
    		Integer nSlide = Integer.parseInt(numSlide) -1;
    		numSlide = Integer.toString(nSlide - 1);
    		JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    		NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
    		JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
    		Slides service = new Slides.Builder(HTTP_TRANSPORT, JSON_FACTORY, googleAuthorizationConfig
    				.getCredentialsServiceAccountSlide(HTTP_TRANSPORT, jsonFactory, credentialsFilePath))
    				.setApplicationName(APPLICATIONNAME).build();
    		
		    Presentation response = service.presentations().get(presentationID).execute();
		    List<Page> slides = response.getSlides();
		    Page slide = slides.get(nSlide);
		    result = slide.getObjectId();
    		
    		return result;
		} catch (Exception e) {
			return result;
		}
    }
    public void test(SlideRequest request) {
    	try {
    		
    		
    		
    		//Obtener credenciales para Google Slides
    		JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    		NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
    		JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
    		Slides service = new Slides.Builder(HTTP_TRANSPORT, JSON_FACTORY, googleAuthorizationConfig
    				.getCredentialsServiceAccountSlide(HTTP_TRANSPORT, jsonFactory, credentialsFilePath))
    				.setApplicationName(APPLICATIONNAME).build();
    		
		    Presentation response = service.presentations().get(request.getSlide_id()).execute();
		    List<Page> slides = response.getSlides();
		    System.out.println("");
		    /*System.out.printf("The presentation contains %s slides:\n", slides.size());
		    for (int i = 0; i < slides.size(); ++i) {
		      System.out.printf("- Slide #%s contains %s elements.\n", i + 1,
		          slides.get(i).getPageElements().size());
		    }*/
		    
		    
			String imageUrl ="https://mirkoreisser.de/wp-content/uploads/2019/10/W2036_daim-dynamic-splash-14x21-1xrun-07.jpg";
			String slideId = "g1b4514ccc2f_0_6";
		    // Create a new image, using the supplied object ID, with content downloaded from imageUrl.
		    List<Request> requests = new ArrayList<>();
		    String imageId = "item1";
		    Dimension emu4M = new Dimension().setMagnitude(4000000.0).setUnit("EMU");
		    requests.add(new Request()
		        .setCreateImage(new CreateImageRequest()
		            .setObjectId(imageId)
		            .setUrl(imageUrl)
		            .setElementProperties(new PageElementProperties()
		                .setPageObjectId(slideId)
		                .setSize(new Size()
		                    .setHeight(emu4M)
		                    .setWidth(emu4M))
		                .setTransform(new AffineTransform()
    		                .setScaleX(0.5)
    		                .setScaleY(0.5)
		                    .setTranslateX(100000.0)
		                    .setTranslateY(100000.0)
		                    .setUnit("EMU")))));	
			
		      // Execute the request.
		      BatchUpdatePresentationRequest body =
		          new BatchUpdatePresentationRequest().setRequests(requests);
		      BatchUpdatePresentationResponse responseImg = service.presentations().batchUpdate(request.getSlide_id(), body).execute();
		      CreateImageResponse createImageResponse = responseImg.getReplies().get(0).getCreateImage();
		      // Prints the created image id.
		      System.out.println("Created image with ID: " + createImageResponse.getObjectId());
		    
    		
    		
		} catch (Exception e) {
			System.out.println("");
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
