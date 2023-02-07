package GoogleData.sheet.service;

import org.springframework.stereotype.Component;

import GoogleData.sheet.dto.request.*;
import GoogleData.sheet.dto.response.*;

@Component
public interface GoogleSlideService {
	SlideResponse updateDataSlide (SlideRequest request);
	SlideResponse addImagesSlide (AddImgSlideRequest request);
	void test (SlideRequest request);
}
