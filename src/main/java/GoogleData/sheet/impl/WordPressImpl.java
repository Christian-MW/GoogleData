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
			char lc = request.getSite().charAt(request.getSite().length() - 1);
			if (lc == '/') {
				request.setSite(request.getSite().substring(0, request.getSite().length() -1 ));
			}
			
			
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
			
			//Buscando el email del usuario para saber si insertar o actualizar el registro
			SheetResponse restGetRaw = googleImpl.getDataSheetByFilter("RAW", request.getSpreadsheet_id(),
					NAME_SHEET_WP_CONFIG.trim());
			//Buscando las posiciones de las columnas
			List<Object> headersFile = restGetRaw.objectResult.get(0);
			String letterUserWP = utilities.numToLetter(headersFile.indexOf("Usuario WordPress") + 1);
			String letterTokenGPT = utilities.numToLetter(headersFile.indexOf("Token ChatGPT") + 1);

			List<String> listElements = googleImpl.getDataByColumn(request.getSpreadsheet_id(), NAME_COLUMN_SITE,
					NAME_SHEET_WP_CONFIG.trim());
			
			String Headers_R = "";
			int countReg = restGetRaw.objectResult.size() + 1;
			ArrayList<Integer> positions = new ArrayList<>();
	        for (int i = 0; i < listElements.size(); i++) {
	            if (listElements.get(i).equals(request.getEmail())) {
	                positions.add(i);
	            }
	        }
	        
			List<List<Object>> valuesItems = new ArrayList<List<Object>>();
			List<Object> valItem = new ArrayList<Object>();
			Boolean existReg = false;
			//Sí existe el correo se actualiza en caso de no existir se inserta el registro
			if(positions.size() > 0) {
				//Existe por lo menos un email
				for (int i = 0; i < positions.size(); i++) {
					System.out.println(positions.get(i));
					List<Object> register = restGetRaw.objectResult.get(positions.get(i) + 1);
					System.out.println(register.get(1).toString());
					System.out.println(request.getSite());
					
					char lcb = register.get(1).toString().charAt(register.get(1).toString().length() - 1);
					String reg = "";
					if (lcb == '/') 
						reg = register.get(1).toString().substring(0, register.get(1).toString().length() - 1);
					else 
						reg = register.get(1).toString();
					
					if(reg.equals(request.getSite())) {
						//El correo coincide con el sitio
						int pos = positions.get(i) + 2;
						Headers_R = NAME_SHEET_WP_CONFIG.trim() + "!" + letterUserWP + pos;
						valItem.add(request.getUserWP());
						valItem.add(request.getPassWP());
						existReg = true;
						break;
					}
					else {
						existReg = false;
					}
				}
			}
			else {
				Headers_R = NAME_SHEET_WP_CONFIG.trim() + "!A" + countReg;
				valItem.add(request.getEmail());
				valItem.add(request.getSite());
				valItem.add(request.getUserWP());
				valItem.add(request.getPassWP());
				existReg = true;
			}
			if(!existReg) {
				Headers_R = NAME_SHEET_WP_CONFIG.trim() + "!A" + countReg;
				valItem.add(request.getEmail());
				valItem.add(request.getSite());
				valItem.add(request.getUserWP());
				valItem.add(request.getPassWP());
			}

			valuesItems.add(valItem);
			googleImpl.updateAndReplaceData(valuesItems, Headers_R, request.getSpreadsheet_id());
			Thread.sleep(800);
			
			// Actualizar solo el token de ChatGPT dependiendo del status
			if (request.isChangeTCGPT()) {
				List<List<Object>> valuesToken = new ArrayList<List<Object>>();
				List<Object> valToken = new ArrayList<Object>();
				valToken.add("Bearer " + request.getTokenChatGPT());
				valuesToken.add(valToken);
				googleImpl.updateAndReplaceData(valuesToken, NAME_SHEET_WP_CONFIG.trim() + "!"+letterTokenGPT+"2",
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
