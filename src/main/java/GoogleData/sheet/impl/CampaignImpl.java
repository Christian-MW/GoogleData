package GoogleData.sheet.impl;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.googleapis.json.GoogleJsonError;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.AddConditionalFormatRuleRequest;
import com.google.api.services.sheets.v4.model.AppendValuesResponse;
import com.google.api.services.sheets.v4.model.BatchUpdateSpreadsheetRequest;
import com.google.api.services.sheets.v4.model.BatchUpdateSpreadsheetResponse;
import com.google.api.services.sheets.v4.model.BooleanCondition;
import com.google.api.services.sheets.v4.model.BooleanRule;
import com.google.api.services.sheets.v4.model.CellData;
import com.google.api.services.sheets.v4.model.CellFormat;
import com.google.api.services.sheets.v4.model.Color;
import com.google.api.services.sheets.v4.model.ConditionValue;
import com.google.api.services.sheets.v4.model.ConditionalFormatRule;
import com.google.api.services.sheets.v4.model.GridRange;
import com.google.api.services.sheets.v4.model.RepeatCellRequest;
import com.google.api.services.sheets.v4.model.Request;
import com.google.api.services.sheets.v4.model.RowData;
import com.google.api.services.sheets.v4.model.TextFormat;
import com.google.api.services.sheets.v4.model.UpdateCellsRequest;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.google.common.base.CharMatcher;
import com.google.gson.Gson;

import GoogleData.sheet.config.GoogleAuthorizationConfig;
import GoogleData.sheet.dto.request.*;
import GoogleData.sheet.dto.response.*;
import GoogleData.sheet.model.CampaignRulesModel;
import GoogleData.sheet.model.CampaignStreamModel;
import GoogleData.sheet.model.ObjColModel;
import GoogleData.sheet.service.CampaignService;
import GoogleData.sheet.utils.Utilities;

@Service("CampaignImpl")
public class CampaignImpl implements CampaignService {
	
	private static Logger log = Logger.getLogger(CampaignImpl.class);
    @Value("${file.columns.campaign}")
    private String FILE_COLUMNS_CAMPAIGN;
    @Value("${file.range.campaign}")
    private String RANGE_CAMPAIGN;
    @Value("${file.camp.campaign}")
    private String RANGE_CAMPAIGN_STATUS;
    @Value("${url.file.campaign}")
    private String URL_FILE_CAMPAIGNS;
    @Value("${conf.hours}")
    private String CONF_HOURS_CAMPAIGN;
    @Value("${min.followers.campaign}")
    private String MIN_FOLLOWERS_CAMPAIGN;
    
    @Value("${file.char.update.user}")
    private String CHAR_UPDATE_USER;
    @Value("${file.char.add.user}")
    private String CHAR_ADD_USER;
    @Autowired
    private static GoogleAuthorizationConfig googleAuthorizationConfig;
    @Autowired
    Utilities utilities;
	@Autowired
	GoogleImpl googleImpl;
    
