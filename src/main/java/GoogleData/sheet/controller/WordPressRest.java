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

import GoogleData.sheet.dto.request.WordPressRequest;
import GoogleData.sheet.service.WordPressService;

@RestController
@RequestMapping(value="/WordPress")
public class WordPressRest {	
	private final Log log = LogFactory.getLog(getClass());
	@Autowired
	WordPressService wordPressService;
	
	@PostMapping(value="/API/SaveConfiguration", 
	consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> saveConfiguration(@RequestBody WordPressRequest request) {
		return wordPressService.saveConfiguration(request);
	}
}
