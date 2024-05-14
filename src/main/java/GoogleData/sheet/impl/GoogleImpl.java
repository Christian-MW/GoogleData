package GoogleData.sheet.impl;

import org.springframework.stereotype.Service;

import GoogleData.sheet.dto.request.*;
import GoogleData.sheet.dto.response.*;
import GoogleData.sheet.model.*;
import GoogleData.sheet.service.GoogleService;
import GoogleData.sheet.utils.Utilities;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.AddSheetRequest;
import com.google.api.services.sheets.v4.model.AppendValuesResponse;
import com.google.api.services.sheets.v4.model.BatchGetValuesResponse;
import com.google.api.services.sheets.v4.model.BatchUpdateSpreadsheetRequest;
import com.google.api.services.sheets.v4.model.BatchUpdateSpreadsheetResponse;
import com.google.api.services.sheets.v4.model.ClearValuesRequest;
import com.google.api.services.sheets.v4.model.DimensionRange;
import com.google.api.services.sheets.v4.model.FindReplaceRequest;
import com.google.api.services.sheets.v4.model.FindReplaceResponse;
import com.google.api.services.sheets.v4.model.InsertDimensionRequest;
import com.google.api.services.sheets.v4.model.Request;
import com.google.api.services.sheets.v4.model.Sheet;
import com.google.api.services.sheets.v4.model.SheetProperties;
import com.google.api.services.sheets.v4.model.Spreadsheet;
import GoogleData.sheet.config.GoogleAuthorizationConfig;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.json.jackson2.JacksonFactory;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.util.store.MemoryDataStoreFactory;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.common.base.CharMatcher;
import com.google.gson.Gson;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.sheets.v4.model.SpreadsheetProperties;
import com.google.api.services.sheets.v4.model.UpdateSpreadsheetPropertiesRequest;
import com.google.api.services.sheets.v4.model.UpdateValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.googleapis.json.GoogleJsonError;
import com.google.api.client.json.JsonFactory;
import java.io.FileNotFoundException;
import com.google.api.client.util.store.FileDataStoreFactory;


@Service("GoogleImpl")
public class GoogleImpl implements GoogleService {

	private static Logger log = Logger.getLogger(GoogleImpl.class);
    @Value("${api.key}")
    private String API_KEY;
    @Value("${application.name}")
    private String APPLICATIONNAME;
    @Value("${file.columns}")
    private String FILE_COLUMNS;
    @Value("${file.columns.melt}")
    private String FILE_COLUMNS_MELT;
    @Value("${file.columns.melt2}")
    private String FILE_COLUMNS_MELT2;
    @Value("${file.columns.melt.alcance}")
    private String FILE_COLUMNS_MELT_ALCANCE;
    @Value("${terms.melt.files}")
    private String TEMRS_MELT_FILES;
    @Value("${credentials.file.path}")
    private String credentialsFilePath;
    @Value("${values.file.melt}")
    private String headersFileMelt;
    @Value("${file.columns.mentionf}")
    private String COLUMNS_FILE_MENTIONS;
    @Value("${file.columns.mentionf.sh}")
    private String COLUMNS_FILE_MENTIONS_SH;
    @Value("${file.columns.mentionf.au}")
    private String COLUMNS_FILE_MENTIONS_AU;
    @Value("${file.log.extension}")
    private String FILE_LOG_EXTENSION;
    @Value("${file.log.extension.headers}")
    private String HEADERS_FILE_LOG_EXTENSION;
    @Autowired
    private static GoogleAuthorizationConfig googleAuthorizationConfig;
    @Autowired
    Utilities utilities;
    @Autowired
    GoogleSlideImpl googleSlideImpl;
	
    
	public SheetResponse getDataSheet(SheetRequest request) {
		SheetResponse result = new SheetResponse();
		log.info("###########################################################");
		log.info("################_GOOGLE-GETDATASPREADSHEET_################");
		log.info("###########################################################");
		log.info("=>Spreadsheet_id: " + request.getSpreadsheet_id());
		log.info("=>Range: " + request.getRange());
		log.info("=>Columns: " + request.getColumns());
		try {
			String[] countColumns = request.getColumns().split(",");
			Sheets service = getServiceSheet();
	        ValueRange response = service.spreadsheets().values().get(request.getSpreadsheet_id(), request.getRange()).setKey(API_KEY).execute();
	        List<List<Object>> values = response.getValues();
	        //Validar que existan las columnas necesarias para la extensión
	        Integer columns = 0;
	        ArrayList arrCF = new ArrayList<>();
	        String letter = "";
	        String rangeCol = "";
	        for (String h : request.getColumns().trim().split(",")) {
	        	Integer columnFile = -1;
	        	for(Object headers: values.get(0)) {
	        		columnFile++;
	        		if(h.trim().toLowerCase().equals(headers.toString().trim().toLowerCase())) {
	        			arrCF.add(columnFile);
	        			columns++;
	        			break;
	        		}
	        	}
	        }
	        if(columns == countColumns.length) {
		        List<List<Object>> valuesAll = new ArrayList<List<Object>>();
	        	for (List<Object> item : values) {
	        		if(item.size() > 0) {
		        		List<Object> value = new ArrayList<Object>();
		        		for(Object itmArr : arrCF) {
		        			try {
		        				System.out.println(item.get((int)itmArr));
		        				value.add(item.get((int)itmArr));
							} catch (Exception e) {
		        				System.out.println("vacío");
		        				value.add("");
							}

		        			
		        			/*try {
			        			if(!item.get((int)itmArr).toString().isEmpty()) {
			        				System.out.println(item.get((int)itmArr));
			        				value.add(item.get((int)itmArr));
			        			}
			        			else
			        				break;
							} catch (Exception e) {
								//valuesAll.add(value);
								break;
							}*/
		        		}
		        		valuesAll.add(value);
		        		System.out.println("");
	        		}
				}
	        	log.info("====El archivo se obtuvo correctamente");
		        result.setObjectResult(valuesAll);
		        result.setCode(200);
		        result.setMessage("OK");
	        }
	        else {
	        	log.error("################_GOOGLE-getDataSpreadsheet___ERROR");
	        	log.error("====ERROR__ El archivo no contiene los encabezados necesarios");
		        result.setObjectResult(null);
		        result.setCode(409);
		        result.setMessage("El archivo no contiene los encabezados necesarios");
	        }
	        log.info("====================>Archivo obtenido con éxito");
	        return result;
		} catch (Exception e) {
			log.error("################_GOOGLE-getDataSpreadsheet___ERROR ");
			log.error(e.getMessage());
			//Error si la hoja o el spreadshet no existe
			//404 spreadshet
			//400 hoja
			result.setMessage("Error NOT EXIST Spreadshet or Range");
			result.setCode(500);
			return result;
		}
	}
	
	public SheetResponse getDataSheetByFilter(String MajorDimension, String spreadsheet_id, String range) {
		log.info("###########################################################");
		log.info("##############_GOOGLE-GETDATASHEETBYFILTER_################");
		log.info("###########################################################");
		log.info("=>MajorDimension: " + MajorDimension);
		log.info("=>Spreadsheet_id: " + spreadsheet_id);
		log.info("=>Range: " + range);
		SheetResponse result = new SheetResponse();
		ValueRange response = new ValueRange();
		try {
			Sheets service = getServiceSheet();
			if (MajorDimension.equals("COLUMNS")) {
		        Sheets.Spreadsheets.Values.Get req = service.spreadsheets().values().get(spreadsheet_id, range);
		        req.setMajorDimension(MajorDimension);
		        req.setDateTimeRenderOption("FORMATTED_STRING");
		        response = req.execute();	
			}
			if (MajorDimension.equals("RAW")) {
				response = service.spreadsheets().values().get(spreadsheet_id, range).setKey(API_KEY).execute();
			}
	        List<List<Object>> values = response.getValues();
	        result.setObjectResult(values);
	        result.setCode(200);
	        result.setMessage("OK");
			return result;
		} catch (Exception e) {
			log.error("################_GOOGLE-getDataSheetByFilter___ERROR ");
			log.error(e.getMessage());
			result.setMessage("Error NOT EXIST Spreadshet or Range");
			result.setCode(500);
			return result;
		}
	}
	