    public AddCampaignResponse addCampaign (AddCampaignRequest request) {
    	AddCampaignResponse result = new AddCampaignResponse();
    	log.info("===> request: " + new Gson().toJson(request));
	
    	try {
    		Sheets service = utilities.getServiceSheet();
    		Boolean existsheet = false;
			//Obtenemos las hojas existentes del Spreadsheet enviado
			SheetRequest reqListSheets = new SheetRequest();
			reqListSheets.setSpreadsheet_id(URL_FILE_CAMPAIGNS);
			GetListSheetsResponse resListSheets = googleImpl.getElementsListSpreadsheet(reqListSheets);
			//Se valida si la hoja existe en el Spreadsheet
			for (String itemSheetBD : resListSheets.getListSheets()) {
				if (request.getRange().equals(itemSheetBD)) {
					log.info("La hoja existe");
					existsheet = true;
					break;
				}
			}
			//En caso de que no, se crea
			if (!existsheet) {
				googleImpl.createSheet(URL_FILE_CAMPAIGNS, request.getRange());
			}
    		
	        //##########GetData Sheet
			SheetResponse restGet = new SheetResponse();
			SheetResponse restGetRaw = new SheetResponse();
	        restGet = googleImpl.getDataSheetByFilter("COLUMNS", URL_FILE_CAMPAIGNS, request.getRange().trim());
	        restGetRaw = googleImpl.getDataSheetByFilter("RAW", URL_FILE_CAMPAIGNS, request.getRange().trim());
	        boolean includeGridData = true;
	        String valueInputOption = "RAW";
	        Boolean addHeaders = false;
	        String[] headersCampaign = FILE_COLUMNS_CAMPAIGN.split(",");
	        
	        //####Validamos si la hoja del sheet contiene datos
	        //####Si esta vacía habrá que agregar los encabezados
	        if (restGet.objectResult == null) {
	        	//Agregar encabezados
	        	if (!addHeaders) {
	        		String Headers_R = request.getRange().trim() + "!" + "A1";
					List<List<Object>> valuesHeader = new ArrayList<List<Object>>();
					List<Object> valHead = new ArrayList<Object>();
					for (String item : headersCampaign) {
						if (item.equals("Campana"))
							item = "Campaña";
						
						valHead.add(item);
					}
					valuesHeader.add(valHead);
					addHeaders = googleImpl.addHeadersSheet(valuesHeader, Headers_R, URL_FILE_CAMPAIGNS);
	        	}
		        restGet = googleImpl.getDataSheetByFilter("COLUMNS", URL_FILE_CAMPAIGNS, request.getRange().trim());
		        restGetRaw = googleImpl.getDataSheetByFilter("RAW", URL_FILE_CAMPAIGNS, request.getRange().trim());
		        addHeaders= true;
	        }
	        //####Agregar objeto de campaña
	        SimpleDateFormat dateFormatLocal = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	        Date date = new Date();
	        
	        Calendar calendar = Calendar.getInstance();
	        calendar.setTime(date);
	        calendar.add(Calendar.HOUR, Integer.parseInt(CONF_HOURS_CAMPAIGN));
	        System.out.println("LOCAL: " + dateFormatLocal.format(calendar.getTime()));
			List<List<Object>> valuesCamp = new ArrayList<List<Object>>();
			List<Object> valCamp = new ArrayList<Object>();
			valCamp.add(request.getCampaign());
			valCamp.add(request.getSearch());
			valCamp.add("https://docs.google.com/spreadsheets/d/" + request.getSpreadsheet_id() + "/edit");
			valCamp.add(request.getDate_start());
			valCamp.add(request.getDate_end());
			valCamp.add("");
			valCamp.add(dateFormatLocal.format(calendar.getTime()));
			valuesCamp.add(valCamp);
	        
			Integer countCamp = restGetRaw.objectResult.size();
			String dataPost = request.range.trim() + "!" + utilities.numToLetter(countCamp) + "1";
	        ValueRange bodyPost = new ValueRange()
	                .setValues(valuesCamp);
	        AppendValuesResponse res = service.spreadsheets().values().append(URL_FILE_CAMPAIGNS, dataPost, bodyPost)
	                .setValueInputOption(valueInputOption)
	                .execute();
	        
	 
	    	//####Guardar o actualizar Campaña en JSON
	    	GetUsersCampaignResponse usersResponse = getInfoUsers(request.getSpreadsheet_id());
	    	if (usersResponse.getCode() == 200) 
	    		request.setUsers(usersResponse.getUsers());
	    	
	    	CampaignStreamModel campModel = setCamResToCampStream(request);
	    	Boolean resSendJSON = utilities.sendItemStream(campModel, "json");
	    	Boolean resAddSearch = utilities.sendItemStream(campModel, "search");
	        //utilities.createFile("", "SearchAndUsers", new Gson().toJson(request));
	    	
	        
	        result.setCode(200);
	        result.setMessage("OK");
    		return result;
		} catch (Exception ex) {
			log.error("###__ERROR AL INSERTAR EL REGISTRO DE LA CAMPAÑA");
			log.error(ex.getMessage());
			
			result.setCode(500);
			result.setMessage("Error: " + ex.getMessage());
			return result;
		}
    }

