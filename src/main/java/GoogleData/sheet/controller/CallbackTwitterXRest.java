package GoogleData.sheet.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import GoogleData.sheet.impl.GoogleImpl;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value="/TwitterX")
public class CallbackTwitterXRest {
	private static Logger log = Logger.getLogger(CallbackTwitterXRest.class);
	
	@PostMapping(value="/callback")
	@CrossOrigin(origins = "*")
	public ResponseEntity<?> callbackTwitterX(@RequestBody Object request) {
		log.info("##############################################################################");
		log.info("#########################___callbackTwitterX___###############################");
		try {
			log.info("---PROCESADO CORRECTAMENTE");
			log.info(request);
			System.out.println(request);
			return ResponseEntity.ok().build();
		} catch (Exception e) {
			log.error("ERROR DE RESPUESTA");
			System.out.println(request);
			return ResponseEntity.internalServerError().body(null);
		}

	}
}
