package GoogleData.sheet.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import GoogleData.sheet.dto.request.AddDataTikTokRequest;
import GoogleData.sheet.dto.request.MeditionFSV2Request;
import GoogleData.sheet.dto.request.SearchFileV2Request;
import GoogleData.sheet.dto.response.MeditionFSResponse;
import GoogleData.sheet.service.GoogleRestV2Service;

@RestController
@RequestMapping(value="/GoogleRestV2")
public class GoogleRestV2 {
	private final Log log = LogFactory.getLog(getClass());
	@Autowired
	GoogleRestV2Service googleRestV2Service;
	
	
	@PostMapping(value="/sheets/MeditionFileAndSlides", 
	consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public MeditionFSResponse MeditionFileAndSlides(@RequestBody MeditionFSV2Request request) {
		return googleRestV2Service.MeditionFileAndSlides(request);
	}
	
	@PostMapping(value="/sheets/AddDataSearchFile",
	consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> AddDataSearchFile(@RequestBody SearchFileV2Request request){
		return googleRestV2Service.addDataToSearchFile(request);
	}
	
	@PostMapping(value="/tiktok/AddDataFile",
	consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> AddDataSearchFile(@RequestBody AddDataTikTokRequest request){
		return googleRestV2Service.addDataSearchFile(request);
	}
	
	@PostMapping(value="/tiktok/AddDataComents",
	consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> AddDataFileComents(@RequestBody AddDataTikTokRequest request){
		return googleRestV2Service.addDataFileComents(request);
	}
}
