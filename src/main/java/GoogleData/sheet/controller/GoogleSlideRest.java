package GoogleData.sheet.controller;

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
import GoogleData.sheet.service.GoogleSlideService;

@RestController
@RequestMapping(value="/GoogleData")
@CrossOrigin(origins = "*")
public class GoogleSlideRest {
	private static Logger log = Logger.getLogger(GoogleRest.class);
	@Autowired
	GoogleSlideService googleSlideService;

	@PostMapping(value="/Slides/UpdateData", 
	consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@CrossOrigin(origins = "*")
	public SlideResponse updateDataSlide(@RequestBody SlideRequest request) {
		return googleSlideService.updateDataSlide(request);
	}
	
	
	@PostMapping(value="/Slides/AddImages", 
	consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@CrossOrigin(origins = "*")
	public SlideResponse updateImagesSlide(@RequestBody AddImgSlideRequest request) {
		return googleSlideService.addImagesSlide(request);
	}
	
	@PostMapping(value="/Slides/test", 
	consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@CrossOrigin(origins = "*")
	public void test(@RequestBody SlideRequest request) {
		googleSlideService.test(request);
	}
}
