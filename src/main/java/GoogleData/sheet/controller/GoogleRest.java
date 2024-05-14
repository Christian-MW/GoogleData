package GoogleData.sheet.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
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
@CrossOrigin(origins = "*")
public class GoogleRest {	

	private static Logger log = Logger.getLogger(GoogleRest.class);
	@Autowired
	GoogleService googleService;
	
	@PostMapping(value="/getData/sheet", 
	consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@CrossOrigin(origins = "*")
	public SheetResponse getDataSpreadsheet(@RequestBody SheetRequest request) {
		return googleService.getDataSheet(request);
	}
	
	
	@PostMapping(value="/updateData/sheet", 
	consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@CrossOrigin(origins = "*")
	public UpdateSheetResponse updateDataSpreadsheet(@RequestBody UpdateSheetRequest request) {
		return googleService.updateDataSheet(request);
	}
	
	
	@PostMapping(value="/updateDataMelt/sheet", 
	consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@CrossOrigin(origins = "*")
	public UpdateSheetMeltResponse updateDataSpreadsheetMelt(@RequestBody UpdateSheetMeltRequest request) {
		return googleService.updateDataSheetMelt(request);
	}
	
	@PostMapping(value="/getElements/sheets", 
	consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@CrossOrigin(origins = "*")
	public GetListSheetsResponse getElementsListSpreadsheet(@RequestBody SheetRequest request) {
		return googleService.getElementsListSpreadsheet(request);
	}
	
	@PostMapping(value="/sheets/MeditionFileSlides", 
	consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@CrossOrigin(origins = "*")
	public MeditionFSResponse MeditionFileSlides(@RequestBody MeditionFSRequest request) {
		return googleService.meditionFileSlides(request);
	}	
	
	@PostMapping(value="/sheets/LogExtension", 
	consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@CrossOrigin(origins = "*")
    public LogExtensionResponse logExtension(@RequestBody LogExtensionRequest request) {
		return googleService.logExtension(request);
   }
	
	@PostMapping(value="/sheets/test", 
	consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@CrossOrigin(origins = "*")
    public AIResponse test(@RequestBody AIRequest request) {
		return googleService.test(request);
   }
	
}