	public UpdateSheetResponse updateDataSheet(UpdateSheetRequest request) {
		log.info("###########################################################");
		log.info("##############_GOOGLE----UPDATEDATASHEET___################");
		log.info("###########################################################");
		log.info("=>Spreadsheet_id: " + request.getSpreadsheet_id());
		log.info("=>Range: " + request.getRange());
		log.info("=>Columns: " + request.getColumns());
		//log.info("=>ObjectData : " + new Gson().toJson(request.getObjectResult()));
		UpdateSheetResponse result = new UpdateSheetResponse();
		try {
			Sheets service = getServiceSheet();
	        //##########GetData Sheet
	        SheetResponse restGet = getDataSheetByFilter("COLUMNS", request.getSpreadsheet_id(), request.getRange());
	        SheetResponse restGetRaw = getDataSheetByFilter("RAW", request.getSpreadsheet_id(), request.getRange());
	        String Headers_R = request.getRange() + "!" + utilities.numToLetter(restGet.getObjectResult().size() + 1) +  "1";
	        boolean includeGridData = true;
	        String valueInputOption = "RAW";
	        List<List<Object>> values = new ArrayList<List<Object>>();   
	        Boolean addHeaders = false;
	        Boolean stUH = false;
	        Integer stUHi = 0;
	        for (TreeMap<String, Object> iterator : request.getObjectResult()) {
	        	if (iterator.size()>3) {
				//Validar si la función regresa true no hacer más y ciclar esta función de actualización
				//Si refresa false continuar con el proceso normal
				if (stUHi < 1) {
					stUH = findAndUpdateHeaders(service, restGet, restGetRaw, iterator, request.getSpreadsheet_id(),
							request.getRange());
				}
				if (!stUH) {
					stUHi++;
	        	//Map.Entry<String, Object> postValidate = iterator.ceilingEntry("ALCANCE");
	        	//Boolean containsSubStringKey = postValidate != null && postValidate.getKey().startsWith("ALCANCE");
	        	//if (containsSubStringKey) {
	        		System.out.println("PROCESAR ELEMENTO");
		        	try {
		        		//##Add and Search Headers Sheet
						if (!addHeaders /*&& containsSubStringKey*/) {
							List<List<Object>> valuesHeader = new ArrayList<List<Object>>();
							List<Object> valHead = new ArrayList<Object>();
							if (getValuePost(iterator, "ALCANCE", "key").toLowerCase().startsWith("alcance")) 
								valHead.add(getValuePost(iterator, "ALCANCE", "key"));
							if(getValuePost(iterator, "INTERACCIONES", "key").toLowerCase().startsWith("interaccion"))
								valHead.add(getValuePost(iterator, "INTERACCIONES", "key"));
							if(getValuePost(iterator, "REACCIONES", "key").toLowerCase().startsWith("reaccion"))
								valHead.add(getValuePost(iterator, "REACCIONES", "key"));
							if(getValuePost(iterator, "COMENTARIOS", "key").toLowerCase().startsWith("comentario"))
								valHead.add(getValuePost(iterator, "COMENTARIOS", "key"));
							if(getValuePost(iterator, "COMPARTIDOS", "key").toLowerCase().startsWith("compartido"))
								valHead.add(getValuePost(iterator, "COMPARTIDOS", "key"));
							
							/*valHead.add(getValuePost(iterator, "ALCANCE", "key").toLowerCase().startsWith("alcance"));
							valHead.add(getValuePost(iterator, "INTERACCIONES", "key").toLowerCase().startsWith("interaccion"));
							valHead.add(getValuePost(iterator, "REACCIONES", "key").toLowerCase().startsWith("reaccion"));
							valHead.add(getValuePost(iterator, "COMENTARIOS", "key").toLowerCase().startsWith("comentario"));
							valHead.add(getValuePost(iterator, "COMPARTIDOS", "key").toLowerCase().startsWith("compartido"));*/
							valuesHeader.add(valHead);
							addHeaders = addHeadersSheet(valuesHeader, Headers_R, request.getSpreadsheet_id());
						}
			        	//##Procesar
				        //UPDATE_ELEMENT_SHEET_DRIVE
				        Integer linkcol = -1;
				        for (Object itRaw : restGetRaw.getObjectResult().get(0)) {
				        	linkcol++;
				        	if (itRaw.toString().trim().toLowerCase().equals("link"))
								break;
						}
				        Integer numPost = 0;
		        		List<List<Object>> valuesPost = new ArrayList<List<Object>>();
		    	        List<Object> valPost = new ArrayList<Object>();
				        for (Object iteratorDrive : restGet.getObjectResult().get(linkcol)) {
				        	numPost++;
				        	if (iteratorDrive.toString().equals(getValuePost(iterator, "LINK", "value"))) {
								System.out.println("Hay que actualizar este post");
								if (getValuePost(iterator, "ALCANCE", "key").toLowerCase().startsWith("alcance")) 
									valPost.add(getValuePost(iterator, "ALCANCE", "value"));
								if(getValuePost(iterator, "INTERACCIONES", "key").toLowerCase().startsWith("interaccion"))
									valPost.add(getValuePost(iterator, "INTERACCIONES", "value"));
								if(getValuePost(iterator, "REACCIONES", "key").toLowerCase().startsWith("reaccion"))
									valPost.add(getValuePost(iterator, "REACCIONES", "value"));
								if(getValuePost(iterator, "COMENTARIOS", "key").toLowerCase().startsWith("comentario"))
									valPost.add(getValuePost(iterator, "COMENTARIOS", "value"));
								if(getValuePost(iterator, "COMPARTIDOS", "key").toLowerCase().startsWith("compartido"))
									valPost.add(getValuePost(iterator, "COMPARTIDOS", "value"));
								/*valPost.add(getValuePost(iterator, "ALCANCE", "value").toLowerCase().startsWith("alcance"));
								valPost.add(getValuePost(iterator, "INTERACCIONES", "value").toLowerCase().startsWith("interaccion"));
								valPost.add(getValuePost(iterator, "REACCIONES", "value").toLowerCase().startsWith("reaccion"));
								valPost.add(getValuePost(iterator, "COMENTARIOS", "value").toLowerCase().startsWith("comentario"));
								valPost.add(getValuePost(iterator, "COMPARTIDOS", "value").toLowerCase().startsWith("compartido"));*/
								valuesPost.add(valPost);
								String dataPost = request.getRange() + "!" + utilities.numToLetter(restGet.getObjectResult().size() + 1) + numPost.toString();
						        ValueRange bodyPost = new ValueRange()
						                .setValues(valuesPost);
						        AppendValuesResponse res = service.spreadsheets().values().append(request.getSpreadsheet_id(), dataPost, bodyPost)
						                .setValueInputOption(valueInputOption)
						                .execute();
						            System.out.printf("%d cells appended.", res.getUpdates().getUpdatedCells());
						            Thread.sleep(600);
								break;
							}
						}
					} catch (Exception e) {
						System.out.println("ERROR: AL PROCESAR EL ELEMENTO: " + e.getMessage());
						System.out.println(iterator);
					}
				//}
	        	//else {
	        		//System.out.println("NO__PROCESAR ELEMENTO");
	        	//}
				}
	        }
			}
	        log.info("=====El archivo se actualizó correctamente ");
	        result.setCode(200);
	        result.setMessage("El archivo se actualizo correctamente");
			return result;
		} catch (Exception e) {
        	log.error("################_GOOGLE-updateDataSheet___ERROR");
        	log.error("====ERROR__ El archivo no se actualizo");
        	log.error(e.getMessage());
	        result.setCode(500);
	        result.setMessage("ERROR el archivo no se actualizo : " + e.getMessage());
			return result;
		}
	}
	
	public Boolean addHeadersSheet(List<List<Object>> values, String Headers_Range, String spreadsheet_id) {
		log.info("##############_GOOGLE-addHeadersSheet_################");
		String valueInputOption = "RAW";
		try {
			Sheets service = getServiceSheet();
	        ValueRange body = new ValueRange()
	                .setValues(values);
	        AppendValuesResponse res = service.spreadsheets().values().append(spreadsheet_id, Headers_Range, body)
	                .setValueInputOption(valueInputOption)
	                .execute();
	            // Prints the spreadsheet with appended values.
	            log.info("===== Se agregaron los headers correctamente");
			return true;
		} catch (Exception e) {
        	log.error("################_GOOGLE---addHeadersSheet____ERROR");
        	log.error("====ERROR__ Los encabezados no se agregaron correctamente");
        	log.error(e.getMessage());
			return false;
		}
	}
    
	public Sheets getServiceSheet() {
		try {
			//log.info("#########_Current Working Directory is = " +this.getClass().getClassLoader().getResource("").getPath() + credentialsFilePath);
	        NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
	        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
	        Sheets service = new Sheets.Builder(httpTransport, jsonFactory, 
	        		googleAuthorizationConfig.getCredentialsServiceAccount(httpTransport, jsonFactory, credentialsFilePath))
	        		.setApplicationName(APPLICATIONNAME).build();
	        return service;
		} catch (Exception e) {
			System.out.println("=> ERROR__getServiceSheet__: " + e.getMessage());
			return new Sheets(null, null, null);
		}
	}
	
