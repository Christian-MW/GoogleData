package GoogleData.sheet.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import GoogleData.sheet.dto.request.SaveLogWordPressRequest;
import GoogleData.sheet.dto.request.WordPressRequest;

@Component
public interface WordPressService {
	ResponseEntity<?> saveConfiguration(WordPressRequest request);
	ResponseEntity<?> saveLog(SaveLogWordPressRequest request);
}
