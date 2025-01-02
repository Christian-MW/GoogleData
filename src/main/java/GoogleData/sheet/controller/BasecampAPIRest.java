package GoogleData.sheet.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import GoogleData.sheet.dto.request.processBasecamAPIRequest;
import GoogleData.sheet.service.BasecampAPIService;

@RestController
@RequestMapping(value="/GoogleSheet/BasecampAPI")
@CrossOrigin(origins = "*")
public class BasecampAPIRest {
	private final Log log = LogFactory.getLog(getClass());
	@Autowired
	BasecampAPIService basecampAPIService;
	
	@PostMapping(value="/processData", 
	consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@CrossOrigin(origins = "*")
	public ResponseEntity<?> ProcessData(@RequestBody processBasecamAPIRequest request) {
		return basecampAPIService.ProcessData(request);
	}
}
