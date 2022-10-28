package GoogleData.sheet.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import GoogleData.sheet.dto.request.*;
import GoogleData.sheet.dto.response.*;
import GoogleData.sheet.model.ItemFiles;
import GoogleData.sheet.model.ObjMelt;
import GoogleData.sheet.service.GoogleService;


@RestController
@RequestMapping(value="/GoogleData")
public class GoogleRest {	

	private static Logger log = Logger.getLogger(GoogleRest.class);
	@Autowired
	GoogleService googleService;
	
	@PostMapping(value="/getData/sheet", 
	consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public SheetResponse getDataSpreadsheet(@RequestBody SheetRequest request) {
		return googleService.getDataSheet(request);
	}
	
	
	@PostMapping(value="/updateData/sheet", 
	consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public UpdateSheetResponse updateDataSpreadsheet(@RequestBody UpdateSheetRequest request) {
		return googleService.updateDataSheet(request);
	}
	
	
	@PostMapping(value="/updateDataMelt/sheet", 
	consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public UpdateSheetMeltResponse updateDataSpreadsheetMelt(@RequestBody UpdateSheetMeltRequest request) {
		return googleService.updateDataSheetMelt(request);
	}
	
	@PostMapping(value="/getElements/sheets", 
	consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public GetListSheetsResponse getElementsListSpreadsheet(@RequestBody SheetRequest request) {
		return googleService.getElementsListSpreadsheet(request);
	}
	
	@GetMapping(value="/get/test", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<String> getDataTest() {
		
		HashMap<String, List<String>> newItem = new HashMap<String, List<String>>();
		List<String> it = new ArrayList<String>();
		List<ObjMelt> ListitemAll = new ArrayList<ObjMelt>();
		ObjMelt itemAll = new ObjMelt();
		UpdateSheetMeltRequest req = new UpdateSheetMeltRequest();
		it.add("2022-09-28 00:00:00,1836");
		it.add("2022-09-28 00:00:00,1836");
		newItem.put("Totales", it);
		newItem.put("Tweets", it);
		itemAll.setValuesFile(newItem);
		itemAll.setSearch("Busqueda2");
		
		ListitemAll.add(itemAll);
		req.setObjectResult(ListitemAll);
		
		List<String> res = new ArrayList<>();
		return res;
   }
	
}
