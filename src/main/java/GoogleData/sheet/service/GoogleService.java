package GoogleData.sheet.service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;

import org.springframework.stereotype.Component;

import GoogleData.sheet.dto.request.*;
import GoogleData.sheet.dto.response.*;

@Component
public interface GoogleService {
	SheetResponse getDataSheet(SheetRequest request);
	UpdateSheetResponse updateDataSheet (UpdateSheetRequest request);
	UpdateSheetMeltResponse updateDataSheetMelt (UpdateSheetMeltRequest request);
	GetListSheetsResponse getElementsListSpreadsheet(SheetRequest request);
	MeditionFSResponse meditionFileSlides (MeditionFSRequest request);
	
	void test(SheetRequest request);
}
