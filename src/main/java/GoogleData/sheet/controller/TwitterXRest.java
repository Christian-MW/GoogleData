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

import GoogleData.sheet.dto.request.ConfigGoogleRequest;
import GoogleData.sheet.dto.request.GetTweetsRequest;
import GoogleData.sheet.dto.request.UpdateTweetRequest;
import GoogleData.sheet.service.TwitterXService;

@RestController
@CrossOrigin(origins="*")
@RequestMapping(value="/TwitterX")
public class TwitterXRest {
	private final Log log = LogFactory.getLog(getClass());
	@Autowired
	TwitterXService twitterXService;
	
	@PostMapping(value="/API/SaveConfiguration", 
			consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@CrossOrigin(origins = "*")
	public ResponseEntity<?> saveConfiguration(@RequestBody ConfigGoogleRequest request) {
		return twitterXService.saveConfiguration(request);
	}
	
	@PostMapping(value="/API/GetItems",
			consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@CrossOrigin(origins="*")
	public ResponseEntity<?> GetTweets(@RequestBody GetTweetsRequest request){
		return twitterXService.GetTweets(request);
	}
	
	@PostMapping(value="/API/UpdatePost",
			consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@CrossOrigin(origins="*")
	public ResponseEntity<?> UpdatePost(@RequestBody UpdateTweetRequest request){
		return twitterXService.UpdatePost(request);
	}
}