	public String getValuePost(TreeMap<String, Object> iterator, String object, String type){
		String result = "";
		try {
	        Map.Entry<String, Object> element = iterator.ceilingEntry(object);
	        if (type == "key")
	        	result = element.getKey();
	        if(type == "value")
	        	result = element.getValue().toString();
	        return result;
		} catch (Exception e) {
			System.out.println("===> ERROR__ " + e.getMessage());
			return "";
		}
	}
	
	public Boolean findAndUpdateHeaders(Sheets service, SheetResponse DataSheetBDCol, SheetResponse DataSheetBDRaw, 
			TreeMap<String, Object> iterator, String spreadsheet_id, String Range) {
		log.info("##############_GOOGLE-findAndUpdateHeaders_################");
		Boolean status = false;
		try {
			Map.Entry<String, Object> elementPOST = iterator.ceilingEntry("LINK");
			Map.Entry<String, Object> elementALC = iterator.ceilingEntry("ALCANCE_");
			Map.Entry<String, Object> elementINT = iterator.ceilingEntry("INTERACCIONES_");
			Map.Entry<String, Object> elementREA = iterator.ceilingEntry("REACCIONES_");
			Map.Entry<String, Object> elementCOM = iterator.ceilingEntry("COMENTARIOS_");
			Map.Entry<String, Object> elementCOMP = iterator.ceilingEntry("COMPARTIDOS_");
			String[] elHead = {elementALC.getKey(), elementINT.getKey(), elementREA.getKey(), elementCOM.getKey(),elementCOMP.getKey()};
	        Integer linkcol = -1;
	        Integer alcancecol = -1;
	        Integer elecol = -1;
	        Integer numPost = 0;
	        for (Object itRaw : DataSheetBDRaw.getObjectResult().get(0)) {
	        	linkcol++;
	        	if (itRaw.toString().trim().toLowerCase().equals("link"))
					break;
	        }
	        for (Object itRaw : DataSheetBDRaw.getObjectResult().get(0)) {
	        	alcancecol++;
	        	if (itRaw.toString().equals(elementALC.getKey().toString()))
					break;
	        }
	        for (Object itRaw : DataSheetBDRaw.getObjectResult().get(0)) {
	        	elecol++;
	        	//for (int i=0; i < elHead.length; i++) {
		        	if (itRaw.toString().trim().equals(elHead[0].toString().trim())) {
		        		String LettRang = utilities.numToLetter(elecol +1);
		        		List<List<Object>> valuesPost = new ArrayList<List<Object>>();
		    	        List<Object> valPost = new ArrayList<Object>();
		    	        for (Object iteratorDrive : DataSheetBDCol.getObjectResult().get(linkcol)) {
		    	        	numPost++;
		    	        	if (iteratorDrive.toString().equals(getValuePost(iterator, "LINK", "value"))) {
		    	        		System.out.println("Hay que actualizar este post");
		    	        		String RangeItem = Range + "!" + LettRang  + numPost.toString();
				        		List<List<Object>> valuPost = new ArrayList<List<Object>>();
				    	        List<Object> itmPost = new ArrayList<Object>();
				    	        itmPost.add(getValuePost(iterator, "ALCANCE", "value"));
				    	        itmPost.add(getValuePost(iterator, "INTERACCIONES", "value"));
				    	        itmPost.add(getValuePost(iterator, "REACCIONES", "value"));
				    	        itmPost.add(getValuePost(iterator, "COMENTARIOS", "value"));
				    	        itmPost.add(getValuePost(iterator, "COMPARTIDOS", "value"));
				    	        valuPost.add(itmPost);
							        ValueRange bodyPost = new ValueRange()
							                .setValues(valuPost);
		    				    Sheets.Spreadsheets.Values.Update request =
		    				    		service.spreadsheets().values().update(spreadsheet_id, RangeItem, bodyPost);
		    				        request.setValueInputOption("RAW").execute();
		    				        status = true;
		    	        	}
		    	        }
		        	}
	        	//}
			}
			log.info("########Los valores se actualizaron correctamente");
			return status;
		} catch (Exception e) {
        	log.error("################_GOOGLE---findAndUpdateHeaders____ERROR");
        	log.error("====ERROR__ Error al actualizar los encabezados");
        	log.error(e.getMessage());
			return false;
		}
	}

