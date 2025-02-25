package GoogleData.sheet.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import GoogleData.sheet.dto.request.ConfigGoogleRequest;
import GoogleData.sheet.dto.request.GetTweetsRequest;
import GoogleData.sheet.dto.request.UpdateTweetRequest;
import GoogleData.sheet.dto.response.SheetResponse;
import GoogleData.sheet.dto.response.TwitterXItemsRequest;
import GoogleData.sheet.service.TwitterXService;
import GoogleData.sheet.utils.Utilities;

@Service("TwitterXImpl")
public class TwitterXImpl implements TwitterXService{
	private final Log log = LogFactory.getLog(getClass());
	@Autowired
	GoogleImpl googleImpl;
	@Autowired
	Utilities utilities;

	public ResponseEntity<?> saveConfiguration(ConfigGoogleRequest request) {
		log.info("-------------------------------------------------------------------");
		log.info("----------------------GUARDANDO LA CONFIGURACIÓN-------------------");
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			//Buscar en el "state" en el documento sheet
			int pos = 0;
			SheetResponse restGetRaw = googleImpl.getDataSheetByFilter("RAW", request.getSpreadsheet_id(), request.getSpreadsheet_name());
			if (restGetRaw.objectResult != null) {
				for(List<Object> itemFile : restGetRaw.getObjectResult()) {
					pos++;
					if(request.getState().equals(itemFile.get(6).toString())) {
						//Elemento encontrado ahora solo actualizar tokens y fechas
						List<List<Object>> values = new ArrayList<List<Object>>();
						List<Object> val = new ArrayList<Object>();
						val.add(request.getAccess_token());
						val.add(request.getDateGenerateToken());
						val.add(request.getRefresh_token());
						val.add(request.getDateNextGenerateToken());
						values.add(val);
						String range = request.getSpreadsheet_name() + "!C" + pos;
						googleImpl.updateAndReplaceData(values, range, request.getSpreadsheet_id());
						break;
					}
				}
				map.put("code", 200);
				map.put("message", "OK");
				ResponseEntity<?> res = utilities.getResponseEntity(map);
				return res;
			}else {
				map.put("code", 204);
				map.put("message", "NOT EXIST");
				ResponseEntity<?> res = utilities.getResponseEntity(map);
				return res;
			}
		} catch (Exception ex) {
			log.error("##########################---PROBLEMAS AL GUARDAR LA CONFIGURACIÓN---##########################");
			log.error(ex.getMessage());
			map.put("code", 500);
			map.put("message", "ERROR");
			ResponseEntity<?> res = utilities.getResponseEntity(map);
			return res;
		}
	}
	
	public ResponseEntity<?> GetTweets(GetTweetsRequest request) {
		log.info("-------------------------------------------------------------------");
		log.info("-------------------------OBTENIENDO LOS TWEETS---------------------");
		log.info("==> USER: "+request.getUser());
		log.info("==> ACCOUNT: " + request.getAccount());
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			int pos = 0;
			List<TwitterXItemsRequest> items = new ArrayList<TwitterXItemsRequest>();
			SheetResponse restGetRaw = googleImpl.getDataSheetByFilter("RAW", request.getSpreadsheet_id(), request.getUser());
			
			if (restGetRaw.objectResult != null) {
				for(List<Object> itemFile : restGetRaw.getObjectResult()) {
					pos++;
					if(pos >= 1) {
						if(request.getAccount().equals(itemFile.get(0).toString())) {
							TwitterXItemsRequest tweet = new TwitterXItemsRequest();
							tweet.setAccount(itemFile.get(0).toString());
							tweet.setTweet(itemFile.get(1).toString());
							tweet.setHour(itemFile.get(2).toString());
							try {
								tweet.setStatus(itemFile.get(3).toString());
								System.out.println("");
							} catch (Exception ex) {
								tweet.setStatus("");
								System.out.println("");
							}
							try {
								tweet.setUrl_Post(itemFile.get(4).toString());
								System.out.println("");
							} catch (Exception ex) {
								tweet.setUrl_Post("");
								System.out.println("");
							}
							items.add(tweet);
						}
					}
				}
				map.put("code", 200);
				map.put("message", "OK");
				map.put("objectResult", items);
				ResponseEntity<?> res = utilities.getResponseEntity(map);
				return res;
			}
			else {
				map.put("code", 204);
				map.put("message", "NOT EXIST ELEMENTS");
				map.put("objectResult", null);
				ResponseEntity<?> res = utilities.getResponseEntity(map);
				return res;
			}
			
		} catch (Exception ex) {
			log.error("#################---PROBLEMAS AL OBTENER LOS TWEETS DEL USUARIO: " + request.getUser() +", Y DE LA CUENTA: " + request.getAccount());
			log.error(ex.getMessage());
			map.put("code", 500);
			map.put("message", "ERROR");
			map.put("objectResult", null);
			ResponseEntity<?> res = utilities.getResponseEntity(map);
			return res;
		}
	}


	public ResponseEntity<?> UpdatePost(UpdateTweetRequest request) {
		log.info("--------------------------------------------------------------------------------------");
		log.info("---------------___ACTUALIZANDO POST CON STATUS Y URL_(UpdatePost)___------------------");
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			int pos = 0;
			SheetResponse restGetRaw = googleImpl.getDataSheetByFilter("RAW", request.getSpreadsheet_id(), request.getUser());
			if (restGetRaw.objectResult != null) {
				for (List<Object> itemFile : restGetRaw.getObjectResult()) {
					pos++;
					if (request.getAccount().equals(itemFile.get(0).toString())
							&& request.getTweet().equals(itemFile.get(1).toString())) {
						break;
					}
				}
			}
			//El tweet se encuentra en la posición "pos"
			List<List<Object>> values = new ArrayList<List<Object>>();
			List<Object> val = new ArrayList<Object>();
			val.add(request.getStatus());
			val.add(request.getUrlPost());
			values.add(val);
			
			String range = request.getUser() + "!D" + pos;
			googleImpl.updateAndReplaceData(values, range, request.getSpreadsheet_id());
			map.put("code", 200);
			map.put("message", "OK");
			ResponseEntity<?> res = utilities.getResponseEntity(map);
			return res;
			
		} catch (Exception ex) {
			log.error("###################################################################################");
			log.error("#######################_PROBLEMAS AL ACTUALIZAR EL POST__##########################");
			log.error(ex.getMessage());
			map.put("code", 500);
			map.put("message", "ERROR");
			ResponseEntity<?> res = utilities.getResponseEntity(map);
			return res;
		}
	}

	
}