    public void updateFileCampaign(UpdateFileCampaignRequest request) {
    	log.info("#####__REQUEST-UPDATE-FILE-CAMPAIGN");
    	Gson gson = new Gson();
    	log.info(gson.toJson(request));
    	try {
    		Sheets service = utilities.getServiceSheet();
	        //##########GetData Sheet
			SheetResponse restGet = new SheetResponse();
			SheetResponse restGetRaw = new SheetResponse();
	        restGet = googleImpl.getDataSheetByFilter("COLUMNS", request.getSheet(), RANGE_CAMPAIGN.toLowerCase().trim());
	        restGetRaw = googleImpl.getDataSheetByFilter("RAW", request.getSheet(), RANGE_CAMPAIGN.toLowerCase().trim());
	        
	        Integer column = 0, file = 0;
	        String range = "";
	        String valChar = "";
	        Boolean existUser = false;
	        String valueInputOption = "RAW";
	        List<Integer> columPos = new ArrayList<Integer>();
	        List<ObjColModel> objCols = new ArrayList<ObjColModel>();
			List<List<Object>> values = new ArrayList<List<Object>>();
			List<Object> val = new ArrayList<Object>();
	        if (request.getType().equals("update")) {
	        	log.info("El usuario_####### "+request.getAccount()+" ########_EXISTE en el sheet hay que actualizarlo");
	        	System.out.println("El usuario_####### "+request.getAccount()+" ########_EXISTE en el sheet hay que actualizarlo");
	        	valChar = CHAR_UPDATE_USER;
     	
		        for (Integer i = 0; i < restGet.getObjectResult().size(); i++) {
		        	if (existUser) 
		        		break;
		        	List<Object> itemRes = restGet.getObjectResult().get(i);
		        	column++;
		        	file = 0;
		        	for (Integer k = 0; k < itemRes.size();k++) {
		        		Object itemUs = itemRes.get(k);
		        		file++;
		        		if (itemUs.toString().trim().equals(request.getAccount().trim())) {
		        			log.info("El usuario se encuentra en el archivo");
		        			System.out.println("La cuenta se encuentra en el archivo");
		        			column++;
		        			existUser = true;
		        			break;
						}
		        	}
				}
		        
		        range = RANGE_CAMPAIGN.toLowerCase().trim() + "!" + utilities.numToLetter(column);
		        range += file.toString();
		        log.info("##Rango a actualizar el usuario: " + range);
		        System.out.println("##Rango a actualizar el usuario: " + range);
				val.add(valChar);
				values.add(val);
				
				
	        	/*###########################################################################*/
	        	/*##################OBTENER VALOR DE LA CELDA DEL USUARIO####################*/
	        	/*###########################################################################*/
	        	try {
		        	ValueRange result = service.spreadsheets().values().get(request.getSheet(), range).execute();
		        	System.out.println(result);
		        	HashMap<String,Object> resultA = new ObjectMapper().readValue(result.toString(), HashMap.class);
		        	var valGetCel =  resultA.get("values");
		        	if (valGetCel != null) {
						//La celda de estátus está vacía para este usuario
		        		log.info("===>La celda de estátus está vacía para este usuario");
		        		return;
					}
		        	System.out.println(valGetCel);
				} catch (Exception ex) {
					System.out.println(ex.getMessage());
				}		
				
				
				
		        ValueRange bodyPost = new ValueRange()
		                .setValues(values);
		    Sheets.Spreadsheets.Values.Update res =
		    		service.spreadsheets().values().update(request.getSheet(), range, bodyPost);
		        res.setValueInputOption(valueInputOption).execute();
				
			}
	        
	        else if(request.getType().equals("add")) {
	        	log.info("El usuario_####### "+request.getAccount()+" ########_ES NUEVO en el sheet hay que agregarlo");
	        	System.out.println("El usuario_####### "+request.getAccount()+" ########_ES NUEVO en el sheet hay que agregarlo");
	        	valChar = CHAR_ADD_USER;
	        	
	        	/*###########################################################################*/
	        	/*VALIDAMOS SI EXISTE EL USUARIO NUEVO PARA NO VOLVERLO A AGREGAR AL ARCHIVO*/
	        	/*###########################################################################*/
	        	for (Integer i = 0; i < restGetRaw.getObjectResult().size(); i++) {
		        	if (existUser) 
		        		break;
		        	List<Object> itemRes = restGetRaw.getObjectResult().get(i);
		        	for (Integer k = 0; k < itemRes.size();k++) {
		        		Object itemUs = itemRes.get(k);
		        		file++;
		        		if (itemUs.toString().trim().equals(request.getAccount().trim())) {
		        			log.info("EL USUARIO NUEVO YA EXISTE EN EL ARCHIVO");
		        			System.out.println("EL USUARIO NUEVO YA EXISTE EN EL ARCHIVO");
		        			existUser = true;
							return;
						}
		        	}
	        	}
	        	
	        	for (Integer i = 0; i < restGetRaw.getObjectResult().size(); i++) {
		        	if (existUser) 
		        		break;
		        	List<Object> itemRes = restGetRaw.getObjectResult().get(i);
		        	column++;
		        	file = 0;
		        	for (Integer k = 0; k < itemRes.size();k++) {
		        		Object itemUs = itemRes.get(k);
		        		file++;
		        		if (itemUs.toString().trim().toLowerCase().equals(RANGE_CAMPAIGN.toLowerCase().trim())) {
		        			columPos.add(k);
		        			column++;
		        			existUser = true;
		        			log.info("EL USUARIO NUEVO NO existe en el archivo HAY QUE AGREGARLO!!!");
		        			System.out.println("EL USUARIO NUEVO NO existe en el archivo HAY QUE AGREGARLO!!!");
						}
		        	}
	        	}
	        	for (Integer i = 0; i < columPos.size(); i++) {
		        	/*if (existUser) 
		        		break;*/
		        	List<Object> itemRes = restGet.getObjectResult().get(columPos.get(i));
		        	ObjColModel itemCol = new ObjColModel();
		        	itemCol.setColName("usuarios_" + columPos.get(i).toString());
		        	itemCol.setPosition(columPos.get(i));
		        	itemCol.setValue(itemRes.size());
		        	objCols.add(itemCol);
	        	}
	        	
	        	
	        	List<ObjColModel> sortedUsers = objCols.stream()
	        			  .sorted(Comparator.comparing(ObjColModel::getValue))
	        			  .collect(Collectors.toList());
	        
	        	System.out.println("");
		        range = RANGE_CAMPAIGN.toLowerCase().trim() + "!" + utilities.numToLetter(sortedUsers.get(0).getPosition()+1);
		        range += sortedUsers.get(0).getValue() +1 + ":" + utilities.numToLetter(sortedUsers.get(0).getPosition()+2);
		        range +=  sortedUsers.get(0).getValue() +1;
		        
		        log.info("##Rango a CREAR el usuario: " + range);
		        System.out.println("##Rango a CREAR el usuario: " + range);
		        
				val.add(request.getAccount());
				val.add(valChar);
				values.add(val);
				
		        ValueRange bodyPost = new ValueRange()
		                .setValues(values);
		    Sheets.Spreadsheets.Values.Update res =
		    		service.spreadsheets().values().update(request.getSheet(), range, bodyPost);
		        res.setValueInputOption(valueInputOption).execute();
	        }
			
			
	        /*ValueRange bodyPost = new ValueRange()
	                .setValues(values);
	        AppendValuesResponse res = service.spreadsheets().values().append(request.getSheet(), range, bodyPost)
	                .setValueInputOption(valueInputOption)
	                .execute();*/
	        
	        
	        System.out.println("");
    		
		} catch (Exception ex) {
			log.error("############___PROBLEMAS AL ACTUALIZAR EL ARCHIVO DE CAMPAÑAS______######");
			log.error(ex);
		}
    }
    public void updateStatusCampaign (updateStatusCampaignRequest request) {
    	log.info("#####__REQUEST-UPDATE-STATUS-CAMPAIGN");
    	Gson gson = new Gson();
    	log.info(gson.toJson(request));
    	try {
    		Sheets service = utilities.getServiceSheet();
	        //##########GetData Sheet
			SheetResponse restGet = new SheetResponse();
			SheetResponse restGetRaw = new SheetResponse();
			String rangeCamp = RANGE_CAMPAIGN_STATUS.toLowerCase().trim().equals("campana") ? "campañas" : "campañas";
	        restGet = googleImpl.getDataSheetByFilter("COLUMNS", request.getSheet(), rangeCamp);
	        restGetRaw = googleImpl.getDataSheetByFilter("RAW", request.getSheet(), rangeCamp);
			
	        
	        Integer column = 0, file = 0;
	        String range = "";
	        String valChar = request.getStatus().equals("activar") ? "Activa" : "Inactiva";
	        Boolean existCampaign= false;
	        String valueInputOption = "RAW";
	        for (Integer i = 0; i < restGet.getObjectResult().size(); i++) {
	        	if (existCampaign) 
	        		break;
	        	List<Object> itemRes = restGet.getObjectResult().get(i);
	        	column++;
	        	file = 0;
	        	for (Integer k = 0; k < itemRes.size();k++) {
	        		Object itemUs = itemRes.get(k);
	        		file++;
	        		if (itemUs.toString().trim().equals(request.getTheme().trim())) {
	        			System.out.println("La CAMPAÑA se encuentra en el archivo");
	        			column = column +5;
	        			existCampaign = true;
	        			break;
					}
	        	}
	        }
	        
	        range = rangeCamp + "!" + utilities.numToLetter(column);
	        range += file.toString();
	        
	        
			List<List<Object>> values = new ArrayList<List<Object>>();
			List<Object> val = new ArrayList<Object>();
			val.add(valChar);
			values.add(val);
	        
			
	        ValueRange bodyPost = new ValueRange()
	                .setValues(values);
	    Sheets.Spreadsheets.Values.Update res =
	    		service.spreadsheets().values().update(request.getSheet(), range, bodyPost);
	        res.setValueInputOption(valueInputOption).execute();
	        
	        /*ValueRange bodyPost = new ValueRange()
	                .setValues(values);
	        AppendValuesResponse res = service.spreadsheets().values().append(request.getSheet(), range, bodyPost)
	                .setValueInputOption(valueInputOption)
	                .execute();*/
	        
	        System.out.println("");
	        
		} catch (Exception ex) {
			log.error("############___**PROBLEMAS AL ACTUALIZAR EL STATUS DEL ARCHIVO DE CAMPAÑAS**______######");
			log.error(ex);
		}
    	
    }
    