	public UpdateSheetMeltResponse updateDataSheetMelt(UpdateSheetMeltRequest request) {
		log.info("###########################################################");
		log.info("##############_GOOGLE----UPDATE-DATASHEET-MELT___################");
		log.info("###########################################################");
		log.info("=>Spreadsheet_id: " + request.getSpreadsheet_id());
		log.info("=>Range: " + request.getRange());
		log.info("=>Columns: " + request.getColumns());
		//log.info("=>ObjectData : " + new Gson().toJson(request.getObjectResult()));
		UpdateSheetMeltResponse result = new UpdateSheetMeltResponse();
		try {
			String charsToRetain = "0123456789";
			Sheets service = getServiceSheet();
			//Obtenemos las hojas existentes del Spreadsheet enviado
			SheetRequest reqListSheets = new SheetRequest();
			reqListSheets.setSpreadsheet_id(request.getSpreadsheet_id());
			GetListSheetsResponse resListSheets = getElementsListSpreadsheet(reqListSheets);	
			String[] headersValMelt = headersFileMelt.split(",");
			String Search = "";
			String[] fileEndsMelt = TEMRS_MELT_FILES.toString().split(",");
			Boolean isNew = false;
			for (ObjMelt itemOBJ :request.getObjectResult()) {
				Integer RangeCount = 0;
				Boolean existsheet = false;
				Search = itemOBJ.getSearch();
				String hoja = "";
				for (String termFIle : fileEndsMelt) {
					for (String itemSheetBD : resListSheets.getListSheets()) {
						hoja = itemOBJ.getSearch()+termFIle;
						if (hoja.equals(itemSheetBD)) {
							System.out.println("La hoja existe");
							existsheet = true;
							break;
						}
					}
					if (!existsheet) {
						createSheet(request.getSpreadsheet_id(), hoja);
					}
					//Código para hacer todo el desmadre
					RangeCount++;
					SheetResponse restGet = new SheetResponse();
					SheetResponse restGetRaw = new SheetResponse();
			        //##########GetData Sheet
			        restGet = getDataSheetByFilter("COLUMNS", request.getSpreadsheet_id(), hoja.trim());
			        restGetRaw = getDataSheetByFilter("RAW", request.getSpreadsheet_id(), hoja.trim());
			        String valueInputOption = "RAW";
			        Boolean addHeaders = false;
			        Integer numPost = 1;
			        //Validamos si la hoja del sheet contiene datos
			        //Si esta vacía habrá que agregar los encabezados
			        if (restGet.objectResult == null) {
			        	isNew = true;
			        	String[] headers = null;
			        	//Agregar encabezados
			        	if (!addHeaders) {
			        		if (RangeCount==1)
			        			headers = FILE_COLUMNS_MELT.split(",");
			        		if (RangeCount==2)
			        			headers = FILE_COLUMNS_MELT2.split(",");
			        		String Headers_R = hoja.trim() + "!" + "A1";
							List<List<Object>> valuesHeader = new ArrayList<List<Object>>();
							List<Object> valHead = new ArrayList<Object>();
							for (String item : headers) {
								valHead.add(item);
							}
							valuesHeader.add(valHead);
							addHeaders = addHeadersSheet(valuesHeader, Headers_R, request.getSpreadsheet_id());
						}
				        restGet = getDataSheetByFilter("COLUMNS", request.getSpreadsheet_id(), hoja.trim());
				        restGetRaw = getDataSheetByFilter("RAW", request.getSpreadsheet_id(), hoja.trim());
				        addHeaders= true;
			        }
			        Integer AllItems = 0;
			        if (isNew) 
			        	AllItems = restGetRaw.objectResult.size();
			        else
			        	AllItems = restGetRaw.objectResult.size() + 2;
			        List<ObjMelt> elementFile = new ArrayList<ObjMelt>();
			        for (ObjMelt item : request.getObjectResult()) {
						if (item.getSearch().equals(Search)) {
							elementFile.add(item);
							break;
						}
					}
			        //Recorrer el objeto para insertar los datos
			        //for (ObjMelt item : request.getObjectResult()) { //Recorrer todos los elementos en vez de uno solo
			        for (ObjMelt item : elementFile) {
			        	if (RangeCount == 1) {
							List<List<Object>> values = new ArrayList<List<Object>>();
							List<Object> val = new ArrayList<Object>();
							val.add(item.getSearch());
							val.add(Long.parseLong(CharMatcher.anyOf(charsToRetain).retainFrom(item.getUsuarios())));
							val.add(Long.parseLong(CharMatcher.anyOf(charsToRetain).retainFrom(item.getMenciones())));
							val.add(Long.parseLong(CharMatcher.anyOf(charsToRetain).retainFrom(item.getImpresiones())));
							val.add(Long.parseLong(CharMatcher.anyOf(charsToRetain).retainFrom(item.getAlcance())));
							values.add(val);
							String dataPost = hoja.trim() + "!" + utilities.numToLetter(numPost) + AllItems.toString();
					        ValueRange bodyPost = new ValueRange()
					                .setValues(values);
					        AppendValuesResponse res = service.spreadsheets().values().append(request.getSpreadsheet_id(), dataPost, bodyPost)
					                .setValueInputOption(valueInputOption)
					                .execute();
					        AllItems++;
					        Thread.sleep(800);
							//####INSERCIÓN DEL OBJETO ALCANCE#####
							if (item.getDataAlcance().size()>0) {
								String Headers_R = "";
								if (isNew) 
									Headers_R = hoja.trim() + "!" + "G1";
								else {
									AllItems--;
									Headers_R = hoja.trim() + "!" + "G" + AllItems.toString();
								}
								String[] headersAlc = FILE_COLUMNS_MELT_ALCANCE.split(",");
				        		
								List<List<Object>> valuesHeader = new ArrayList<List<Object>>();
								List<Object> valHead = new ArrayList<Object>();
								for (String itemHeA : headersAlc) {
									valHead.add(itemHeA);
								}
								valuesHeader.add(valHead);
								addHeaders = addHeadersSheet(valuesHeader, Headers_R, request.getSpreadsheet_id());
						        restGet = getDataSheetByFilter("COLUMNS", request.getSpreadsheet_id(), hoja.trim());
						        restGetRaw = getDataSheetByFilter("RAW", request.getSpreadsheet_id(), hoja.trim());
						        if (!isNew)
						        	AllItems++;
						        for (Object itemAlc : item.getDataAlcance()) {
						        	HashMap<String,String> hashitemAlc = new HashMap<>();
						        	hashitemAlc = (HashMap<String,String>) itemAlc;
									List<List<Object>> valuesAlc = new ArrayList<List<Object>>();
									List<Object> valAlc = new ArrayList<Object>();
						        	for ( Entry<String, String> itAlcEn : hashitemAlc.entrySet()) {
						        		String s = itAlcEn.getValue().toString();
						        		if (itAlcEn.getKey().equals("Medicion")) 
						        			valAlc.add(s);
						        		else
						        			valAlc.add(Long.parseLong(CharMatcher.anyOf(charsToRetain).retainFrom(s)));
									}
									valuesAlc.add(valAlc);
									String dataPostAlc = hoja.trim() + "!F" + AllItems.toString();
							        ValueRange bodyPostAlc = new ValueRange()
							                .setValues(valuesAlc);
							        AppendValuesResponse resAlc = service.spreadsheets().values().append(request.getSpreadsheet_id(), dataPostAlc, bodyPostAlc)
							                .setValueInputOption(valueInputOption)
							                .execute();
							        AllItems++;
							        Thread.sleep(800);
						        }
						        
							}
			        		//####INSERCIÓN DEL OBJETO ALCANCE#####
						}
			        	
						//Validamos si el objeto del archivo viene lleno e insertamos sus registros
			        	if (RangeCount == 2) {
							//if (item.getValuesFile().size()>0) {
			        		if (headersValMelt.length>0) {
								
								//Obtenemos los objetos de los archivos para almacenar
			        			//for (String key : item.getValuesFile().keySet()) {
								for (String key : headersValMelt) {
									if(key.equals("Total"))
										key = "All";
									//Valores del archivo
									List<String> value = item.getValuesFile().get(key);
									if (value != null) {
										for (String element : value) {
											List<List<Object>> values = new ArrayList<List<Object>>();
											List<Object> val = new ArrayList<Object>();
											val.add(item.getSearch());
											if(key.equals("All"))
												val.add("Total");
											else
												val.add(key);
											if (element != null) {
												//Elementos por renglón
												String[] itemValueArr = element.toString().split(",");
												//val.add(itemValueArr[0].toString().substring(1, itemValueArr[0].toString().length()));
												String itm1 = itemValueArr[0].toString();
												String itm2 = itemValueArr[1].toString();
												val.add(itm1);
												val.add(Long.parseLong(CharMatcher.anyOf(charsToRetain).retainFrom(itm2)));
												values.add(val);
												String dataPost = hoja.trim() + "!" + utilities.numToLetter(numPost) + AllItems.toString();
										        ValueRange bodyPost = new ValueRange()
										                .setValues(values);
										        AppendValuesResponse res = service.spreadsheets().values().append(request.getSpreadsheet_id(), dataPost, bodyPost)
										                .setValueInputOption(valueInputOption)
										                .execute();
										        AllItems++;
										        Thread.sleep(800);
												System.out.println(key.equals(element.toString()));
											}
										}
									}
								}
								//System.out.println("");
							}
							else {
								List<List<Object>> values = new ArrayList<List<Object>>();
								List<Object> val = new ArrayList<Object>();
								val.add(item.getSearch());
								val.add("");
								val.add("");
								val.add("");
								values.add(val);
								AllItems++;
								String dataPost = hoja.trim() + "!" + utilities.numToLetter(numPost) + AllItems.toString();
						        ValueRange bodyPost = new ValueRange()
						                .setValues(values);
						        AppendValuesResponse res = service.spreadsheets().values().append(request.getSpreadsheet_id(), dataPost, bodyPost)
						                .setValueInputOption(valueInputOption)
						                .execute();
						        Thread.sleep(800);
						        System.out.println("");
							}
			        	}
					}		
				}
			}
	        
	        log.info("=====El archivo se actualizó correctamente ");
	        result.setCode(200);
	        result.setMessage("El archivo se actualizo correctamente");
			return result;
		} catch (Exception ex) {
        	log.error("################_GOOGLE-updateDataSheet-MELT___ERROR");
        	log.error("====ERROR__ El archivo no se actualizo");
        	log.error(ex.getMessage());
	        result.setCode(500);
	        result.setMessage("ERROR el archivo no se actualizo : " + ex.getMessage());
			return result;
		}
	}

