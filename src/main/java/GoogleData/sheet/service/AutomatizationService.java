package GoogleData.sheet.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import GoogleData.sheet.dto.request.*;
import GoogleData.sheet.dto.response.*;

@Component
public interface AutomatizationService {
	ResponseEntity<?> getDaysVacations(AutomatizationRequest request);
}
