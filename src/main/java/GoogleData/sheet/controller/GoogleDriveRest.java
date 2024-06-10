package GoogleData.sheet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import GoogleData.sheet.dto.request.SaveFileDriveRequest;
import GoogleData.sheet.service.GoogleDriveService;

import org.apache.log4j.Logger;

@RestController
@RequestMapping(value="/GoogleDrive")
@CrossOrigin(origins = "*")
public class GoogleDriveRest {
	private static Logger log = Logger.getLogger(GoogleDriveRest.class);
	@Autowired
	GoogleDriveService googleDriveService;
	
	@PostMapping(value="/API/fileUpload", 
	consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@CrossOrigin(origins = "*")
	public ResponseEntity<?> getDataSpreadsheet(@RequestBody SaveFileDriveRequest request) {
		return googleDriveService.fileUpload(request);
	}
	
	@PostMapping(value="/API/saveDatafile", 
	consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@CrossOrigin(origins = "*")
	public ResponseEntity<?> getDataFromFile(@RequestBody SaveFileDriveRequest request) {
		return googleDriveService.saveDatafile(request);
	}
}