	public MeditionFSResponse meditionFileSlides(MeditionFSRequest request) {
		MeditionFSResponse result = new MeditionFSResponse();
		try {
			String charsToRetain = "0123456789";
			Sheets service = getServiceSheet();
			//Obtenemos las hojas existentes del Spreadsheet enviado
			SheetRequest reqListSheets = new SheetRequest();
			reqListSheets.setSpreadsheet_id(request.getSpreadsheet_id());
			GetListSheetsResponse resListSheets = getElementsListSpreadsheet(reqListSheets);	
			String[] headersValMelt = headersFileMelt.split(",");
			String[] sheetsFile = COLUMNS_FILE_MENTIONS.toString().split(",");
			Boolean isNew = false;
			String Search = "";
			List<Object> authors = new ArrayList<Object>();
			for (ObjMelt itemOBJ :request.getObjectResult()) {
				Integer RangeCount = 0;
				Boolean existsheet = false;
				Boolean existsheetProd = false;
				Search = itemOBJ.getSearch();
				for (String sheet : sheetsFile) {
					if (sheet.equals("Automatizacion")) {
						sheet = "Automatización";
					}
					//VALIDAMOS SI EXISTE LA HOJA SI NO SE CREA
					for(String sheetProd : resListSheets.getListSheets()) {
						if(sheet.trim().toLowerCase().equals(sheetProd.trim().toLowerCase())) {
							System.out.println("La hoja existe");
							existsheetProd = true;
						}
						if (!existsheetProd) {
							createSheet(request.getSpreadsheet_id(), sheet);
						}
					}
					//createSheet(request.getSpreadsheet_id(), sheet);
					//Código para hacer todo el desmadre
					RangeCount++;
					SheetResponse restGet = new SheetResponse();
					SheetResponse restGetRaw = new SheetResponse();
			        //##########GetData Sheet
			        restGet = getDataSheetByFilter("COLUMNS", request.getSpreadsheet_id(), sheet.trim());
			        restGetRaw = getDataSheetByFilter("RAW", request.getSpreadsheet_id(), sheet.trim());
			        String valueInputOption = "RAW";
			        Boolean addHeaders = false;
			        Integer numPost = 1;
			        //Validamos si la hoja del sheet contiene datos
			        //Si esta vacía habrá que agregar los encabezados					
			        if (restGet.objectResult == null) {
			        	isNew = true;
			        	String[] headers = null;
			        	//Agregar encabezados
			        	if (!addHeaders) {
			        		if (RangeCount==1)
			        			headers = FILE_COLUMNS_MELT.split(",");
			        		if (RangeCount==2)
			        			headers = FILE_COLUMNS_MELT2.split(",");
			        		if (RangeCount==3)
			        			headers = COLUMNS_FILE_MENTIONS_SH.split(",");
			        		if (RangeCount==4)
			        			headers = COLUMNS_FILE_MENTIONS_AU.split(",");
			        		String Headers_R = sheet.trim() + "!" + "A1";
							List<List<Object>> valuesHeader = new ArrayList<List<Object>>();
							List<Object> valHead = new ArrayList<Object>();
							for (String item : headers) {
								valHead.add(item);
							}
							valuesHeader.add(valHead);
							addHeaders = addHeadersSheet(valuesHeader, Headers_R, request.getSpreadsheet_id());
						}
				        restGet = getDataSheetByFilter("COLUMNS", request.getSpreadsheet_id(), sheet.trim());
				        restGetRaw = getDataSheetByFilter("RAW", request.getSpreadsheet_id(), sheet.trim());
				        addHeaders= true;
			        }
			        Integer AllItems = 0;
			        if (isNew) 
			        	AllItems = restGetRaw.objectResult.size();
			        else
			        	AllItems = restGetRaw.objectResult.size() + 2;
			        List<ObjMelt> elementFile = new ArrayList<ObjMelt>();
			        for (ObjMelt item : request.getObjectResult()) {
						if (item.getSearch().equals(Search)) {
							elementFile.add(item);
							break;
						}
					}
			        //Recorrer el objeto para insertar los datos
			        //for (ObjMelt item : request.getObjectResult()) { //Recorrer todos los elementos en vez de uno solo
			        for (ObjMelt item : elementFile) {
			        	//##########__INSERCIÓN DE LA HOJA RESULTADOS__#########
			        	if (RangeCount == 1) {
							List<List<Object>> values = new ArrayList<List<Object>>();
							List<Object> val = new ArrayList<Object>();
							val.add(item.getSearch());
							val.add(Long.parseLong(CharMatcher.anyOf(charsToRetain).retainFrom(item.getUsuarios())));
							val.add(Long.parseLong(CharMatcher.anyOf(charsToRetain).retainFrom(item.getMenciones())));
							val.add(Long.parseLong(CharMatcher.anyOf(charsToRetain).retainFrom(item.getImpresiones())));
							val.add(Long.parseLong(CharMatcher.anyOf(charsToRetain).retainFrom(item.getAlcance())));
							values.add(val);
							//##SE AGREGÓ PARA REEMPLAZAR LOS DATOS EXISTENTES Y QUE NO SE AGREGUEN NUEVOS
							//String dataPost = sheet.trim() + "!" + utilities.numToLetter(numPost) + AllItems.toString();
							String dataPost = sheet.trim() + "!" + utilities.numToLetter(numPost) + 2;
							//##
					        ValueRange bodyPost = new ValueRange()
					                .setValues(values);
    				    Sheets.Spreadsheets.Values.Update res =
    				    		service.spreadsheets().values().update(request.getSpreadsheet_id(), dataPost, bodyPost);
    				        res.setValueInputOption(valueInputOption).execute();
					        AllItems++;
					        Thread.sleep(800);
							//####INSERCIÓN DEL OBJETO ALCANCE#####
							if (item.getDataAlcance().size()>0) {
								String Headers_R = "";
								if (isNew) 
									Headers_R = sheet.trim() + "!" + "G1";
								else {
									AllItems--;
									//Headers_R = sheet.trim() + "!" + "G" + AllItems.toString();
									Headers_R = sheet.trim() + "!" + "G1";
								}
								System.out.println("");
								String[] headersAlc = FILE_COLUMNS_MELT_ALCANCE.split(",");
				        		
								List<List<Object>> valuesHeader = new ArrayList<List<Object>>();
								List<Object> valHead = new ArrayList<Object>();
								for (String itemHeA : headersAlc) {
									valHead.add(itemHeA);
								}
								valuesHeader.add(valHead);
								
								//##SE AGREGÓ PARA REEMPLAZAR LOS DATOS EXISTENTES Y QUE NO SE AGREGUEN NUEVOS
								if (isNew)
									addHeaders = addHeadersSheet(valuesHeader, Headers_R, request.getSpreadsheet_id());
								//##
						        restGet = getDataSheetByFilter("COLUMNS", request.getSpreadsheet_id(), sheet.trim());
						        restGetRaw = getDataSheetByFilter("RAW", request.getSpreadsheet_id(), sheet.trim());
						        if (!isNew)
						        	AllItems++;
						        Integer itmAlc = 1;
						        for (Object itemAlc : item.getDataAlcance()) {
						        	itmAlc++;
						        	HashMap<String,String> hashitemAlc = new HashMap<>();
						        	hashitemAlc = (HashMap<String,String>) itemAlc;
									List<List<Object>> valuesAlc = new ArrayList<List<Object>>();
									List<Object> valAlc = new ArrayList<Object>();
						        	for ( Entry<String, String> itAlcEn : hashitemAlc.entrySet()) {
						        		String s = itAlcEn.getValue().toString();
						        		if (itAlcEn.getKey().equals("Medicion")) 
						        			valAlc.add(s);
						        		else
						        			valAlc.add(Long.parseLong(CharMatcher.anyOf(charsToRetain).retainFrom(s)));
									}
									valuesAlc.add(valAlc);
									//##SE AGREGÓ PARA REEMPLAZAR LOS DATOS EXISTENTES Y QUE NO SE AGREGUEN NUEVOS
									//String dataPostAlc = sheet.trim() + "!F" + AllItems.toString();
									String dataPostAlc = sheet.trim() + "!F" + itmAlc;
									//##
							        ValueRange bodyPostAlc = new ValueRange()
							                .setValues(valuesAlc);
		    				    Sheets.Spreadsheets.Values.Update resAlc =
		    				    		service.spreadsheets().values().update(request.getSpreadsheet_id(), dataPostAlc, bodyPostAlc);
		    				        resAlc.setValueInputOption(valueInputOption).execute();
							        AllItems++;
							        Thread.sleep(800);
						        }
							}
			        		//####INSERCIÓN DEL OBJETO ALCANCE#####
			        	}
			        	//##########__INSERCIÓN DE LA HOJA ACTIVIDAD__#########
						//Validamos si el objeto del archivo viene lleno e insertamos sus registros
			        	if (RangeCount == 2) {
							//if (item.getValuesFile().size()>0) {
			        		if (headersValMelt.length>0) {
								//Obtenemos los objetos de los archivos para almacenar
			        			//for (String key : item.getValuesFile().keySet()) {
								for (String key : headersValMelt) {
									Integer itmAct = 1;
									//Valores del archivo
									List<String> value = item.getValuesFile().get(key);
									if (value != null) {
										for (String element : value) {
											List<List<Object>> values = new ArrayList<List<Object>>();
											List<Object> val = new ArrayList<Object>();
											val.add(item.getSearch());
											val.add(key);
											if (element != null) {
												itmAct ++;
												//Elementos por renglón
												String[] itemValueArr = element.toString().split(",");
												//val.add(itemValueArr[0].toString().substring(1, itemValueArr[0].toString().length()));
												String itm1 = itemValueArr[0].toString();
												String itm2 = itemValueArr[1].toString();
												val.add(itm1);
												val.add(Long.parseLong(CharMatcher.anyOf(charsToRetain).retainFrom(itm2)));
												values.add(val);
												//String dataPost = sheet.trim() + "!" + utilities.numToLetter(numPost) + AllItems.toString();
												String dataPost = sheet.trim() + "!" + utilities.numToLetter(numPost) + itmAct;
										        ValueRange bodyPost = new ValueRange()
										                .setValues(values);
					    				    Sheets.Spreadsheets.Values.Update res =
					    				    		service.spreadsheets().values().update(request.getSpreadsheet_id(), dataPost, bodyPost);
					    				        res.setValueInputOption(valueInputOption).execute();
										        AllItems++;
										        Thread.sleep(800);
											}
										}
									}
								}
							}
							else {
								List<List<Object>> values = new ArrayList<List<Object>>();
								List<Object> val = new ArrayList<Object>();
								val.add(item.getSearch());
								val.add("");
								val.add("");
								val.add("");
								values.add(val);
								AllItems++;
								String dataPost = sheet.trim() + "!" + utilities.numToLetter(numPost) + AllItems.toString();
						        ValueRange bodyPost = new ValueRange()
						                .setValues(values);
						        AppendValuesResponse res = service.spreadsheets().values().append(request.getSpreadsheet_id(), dataPost, bodyPost)
						                .setValueInputOption(valueInputOption)
						                .execute();
						        Thread.sleep(800);
							}
			        	}
			        	//##########__INSERCIÓN DE LA HOJA STAKEHOLDERS__#########
			        	if (RangeCount == 3) {
			        		if (item.getAuthors().size()>0) {
			        			authors = item.getAuthors();
			        			Integer itmSH = 1;
								for (Object itemAut : item.getAuthors()) {
									itmSH++;
						        	HashMap<String,String> hashitemAut = new HashMap<>();
						        	hashitemAut = (HashMap<String,String>) itemAut;
									List<List<Object>> valuesAut = new ArrayList<List<Object>>();
									List<Object> valAut = new ArrayList<Object>();
						        	for ( Entry<String, String> itAlcEn : hashitemAut.entrySet()) {
						        		String s = itAlcEn.getValue().toString();
						        		valAut.add(s);
									}
						        	valuesAut.add(valAut);
									String dataPostAlc = sheet.trim() + "!A" + itmSH;
							        ValueRange bodyPostSH = new ValueRange()
							                .setValues(valuesAut);
		    				    Sheets.Spreadsheets.Values.Update resSH =
		    				    		service.spreadsheets().values().update(request.getSpreadsheet_id(), dataPostAlc, bodyPostSH);
		    				        resSH.setValueInputOption(valueInputOption).execute();
							        AllItems++;
							        Thread.sleep(800);
								}
							}
			        	}
			        	//##########__INSERCIÓN DE LA HOJA AUTOMATIZACIÓN__#########
			        	if (RangeCount == 4) {
			        		System.out.println("");
			        	}
			        }
				}
			}
			//########################################################
			//######Llamar al servicio de actualización de Slides######
			//########################################################
			Thread.sleep(2000);
			SlideRequest objUDS = new SlideRequest();
			objUDS.setSlide_id(request.getSlide_id());
			objUDS.setSpreadsheet_id(request.getSpreadsheet_id());
			SlideResponse resUDS = googleSlideImpl.updateDataSlide(objUDS);
			if (resUDS.getCode() == 500) {
				result.setCode(500);
				result.setMessage("ERROR: " + resUDS.getMessage());
				return result;
			}
			//########################################################
			//####Llamar al servicio de agregar imagenes en Slides####
			//########################################################
			String slideID = String.valueOf(request.getNumberSlide());
			/*String slideID = "";
			SheetResponse restGetRawD = getDataSheetByFilter("RAW", request.getSpreadsheet_id(), "Automatización");
			for (List<Object> itemAut : restGetRawD.getObjectResult()) {
				try {
					if (itemAut.get(1).toString().toLowerCase().equals("{{slide.images.id}}")) {
						slideID = itemAut.get(2).toString();
						break;
					}
				} catch (Exception e) {
					slideID = "0";
					break;
				}
			}*/
			Thread.sleep(1000);
			AddImgSlideRequest objAddIm = new AddImgSlideRequest();
			objAddIm.setPresentation_id(request.getSlide_id());
			objAddIm.setSlide_id(slideID);
			objAddIm.setAuthors(authors);
			SlideResponse resAddIm = googleSlideImpl.addImagesSlide(objAddIm);
			if (resAddIm.getCode() == 500) {
				result.setCode(500);
				result.setMessage("ERROR: " + resAddIm.getMessage());
				return result;
			}
			result.setCode(200);
			result.setMessage("OK");
			return result;
		} catch (Exception e) {
			result.setCode(500);
			result.setMessage("ERROR: " + e.getMessage());
			return result;
		}
	}
	
