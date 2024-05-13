package GoogleData.sheet.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import GoogleData.sheet.dto.request.*;
import GoogleData.sheet.dto.response.*;

@Component
public interface GoogleRestV2Service {
	MeditionFSResponse MeditionFileAndSlides(MeditionFSV2Request request);
	ResponseEntity<?> addDataToSearchFile (SearchFileV2Request request);
	ResponseEntity<?> addDataSearchFile (AddDataTikTokRequest request);
	ResponseEntity<?> addDataFileComents (AddDataTikTokRequest request);
}
