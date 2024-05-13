package GoogleData.sheet.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import GoogleData.sheet.dto.request.AutomatizationRequest;
import GoogleData.sheet.dto.response.AutomatizationResponse;
import GoogleData.sheet.service.AutomatizationService;

@RestController
@RequestMapping(value="/Automatization")
public class AutomatizationRest {
	public static Logger log = Logger.getLogger(AutomatizationRest.class);
	@Autowired
	AutomatizationService automatizationService;
	
	
	@PostMapping(value="/holidays/getdays", 
	consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getDaysVacations(@RequestBody AutomatizationRequest request) {
		return automatizationService.getDaysVacations(request);
   }
}