 	public void createSheet (String spreadsheet_id, String nameSheet) {
		try {
			Sheets service = getServiceSheet();
			AddSheetRequest addSheetRequest = new AddSheetRequest();
			addSheetRequest.setProperties(new SheetProperties());
			addSheetRequest.getProperties().setTitle(nameSheet);
			BatchUpdateSpreadsheetRequest batchUpdateSpreadsheetRequest = new BatchUpdateSpreadsheetRequest();
			Request req = new Request();
			req.setAddSheet(addSheetRequest);
			batchUpdateSpreadsheetRequest.setRequests(new ArrayList<Request>());
			batchUpdateSpreadsheetRequest.getRequests().add(req);
			service.spreadsheets().batchUpdate(spreadsheet_id, batchUpdateSpreadsheetRequest).execute();
		} catch (Exception ex) {
			log.error(ex.getMessage());
		}
	}
	
 	public GetListSheetsResponse getSheetsBySheet(String sheet_id) {
 		try {
			SheetRequest reqListSheets = new SheetRequest();
			reqListSheets.setSpreadsheet_id(sheet_id);
			GetListSheetsResponse resListSheets = getElementsListSpreadsheet(reqListSheets);
			return resListSheets;
		} catch (Exception e) {
			log.error("##############################################################");
			log.error("######################___ERROR_getSheetsBySheet___############");
			log.error(e.getMessage());
			return new GetListSheetsResponse();
		}
 	}
 	
	public GetListSheetsResponse getElementsListSpreadsheet (SheetRequest request){
		log.info("###########################################################");
		log.info("##############_GOOGLE----Get-ElementsListSpreadsheet___################");
		log.info("###########################################################");
		log.info("==> SpreadsheetID: " + request.getSpreadsheet_id());
		
		GetListSheetsResponse result = new GetListSheetsResponse();
		ArrayList<String> listSheets = new ArrayList<>();
		try {
			Sheets service = getServiceSheet();
			Spreadsheet sp = service.spreadsheets().get(request.getSpreadsheet_id()).execute();
			List<Sheet> sheets = sp.getSheets();
			if (sheets.size() > 0) {
				for (Sheet item : sheets) {
					listSheets.add(item.getProperties().getTitle());
				}
				result.setListSheets(listSheets);
				result.setCode(200);
				result.setMessage("OK");
			}
			log.info("=====El listado de hojas se obtuvo correctamente ");
			return result;
		} catch (Exception e) {
        	log.error("################_GOOGLE-getElementsListSpreadsheet___ERROR");
        	log.error("====ERROR__ Error al obtener los datos");
        	log.error(e.getMessage());
			result.setCode(500);
			result.setMessage("Error al obtener los datos " + e.getMessage());
			return result;
		}
	}

	public List<String> getDataByColumn(String sheetID, String nameColumn, String range) {
		log.info("#################################################################");
		log.info("######################___getDataByColumn___######################");
		log.info("#################################################################");
		log.info("==> sheetID: " + sheetID);
		log.info("==> nameColumn: " + nameColumn);
		List<String> list = new ArrayList<String>();
		try {
	        //##########GetData Sheet
			SheetResponse restGet = getDataSheetByFilter("COLUMNS", sheetID, range.toLowerCase().trim());
			SheetResponse restGetRaw = getDataSheetByFilter("RAW", sheetID, range.toLowerCase().trim());
			String headers = restGetRaw.getObjectResult().get(0).toString();
			String[] headArr = headers.split(",");
			Integer column = null;
			
	        //Obteniendo las POSICIÓN de la columna solicitada
			for (Integer i = 0; i < headArr.length; i++) {
				String it = headArr[i].trim().replace("[", "").replace("]", "");
				if (it.toLowerCase().equals(nameColumn.toLowerCase().trim())) 
					column = i;
			}
			if (column==null) {
				list.add("500");
				return list;
			}
			//Obteniendo las DATA de la columna solicitada
			for (  Object listItems : restGet.getObjectResult().get(column)) {
				if (!listItems.toString().toLowerCase().trim().equals(nameColumn.toLowerCase().trim())) 
					list.add(listItems.toString());
			}
			list.add("200");
			return list;
		} catch (Exception ex) {
			log.error(ex.getMessage());
			list.add("500");
			return list;
		}
	}
	