    public GetUsersCampaignResponse getInfoUsers(String sheetID) {
    	log.info("##############_Get_Info_Users_#############");
    	GetUsersCampaignResponse result = new GetUsersCampaignResponse();
    	try {
    		List<Integer> columPos = new ArrayList<Integer>();
    		String[] headArr;
    		List<String> usersFile = new ArrayList<String>();
    		Sheets service = utilities.getServiceSheet();
			SheetRequest reqListSheets = new SheetRequest();
			reqListSheets.setSpreadsheet_id(sheetID);
			GetListSheetsResponse resListSheets = googleImpl.getElementsListSpreadsheet(reqListSheets);
			//Se valida si la hoja existe en el Spreadsheet
			for (String itemSheetBD : resListSheets.getListSheets()) {
				if (RANGE_CAMPAIGN.toLowerCase().equals(itemSheetBD.toLowerCase())) {
					log.info("La hoja existe");
					result.setCode(200);
					result.setMessage("La hoja existe");
					
			        //##########GetData Sheet
					SheetResponse restGet = new SheetResponse();
					SheetResponse restGetRaw = new SheetResponse();
			        //##########GetData Sheet
			        restGet = googleImpl.getDataSheetByFilter("COLUMNS", sheetID, RANGE_CAMPAIGN.toLowerCase());
			        restGetRaw = googleImpl.getDataSheetByFilter("RAW", sheetID, RANGE_CAMPAIGN.toLowerCase());
			        String headerSt = restGetRaw.getObjectResult().get(0).toString();
			        headArr = headerSt.split(",");
			        //Obteniendo las columnas que son de "usuarios"
					for (Integer i = 0; i < headArr.length; i++) {
						String it = headArr[i].trim().replace("[", "").replace("]", "");
						//System.out.println(it);
						if (it.toLowerCase().equals(RANGE_CAMPAIGN.toLowerCase())) {
							//System.out.println(headArr[i]);
							columPos.add(i);
						}
					}
					
					//Obteniendo los "usuarios" de las filas obtenidas
					for (Integer colItem: columPos) {
						List<Object> valCol = restGet.getObjectResult().get(colItem);
						for (Object itValCol: valCol) {
							if (!itValCol.toString().toLowerCase().equals(RANGE_CAMPAIGN.toLowerCase())) 
								usersFile.add(itValCol.toString());
						}
					}
					
					result.setMessage("Se obtuvieron correctamente los usuarios");
					result.setUsers(usersFile);
					break;
				}
				else {
					result.setCode(404);
					result.setMessage("La hoja NO existe en el archivo");
				}
			}
			return result;
		} catch (Exception ex) {
			result.setCode(500);
			result.setMessage("Error_search_sheet: " + ex.getMessage());
			return result;
		}
    }

