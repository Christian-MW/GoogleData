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

import GoogleData.sheet.dto.request.GetDataAssignamentRequest;
import GoogleData.sheet.dto.request.SaveDataAssignamentRequest;
import GoogleData.sheet.dto.request.UpdatePostMessageRequest;
import GoogleData.sheet.dto.request.VerifyDataBinnacleRequest;
import GoogleData.sheet.dto.request.ViralizationRequest;
import GoogleData.sheet.dto.request.ViralizationUpdatePostRequest;
import GoogleData.sheet.service.ViralizationService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value="/GoogleData/Viralization")
public class ViralizationRest {
	private final Log log = LogFactory.getLog(getClass());
	
	@Autowired
	ViralizationService viralizationRest;
	
	@PostMapping(value="/saveBinnacle", 
	consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@CrossOrigin(origins = "*")
	public ResponseEntity<?> saveConfiguration(@RequestBody ViralizationRequest request) {
		return viralizationRest.saveBinnacle(request);
	}

	@PostMapping(value="/verifyBinnacle", 
	consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@CrossOrigin(origins = "*")
	public ResponseEntity<?> verifyDataBinnacle(@RequestBody VerifyDataBinnacleRequest request) {
		return viralizationRest.verifyBinnacle(request);
	}
	
	@PostMapping(value="/updatePost", 
	consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@CrossOrigin(origins = "*")
	public ResponseEntity<?> updatePost(@RequestBody ViralizationUpdatePostRequest request) {
		return viralizationRest.updatePost(request);
	}
	
	@PostMapping(value="/getdataAssignament", 
	consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@CrossOrigin(origins = "*")
	public ResponseEntity<?> getDataAssignament(@RequestBody GetDataAssignamentRequest request) {
		return viralizationRest.getDataAssignament(request);
	}
	
	@PostMapping(value="/saveDataAssignament", 
	consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@CrossOrigin(origins = "*")
	public ResponseEntity<?> saveDataAssignament(@RequestBody SaveDataAssignamentRequest request) {
		return viralizationRest.saveDataAssignament(request);
	}
	
	@PostMapping(value="/updateDataAssignament", 
	consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@CrossOrigin(origins = "*")
	public ResponseEntity<?> updateDataAssignament(@RequestBody SaveDataAssignamentRequest request) {
		return viralizationRest.updateDataAssignament(request);
	}
	
	@PostMapping(value="/updatePostMessage", 
	consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@CrossOrigin(origins = "*")
	public ResponseEntity<?> updatePostMessage(@RequestBody UpdatePostMessageRequest request) {
		return viralizationRest.updatePostMessage(request);
	}
}