	public LogExtensionResponse logExtension (LogExtensionRequest request) {
		log.info("#############___logExtension___############");
		log.info("=>Request-logExtension: " + new Gson().toJson(request));
		LogExtensionResponse result = new LogExtensionResponse();
		try {
			String range = FILE_LOG_EXTENSION;
			range = range.toLowerCase().trim();
    		Sheets service = utilities.getServiceSheet();
    		Boolean existsheet = false;
			//Obtenemos las hojas existentes del Spreadsheet enviado
			SheetRequest reqListSheets = new SheetRequest();
			reqListSheets.setSpreadsheet_id(request.getSpreadsheet_id());
			GetListSheetsResponse resListSheets = getElementsListSpreadsheet(reqListSheets);
			//Se valida si la hoja existe en el Spreadsheet
			for (String itemSheetBD : resListSheets.getListSheets()) {
				if (range.equals(itemSheetBD)) {
					log.info("La hoja existe");
					existsheet = true;
					break;
				}
			}
			//En caso de que no, se crea
			if (!existsheet) {
				createSheet(request.getSpreadsheet_id(), range);
			}
	        //##########GetData Sheet
			SheetResponse restGet = new SheetResponse();
			SheetResponse restGetRaw = new SheetResponse();
	        restGet = getDataSheetByFilter("COLUMNS", request.getSpreadsheet_id(), range);
	        restGetRaw = getDataSheetByFilter("RAW", request.getSpreadsheet_id(), range);
	        Boolean addHeaders = false;
	        boolean includeGridData = true;
	        String valueInputOption = "RAW";
	        String[] headersCampaign = HEADERS_FILE_LOG_EXTENSION.split(",");
	        //####Validamos si la hoja del sheet contiene datos
	        //####Si esta vacía habrá que agregar los encabezados
	        if (restGet.objectResult == null) {
	        	//Agregar encabezados
	        	if (!addHeaders) {
	        		String Headers_R = range + "!" + "A1";
					List<List<Object>> valuesHeader = new ArrayList<List<Object>>();
					List<Object> valHead = new ArrayList<Object>();
					for (String item : headersCampaign) {
						if (item.equals("Modulo"))
							item = "Módulo";
						valHead.add(item);
					}
					valuesHeader.add(valHead);
					addHeaders = addHeadersSheet(valuesHeader, Headers_R, request.getSpreadsheet_id());
	        	}
		        restGet = getDataSheetByFilter("COLUMNS", request.getSpreadsheet_id(), range);
		        restGetRaw = getDataSheetByFilter("RAW", request.getSpreadsheet_id(), range);
		        addHeaders= true;
	        }
	        List<List<Object>> valuesCamp = new ArrayList<List<Object>>();
			List<Object> valCamp = new ArrayList<Object>();
			valCamp.add(request.getEmail());
			valCamp.add(request.getDate());
			valCamp.add(request.getModule());
			valCamp.add(request.getControl());
			valuesCamp.add(valCamp);
			Integer countCamp = restGetRaw.objectResult.size() + 1;
			String dataPost = range + "!A" + countCamp;
	        ValueRange bodyPost = new ValueRange()
	                .setValues(valuesCamp);
	        AppendValuesResponse res = service.spreadsheets().values().append(request.getSpreadsheet_id(), dataPost, bodyPost)
	                .setValueInputOption(valueInputOption)
	                .execute();

			result.setCode(200);
			result.setMessage("OK");
			return result;
		} catch (Exception e) {
			log.error("###############__ERROR_logExtension__#########");
			log.error(e.getMessage());
			result.setCode(500);
			result.setMessage(e.getMessage());
			return result;
		}
	}
	
	public boolean validateExistSheet(String sheetID, String nameSheet) {
		boolean res = false;
		try {
			SheetRequest reqListSheets = new SheetRequest();
			reqListSheets.setSpreadsheet_id(sheetID);
			GetListSheetsResponse resListSheets = getElementsListSpreadsheet(reqListSheets);
			// Se valida si la hoja existe en el Spreadsheet
			for (String itemSheetBD : resListSheets.getListSheets()) {
				String it = utilities.cleanNameSheet(itemSheetBD.toLowerCase().trim());
				if (nameSheet.equals(it)) {
					res = true;
				}
			}
			return res;
		} catch (Exception ex) {
			log.error("Problemas al encontrar la hoja: " + nameSheet + " en el archivo: " + sheetID);
			log.error(ex.getMessage());
			return false;
		}
	}
	
	public boolean updateAndReplaceData(List<List<Object>> values, String Headers_Range, String spreadsheet_id) {
		log.info("##############_GOOGLE-updateAndReplaceData_################");
		String valueInputOption = "RAW";
		try {
			Sheets service = getServiceSheet();
	        ValueRange bodyPost = new ValueRange()
	                .setValues(values);
	    Sheets.Spreadsheets.Values.Update request =
	    		service.spreadsheets().values().update(spreadsheet_id, Headers_Range, bodyPost);
	        request.setValueInputOption(valueInputOption).execute();
	        return true;
		} catch (Exception ex) {
        	log.error("################_GOOGLE---updateAndReplaceData____ERROR");
        	log.error("====ERROR__ Los registros no se pudieron agregar correctamente");
        	log.error(ex.getMessage());
			return false;
		}
	}
	
