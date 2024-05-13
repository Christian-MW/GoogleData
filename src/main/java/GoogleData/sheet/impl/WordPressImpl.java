package GoogleData.sheet.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import GoogleData.sheet.dto.request.WordPressRequest;
import GoogleData.sheet.service.WordPressService;

@Service("WordPressImpl")
public class WordPressImpl implements WordPressService {
	private final Log log = LogFactory.getLog(getClass());
    @Value("${file.headers.wordpress.config}")
    private String HEADERS_WP_CONFIG;

	public ResponseEntity<?> saveConfiguration(WordPressRequest request) {
		log.info("############___saveConfiguration___############");
		try {
			
		} catch (Exception ex) {

		}
		return null;
	}

}
