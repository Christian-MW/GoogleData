package GoogleData.sheet.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.Permission;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.AddSheetRequest;
import com.google.api.services.sheets.v4.model.BatchUpdateSpreadsheetRequest;
import com.google.api.services.sheets.v4.model.BatchUpdateSpreadsheetResponse;
import com.google.api.services.sheets.v4.model.Request;
import com.google.api.services.sheets.v4.model.SheetProperties;
import com.google.api.services.sheets.v4.model.Spreadsheet;
import com.google.api.services.sheets.v4.model.SpreadsheetProperties;

import GoogleData.sheet.dto.request.SaveFileDriveRequest;
import GoogleData.sheet.model.FileTweetsMeltModel;
import GoogleData.sheet.service.GoogleDriveService;
import GoogleData.sheet.utils.Utilities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

@Service("GoogleDriveImpl")
public class GoogleDriveImpl implements GoogleDriveService {
	private static Logger log = Logger.getLogger(GoogleImpl.class);
	
	@Value("${file.headers.tweets.meditionV2}")
	private String HEADERS_FILE_TWEETS;
	@Autowired
	GoogleImpl googleImpl;
    @Autowired
    Utilities utilities;
	
    /*
     * Mediante un archivo se cargará al drive*/
	@Override
	public ResponseEntity<?> fileUpload(SaveFileDriveRequest request) {
		log.info("#########___CARGANDO EL ARCHIVO AL GOOGLE DRIVE___#########");
		try {
			
			return null;
		} catch (Exception ex) {
			log.error(ex.getMessage());
			log.error("###_PROBLEMAS AL SUBIR EL ARCHIVO: ");
			return null;
		}
	}

	
	/*
	 * Se recibe información en formato JSON para la inserción de los tweets en un
	 * archivo GoogleSheet y se genera una URL de archivo*/
	@Override
	public ResponseEntity<?> saveDatafile(SaveFileDriveRequest request) {
		log.info("#######################################################");
		log.info("############__CREANDO EL ARCHIVO DE SHEET__###########");
		log.info("#######################################################");
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			//##_Validamos si existe un archivo con ese id
			boolean existFile = false;
			if (request.getSpreadsheet_id() != null && !request.getSpreadsheet_id().isEmpty()) {
				existFile = googleImpl.validateExistFile(request.getSpreadsheet_id());
			}
			
			String IDspreadsheet = "";
			String messageResult = "";
			Integer codeResult = 0;
			if(!existFile) {
				log.info("#####_El ARCHIVO NO EXISTE hay que crearlo");
				Sheets service = utilities.getServiceSheet();
		        // Crear una nueva hoja de cálculo
		        Spreadsheet spreadsheet = new Spreadsheet()
		                .setProperties(new SpreadsheetProperties()
		                        .setTitle("Archivo de Tweets"));
		        Spreadsheet createdSpreadsheet = service.spreadsheets().create(spreadsheet).execute();
		        String spreadsheetId = createdSpreadsheet.getSpreadsheetId();

		        // Añadir una nueva hoja con un nombre específico
		        SheetProperties sheetProperties = new SheetProperties().setTitle("Tweets");
		        AddSheetRequest addSheetRequest = new AddSheetRequest().setProperties(sheetProperties);
		        Request requestFile = new Request().setAddSheet(addSheetRequest);
		        BatchUpdateSpreadsheetRequest batchUpdateRequest = new BatchUpdateSpreadsheetRequest()
		                .setRequests(Collections.singletonList(requestFile));

		        BatchUpdateSpreadsheetResponse batchUpdateResponse = service.spreadsheets()
		                .batchUpdate(spreadsheetId, batchUpdateRequest)
		                .execute();
		        Thread.sleep(800);
		        IDspreadsheet = spreadsheetId;
		        log.info("Hoja creada con éxito: " + batchUpdateResponse);
		        
		        // Compartir el archivo con correos específicos
		        Drive driveService = utilities.getServiceDrive();
		        String fileId = spreadsheetId;  // Reemplaza con el ID del archivo que deseas compartir
		        /*String emailAddress = "christian.garcia@mwgroup.com.mx";
		        Permission permission = new Permission()
		                .setType("user")
		                .setRole("writer")
		                .setEmailAddress(emailAddress);*/
		        // Crear permiso para "anyone with the link"
		        Permission permission = new Permission()
		                .setType("anyone")
		                .setRole("writer");

		        driveService.permissions().create(fileId, permission)
		                .setFields("id")
		                .execute();
		        messageResult = "UPDATED";
			}
			else {
				log.info("#####_Archivo existente, hay que actualizarlo");
				IDspreadsheet = request.getSpreadsheet_id();
				messageResult = "OK";
				googleImpl.updateSheet(request.getSpreadsheet_id(), "Tweets", "Tweets2");
				Thread.sleep(500);
		        //Eliminando hoja "Tweets" que se crea automáticamente
				googleImpl.createSheet(request.getSpreadsheet_id(), "Tweets");
				Thread.sleep(500);
				googleImpl.deleteSheet(request.getSpreadsheet_id(), "Tweets2");
				Thread.sleep(500);
				
				

			}
			
	        //Llenar la hoja con todos los elementos correspondientes de los tweets
	        //#_Agregando encabezados
	        String[] headers = HEADERS_FILE_TWEETS.split(",");
        	List<List<Object>> valuesHeader = new ArrayList<List<Object>>();
			List<Object> valHead = new ArrayList<Object>();
			for (int i = 0; i < headers.length; i++) {
				valHead.add(headers[i]);
			}
			valuesHeader.add(valHead);
			String RangeHeaders = "Tweets!A1"; 
			boolean addHeaders = googleImpl.addHeadersSheet(valuesHeader, RangeHeaders, IDspreadsheet);
			Thread.sleep(800);
	        
	        //#_Agregando elementos
			List<List<Object>> valuesItems = new ArrayList<List<Object>>();
			String RangeItems = "Tweets!A2"; 
			for ( FileTweetsMeltModel tweet : request.getItems()) {
				List<Object> valItem = new ArrayList<Object>();
				valItem.add(tweet.getDate());
				valItem.add(tweet.getBody());
				valItem.add("@" + tweet.getAuthor());
				valItem.add(tweet.getUrl());
				valItem.add(tweet.getRts());
				valItem.add(tweet.getEngagement());
				valuesItems.add(valItem);
			}
			googleImpl.updateAndReplaceData(valuesItems, RangeItems, IDspreadsheet);
			Thread.sleep(800);
			
	        //Eliminando hoja "Sheet1" que se crea automáticamente
			googleImpl.deleteSheet(IDspreadsheet, "Sheet1");
	        
	        //URL del archivo
	        String urlFile = "https://docs.google.com/spreadsheets/d/" + IDspreadsheet;
	        log.info("##===>El archivo se creo correctamente: " + urlFile);
			map.put("code", 200);
			map.put("message", messageResult);
			map.put("url", urlFile);
			ResponseEntity<?> res = utilities.getResponseEntity(map);
			return res;
			
		} catch (Exception ex) {
			log.error(ex.getMessage());
			log.error("#########__PROBLEMAS AL GENERAR EL ARCHIVO");
			map.put("code", 500);
			map.put("message", "ERROR");
			map.put("url", "");
			ResponseEntity<?> res = utilities.getResponseEntity(map);
			return res;
		}
	}
	
	

}