	public AIResponse test(AIRequest request) {
		AIResponse result = new AIResponse();
		//################################################
		//########Consumir servicio Búsqueda en Wikipedia
		if (!request.getSearchWikipedia().isEmpty()) {
			return utilities.getInfoWikipedia(request.getSearchWikipedia());
		}
		//################################################
		//########Consumir servicio ChatGPT
		if (!request.getMessageCHATGPT().isEmpty()) {
			//Obtener tweets del archivo
			return utilities.sendTextChatGPT(request.getMessageCHATGPT());
		}
		
		//############################################################
		//###############__PRUEBAS DE SERVICIO GOOGLE-SHEETS__########
		//############################################################
		List<JSONMalta> res = new ArrayList<JSONMalta>();
		Sheets service = getServiceSheet();
		String sheetID="1Iw7dz6Q-mC0vlTbrNY39_KST5gd_CF3_1MK_zOHZivY";
		String sheetName="productos";
		String charsToRetain = "0123456789";
		Integer numProduct = 0;
		try {
	        //##########GetData Sheet
			SheetResponse restGet = getDataSheetByFilter("COLUMNS", sheetID, sheetName);
			SheetResponse restGetRaw = getDataSheetByFilter("RAW", sheetID, sheetName);
			for ( List<Object> product : restGetRaw.objectResult) {
				numProduct++;
				JSONMalta itemProd = new JSONMalta();
				JSONMaltaID idProd = new JSONMaltaID();
				List<Integer> wg = new ArrayList<Integer>();
				List<String> PDA = new ArrayList<String>();
				List<String> PT = new ArrayList<String>();
				List<String> imageP = new ArrayList<String>();
				List<String> images = new ArrayList<String>();
				List<String> featured = new ArrayList<String>();
				JSONMaltaDate stDate =  new JSONMaltaDate();
				JSONMaltaDate enDate = new JSONMaltaDate();
				JSONMaltaMeta metaD = new JSONMaltaMeta();
				if (!product.get(0).toString().startsWith("item_number")) {
					//###-------_id-------###
					if (product.get(2).toString().startsWith("n/d")) 
						idProd.set$oid("");
					else
						idProd.set$oid(product.get(2).toString());
					itemProd.set_id(idProd);
					
					//###-------weight-------###
					if (!product.get(3).toString().isEmpty() && !product.get(3).toString().startsWith("n/d")) 
						wg.add(Integer.parseInt(CharMatcher.anyOf(charsToRetain).retainFrom(product.get(3).toString())));
					if (!product.get(4).toString().isEmpty() && !product.get(4).toString().startsWith("n/d")) 
						wg.add(Integer.parseInt(CharMatcher.anyOf(charsToRetain).retainFrom(product.get(4).toString())));
					itemProd.setWeight(wg);
					
					//###-------possible_drugs_added-------###
					if (!product.get(7).toString().isEmpty() && !product.get(7).toString().startsWith("n/d"))
						PDA.add(product.get(7).toString());
					if (!product.get(8).toString().isEmpty() && !product.get(8).toString().startsWith("n/d"))
						PDA.add(product.get(8).toString());
					if (!product.get(9).toString().isEmpty() && !product.get(9).toString().startsWith("n/d"))
						PDA.add(product.get(9).toString());
					if (!product.get(10).toString().isEmpty() && !product.get(10).toString().startsWith("n/d"))
						PDA.add(product.get(10).toString());
					if (!product.get(11).toString().isEmpty() && !product.get(11).toString().startsWith("n/d"))
						PDA.add(product.get(11).toString());
					if (!product.get(12).toString().isEmpty() && !product.get(12).toString().startsWith("n/d"))
						PDA.add(product.get(12).toString());
					if (!product.get(13).toString().isEmpty() && !product.get(13).toString().startsWith("n/d"))
						PDA.add(product.get(13).toString());
					if (!product.get(14).toString().isEmpty() && !product.get(14).toString().startsWith("n/d"))
						PDA.add(product.get(14).toString());
					if (!product.get(15).toString().isEmpty() && !product.get(15).toString().startsWith("n/d"))
						PDA.add(product.get(15).toString());
					if (!product.get(16).toString().isEmpty() && !product.get(16).toString().startsWith("n/d"))
						PDA.add(product.get(16).toString());
					if (!product.get(17).toString().isEmpty() && !product.get(17).toString().startsWith("n/d"))
						PDA.add(product.get(17).toString());
					if (!product.get(18).toString().isEmpty() && !product.get(18).toString().startsWith("n/d"))
						PDA.add(product.get(18).toString());
					itemProd.setPossible_drugs_added(PDA);
					
					//###-------particle_types-------###
					if (!product.get(6).toString().isEmpty() && !product.get(6).toString().startsWith("n/d"))
						PT.add(product.get(6).toString());
					if (!product.get(19).toString().isEmpty() && !product.get(19).toString().startsWith("n/d"))
						PT.add(product.get(19).toString());
					if (!product.get(20).toString().isEmpty() && !product.get(20).toString().startsWith("n/d"))
						PT.add(product.get(20).toString());
					itemProd.setParticle_types(PT);
					
					//###-------images_particle-------###
					if (!product.get(21).toString().isEmpty() && !product.get(21).toString().startsWith("n/d"))
						imageP.add(product.get(21).toString());
					if (!product.get(22).toString().isEmpty() && !product.get(22).toString().startsWith("n/d"))
						imageP.add(product.get(22).toString());
					itemProd.setImages_particle(imageP);
					
					//###-------images-------###
					if (!product.get(23).toString().isEmpty() && !product.get(23).toString().startsWith("n/d"))
						images.add(product.get(23).toString());
					if (!product.get(24).toString().isEmpty() && !product.get(24).toString().startsWith("n/d"))
						images.add(product.get(24).toString());
					itemProd.setImages(images);
					
					//###-------featured_in-------###
					if (!product.get(25).toString().isEmpty() && !product.get(25).toString().startsWith("n/d"))
						featured.add(product.get(25).toString());
					itemProd.setFeatured_in(featured);
					
					//###-------item_number-------###
					if (!product.get(0).toString().isEmpty() && !product.get(0).toString().startsWith("n/d"))
						itemProd.setItem_number(product.get(0).toString());
					
					//###-------item_name-------###
					if (!product.get(1).toString().isEmpty() && !product.get(1).toString().startsWith("n/d"))
						itemProd.setItem_name(product.get(1).toString());
					
					//###-------item_description-------###
					if (!product.get(26).toString().isEmpty() && !product.get(26).toString().startsWith("n/d"))
						itemProd.setItem_description(product.get(26).toString());
					
					//###-------long_description-------###
					if (!product.get(27).toString().isEmpty() && !product.get(27).toString().startsWith("n/d"))
						itemProd.setLong_description(product.get(27).toString());
					
					//###-------item_shortname-------###
					if (!product.get(28).toString().isEmpty() && !product.get(28).toString().startsWith("n/d"))
						itemProd.setItem_shortname(product.get(28).toString());
					
					//###-------brand_code-------###
					if (!product.get(29).toString().isEmpty() && !product.get(29).toString().startsWith("n/d"))
						itemProd.setBrand_code(product.get(29).toString());
					
					//###-------division_code-------###
					if (!product.get(30).toString().isEmpty() && !product.get(30).toString().startsWith("n/d"))
						itemProd.setDivision_code(product.get(30).toString());
					
					//###-------subdivision-------###
					if (!product.get(31).toString().isEmpty() && !product.get(31).toString().startsWith("n/d"))
						itemProd.setSubdivision(product.get(31).toString());
					
					//###-------price_cathegory-------###
					if (!product.get(32).toString().isEmpty() && !product.get(32).toString().startsWith("n/d"))
						itemProd.setPrice_cathegory(product.get(32).toString());
					
					//###-------sub_brand-------###
					if (!product.get(33).toString().isEmpty() && !product.get(33).toString().startsWith("n/d"))
						itemProd.setSub_brand(product.get(33).toString());

					//###-------cathegory_inventory-------###
					if (!product.get(34).toString().isEmpty() && !product.get(34).toString().startsWith("n/d"))
						itemProd.setCathegory_inventory(product.get(34).toString());
					
					//###-------stage-------###
					if (!product.get(35).toString().isEmpty() && !product.get(35).toString().startsWith("n/d"))
						itemProd.setStage(product.get(35).toString());
					
					//###-------item_status-------###
					if (!product.get(36).toString().isEmpty() && !product.get(36).toString().startsWith("n/d"))
						itemProd.setItem_status(product.get(36).toString());
					
					//###-------sales_product_type_code-------###
					if (!product.get(37).toString().isEmpty() && !product.get(37).toString().startsWith("n/d"))
						itemProd.setSales_product_type_code(product.get(37).toString());
					
					//###-------technology_code-------###
					if (!product.get(38).toString().isEmpty() && !product.get(38).toString().startsWith("n/d"))
						itemProd.setTechnology_code(product.get(38).toString());
					
					//###-------protein-------###
					if (!product.get(39).toString().isEmpty() && !product.get(39).toString().startsWith("n/d"))
						itemProd.setProtein(Integer.parseInt(CharMatcher.anyOf(charsToRetain).retainFrom(product.get(39).toString())));
					
					//###-------fat-------###
					if (!product.get(40).toString().isEmpty() && !product.get(40).toString().startsWith("n/d"))
						itemProd.setFat(Integer.parseInt(CharMatcher.anyOf(charsToRetain).retainFrom(product.get(40).toString())));
					
					//###-------humidity-------###
					if (!product.get(42).toString().isEmpty() && !product.get(42).toString().startsWith("n/d"))
						itemProd.setHumidity(Integer.parseInt(CharMatcher.anyOf(charsToRetain).retainFrom(product.get(42).toString())));
					
					//###-------benefits-------###
					if (!product.get(45).toString().isEmpty() && !product.get(45).toString().startsWith("n/d"))
						itemProd.setBenefits(product.get(45).toString());
					
					//###-------fiber-------###
					if (!product.get(41).toString().isEmpty() && !product.get(41).toString().startsWith("n/d"))
						itemProd.setFiber(Integer.parseInt(CharMatcher.anyOf(charsToRetain).retainFrom(product.get(41).toString())));
					
					//###-------ashes-------###
					if (!product.get(43).toString().isEmpty() && !product.get(43).toString().startsWith("n/d"))
						itemProd.setAshes(Integer.parseInt(CharMatcher.anyOf(charsToRetain).retainFrom(product.get(43).toString())));
					
					//###-------eln-------###
					if (!product.get(46).toString().isEmpty() && !product.get(46).toString().startsWith("n/d"))
						itemProd.setEln(Integer.parseInt(CharMatcher.anyOf(charsToRetain).retainFrom(product.get(46).toString())));
					
					//###-------particle-------###
					if (!product.get(47).toString().isEmpty() && !product.get(47).toString().startsWith("n/d"))
						itemProd.setParticle(product.get(47).toString());
					
					//###-------createdAt-------###
					stDate.$date = "2020-10-13T22:40:07.917+0000";
					itemProd.setCreatedAt(stDate);
					
					//###-------updatedAt-------###
					enDate.$date = "2023-01-27T22:40:07.917+0000";
					itemProd.setUpdatedAt(enDate);
					
					//###-------active-------###
					if (!product.get(53).toString().isEmpty() && !product.get(53).toString().startsWith("n/d"))
						itemProd.setActive(true);
					
					//###-------metadata-------###
					if (!product.get(5).toString().isEmpty() && !product.get(5).toString().startsWith("n/d"))
						metaD.setSader(product.get(5).toString());
					itemProd.setMetadata(metaD);
					
					//##AGREGAR OBJETO AL ARREGLO
					res.add(itemProd);
				}
			}
			return result;
		} catch (Exception e) {
			System.out.println("====> ERROR: ");
			System.out.println(e.getMessage());
			return new AIResponse();
		}
	}
}
