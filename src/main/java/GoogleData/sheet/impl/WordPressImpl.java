package GoogleData.sheet.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import GoogleData.sheet.dto.request.WordPressRequest;
import GoogleData.sheet.dto.response.SheetResponse;
import GoogleData.sheet.service.WordPressService;
import GoogleData.sheet.utils.Utilities;

@Service("WordPressImpl")
public class WordPressImpl implements WordPressService {
	private final Log log = LogFactory.getLog(getClass());
	
    @Value("${file.headers.wordpress.config}")
    private String HEADERS_WP_CONFIG;
    @Value("${file.wordpress.name.config}")
    private String NAME_SHEET_WP_CONFIG;
    @Value("${file.wordpress.name.col}")
    private String NAME_COLUMN_SITE;
	@Autowired
	GoogleImpl googleImpl;
    @Autowired
    Utilities utilities;
    

	public ResponseEntity<?> saveConfiguration(WordPressRequest request) {
		log.info("############___saveConfiguration___############");
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			String[] HeadersFile = HEADERS_WP_CONFIG.split(",");
			if (NAME_SHEET_WP_CONFIG.equals("Configuracion sitios"))
				NAME_SHEET_WP_CONFIG = "Configuración sitios";

			boolean existSh = googleImpl.validateExistSheet(request.getSpreadsheet_id(),
					utilities.cleanNameSheet(NAME_SHEET_WP_CONFIG.trim().toLowerCase()));
			// Si la hoja no existe se crea y se agregan encabezados
			if (!existSh) {
				System.out.println("La hoja NO Existe");
				googleImpl.createSheet(request.getSpreadsheet_id(), NAME_SHEET_WP_CONFIG.trim());
				String Headers_R = NAME_SHEET_WP_CONFIG.trim() + "!A1";
				List<List<Object>> valuesHeader = new ArrayList<List<Object>>();
				List<Object> valHead = new ArrayList<Object>();
				for (int i = 0; i < HeadersFile.length; i++) {
					valHead.add(HeadersFile[i]);
				}
				valuesHeader.add(valHead);
				boolean addHeaders = googleImpl.addHeadersSheet(valuesHeader, Headers_R, request.getSpreadsheet_id());
			}

			// Insertando los datos del objeto
			// SheetResponse restGet = googleImpl.getDataSheetByFilter("COLUMNS",
			// request.getSpreadsheet_id(),NAME_SHEET_WP_CONFIG.trim());
			SheetResponse restGetRaw = googleImpl.getDataSheetByFilter("RAW", request.getSpreadsheet_id(),
					NAME_SHEET_WP_CONFIG.trim());
			int countReg = restGetRaw.objectResult.size() + 1;

			log.info("=>Procesando los elementos a insertar de configuración WordPress");
			log.info("Primero se revisa que si existe el sitio en el archivo");
			List<String> listElements = googleImpl.getDataByColumn(request.getSpreadsheet_id(), NAME_COLUMN_SITE,
					NAME_SHEET_WP_CONFIG.trim());
			int posItem = 1;
			boolean exSite = false;
			for (String item : listElements) {
				posItem++;
				if (item.equals(request.getSite())) {
					exSite = true;
					break;
				}
			}
			// Valida si se tiene que actualizar el token de ChatGPT

			List<List<Object>> valuesItems = new ArrayList<List<Object>>();
			List<Object> valItem = new ArrayList<Object>();
			// Si el sitio existe lo actualiza
			String Headers_R = "";
			if (exSite)
				Headers_R = NAME_SHEET_WP_CONFIG.trim() + "!A" + posItem;
			else
				// Si no existe crea el registro
				Headers_R = NAME_SHEET_WP_CONFIG.trim() + "!A" + countReg;

			valItem.add(request.getSite());
			valItem.add(request.getUserWP());
			valItem.add(request.getPassWP());
			valuesItems.add(valItem);
			googleImpl.updateAndReplaceData(valuesItems, Headers_R, request.getSpreadsheet_id());
			Thread.sleep(800);

			// Actualizar solo el token de ChatGPT dependiendo del status
			if (request.isChangeTCGPT()) {
				List<List<Object>> valuesToken = new ArrayList<List<Object>>();
				List<Object> valToken = new ArrayList<Object>();
				valToken.add("Bearer " + request.getTokenChatGPT());
				valuesToken.add(valToken);
				googleImpl.updateAndReplaceData(valuesToken, NAME_SHEET_WP_CONFIG.trim() + "!D2",
						request.getSpreadsheet_id());
				Thread.sleep(800);
			}
			map.put("code", 200);
			map.put("message", "OK");
			ResponseEntity<?> res = utilities.getResponseEntity(map);
			return res;

		} catch (Exception ex) {
			log.error(ex.getMessage());
			map.put("operation", 500);
			map.put("message", "ERROR");
			ResponseEntity<?> res = utilities.getResponseEntity(map);
			return res;
		}
	}

}
