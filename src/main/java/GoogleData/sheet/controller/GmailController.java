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

import GoogleData.sheet.dto.request.SendMailsListRequest;
import GoogleData.sheet.service.GmailService;

@RestController
@RequestMapping(value="/GoogleRest/Gmail")
public class GmailController {
	private final Log log = LogFactory.getLog(getClass());
	@Autowired
	GmailService gmailService;
	
	@PostMapping(value="/SendMails/List", 
	consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> MeditionFileAndSlides(@RequestBody SendMailsListRequest request) {
		return gmailService.sendMailsList(request);
	}
}
