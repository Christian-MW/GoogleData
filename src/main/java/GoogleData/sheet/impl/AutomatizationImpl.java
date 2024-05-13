package GoogleData.sheet.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import GoogleData.sheet.config.GoogleAuthorizationConfig;
import GoogleData.sheet.dto.request.AutomatizationRequest;
import GoogleData.sheet.dto.request.SheetRequest;
import GoogleData.sheet.dto.response.AutomatizationResponse;
import GoogleData.sheet.dto.response.GetListSheetsResponse;
import GoogleData.sheet.dto.response.SheetResponse;
import GoogleData.sheet.service.AutomatizationService;
import GoogleData.sheet.utils.Utilities;

@Service("AutomatizationImpl")
public class AutomatizationImpl implements AutomatizationService {
	private static Logger log = Logger.getLogger(AutomatizationService.class);
    @Value("${sheet.automatization}")
    private String SHEET_FILE_AUTOMATIZATION;
    @Autowired
    Utilities utilities;
    @Autowired
    GoogleImpl googleImpl;

	@Override
	public ResponseEntity<?> getDaysVacations(AutomatizationRequest request) {
		log.info("##############___getDaysVacations___##############");
		log.info("==> email: " + request.getEmail());
		AutomatizationResponse result = new AutomatizationResponse();
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			boolean existsheet = false;
			//Obtener hojas del archivo SHEET
			String range = SHEET_FILE_AUTOMATIZATION.toLowerCase().trim();
			SheetRequest reqListSheets = new SheetRequest();
			reqListSheets.setSpreadsheet_id(request.getSpreadsheet_id());
			GetListSheetsResponse resListSheets = googleImpl.getElementsListSpreadsheet(reqListSheets);
			//Se valida si la hoja existe en el Spreadsheet
			for (String itemSheetBD : resListSheets.getListSheets()) {
				if (range.equals(utilities.cleanNameSheet(itemSheetBD.toLowerCase().trim()))) {
					existsheet = true;
					range = itemSheetBD;
					break;
				}
			}
			//En caso de que no, se crea
			if (!existsheet) {
				System.out.println("No existe el la hoja en el archivo sheet");
				return null;
			}
			
	        //##########GetData Sheet
			SheetResponse restGet = new SheetResponse();
			SheetResponse restGetRaw = new SheetResponse();
	        restGet = googleImpl.getDataSheetByFilter("COLUMNS", request.getSpreadsheet_id(), range);
	        restGetRaw = googleImpl.getDataSheetByFilter("RAW", request.getSpreadsheet_id(), range);
	        
	        //Buscar el email para obtener sus días
	        if (restGetRaw.objectResult != null) {
	        	for (Integer i = 0; i < restGetRaw.getObjectResult().size(); i++) {
	        		List<Object> itemRes = restGetRaw.getObjectResult().get(i);
		        	for (Integer k = 0; k < itemRes.size();k++) {
		        		if(itemRes.get(k).equals(request.getEmail())) {
							map.put("code", 200);
							map.put("message", "OK");
							
							if(Integer.valueOf(itemRes.get(k + 1).toString()) == 0){
								map.put("availableDays", "0");
							}
							else{
								int r = Integer.valueOf(itemRes.get(k + 1).toString()) - Integer.valueOf(itemRes.get(k + 2).toString());
								map.put("availableDays", r);
							}
							map.put("days", itemRes.get(k + 1).toString());
							map.put("daysUsed", itemRes.get(k + 2).toString());
							map.put("startDate", itemRes.get(k + 3).toString());
							map.put("endDate", itemRes.get(k + 4).toString());
		        	        
		        	        ResponseEntity<?> res = utilities.getResponseEntity(map);
		        	        return res;
		        		}
		        	}
	        	}
	        }
			map.put("code", 404);
			map.put("message", "NOT FOUND");
			map.put("days", "0");
			map.put("daysUsed", "0");
			map.put("startDate", "0");
			map.put("endDate", "0");
	        ResponseEntity<?> res = utilities.getResponseEntity(map);
	        return res;
		} catch (Exception ex) {
			map.put("code", 500);
			map.put("message", "ERROR");
			ResponseEntity<?> res = utilities.getResponseEntity(map);
			return res;
		}
	}

}