    public CampaignStreamModel setCamResToCampStream(AddCampaignRequest req) {
    	log.info("==> Convert CampaignRequest--TO--CampaignStreamModel");
    	CampaignStreamModel result = new CampaignStreamModel();
    	try {
			result.setChannel("");
			result.setSearch(req.getSearch());
			result.setSheet(req.getSpreadsheet_id());
			result.setTheme(req.getCampaign());
			result.setUpdate(req.getUpdate());
			result.setType("campaña");
			result.setUsers(req.getUsers());
			
			//Cargar las reglas básicas para la campaña
			CampaignRulesModel rules = new CampaignRulesModel();
			List<String> language = new ArrayList<String>();
			List<String> location = new ArrayList<String>();
			List<String> typeTweet = new ArrayList<String>();
			//typeTweet.add("original");
			//typeTweet.add("retweeted");
			rules.setEndDate(req.getDate_end());
			language.add("es");
			language.add("en");
			rules.setLanguage(language);
			rules.setLocation(location);
			rules.setMaxFollowers(0);
			rules.setMaxNumReactions(0);
			rules.setMinFollowers(Integer.parseInt(MIN_FOLLOWERS_CAMPAIGN));
			rules.setMinNumReactions(0);
			rules.setStartDate(req.getDate_start());
			rules.setTypeTweet(typeTweet);
			rules.setVerifiedUser("");
			result.setRules(rules);
			
    		
    		return result;
		} catch (Exception ex) {
			log.error("########__PROBLEMAS AL CONVERTIR CAMPAIGN_RESPONSE---TO---CAMPAIGN_STREAM__########");
			log.error(ex);
			return result;
		}
    }
}
