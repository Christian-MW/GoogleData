package GoogleData.sheet.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import GoogleData.sheet.dto.request.SendMailsListRequest;

@Component
public interface GmailService {
	ResponseEntity<?> sendMailsList(SendMailsListRequest request);
}
