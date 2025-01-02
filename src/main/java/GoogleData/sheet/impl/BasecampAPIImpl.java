package GoogleData.sheet.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import GoogleData.sheet.dto.request.processBasecamAPIRequest;
import GoogleData.sheet.model.ProyectModel;
import GoogleData.sheet.service.BasecampAPIService;
import GoogleData.sheet.utils.Utilities;

@Service("BasecampAPIImpl")
public class BasecampAPIImpl implements BasecampAPIService {
	private final Log log = LogFactory.getLog(getClass());
	@Value("${file.namesheet.basecamp}")
	private String NAME_SHEET_BASECAMP_API;
	
    @Autowired
    Utilities utilities;
	@Autowired
	GoogleImpl googleImpl;
    @Autowired
    GoogleSlideImpl googleSlideImpl;
    
    
	@Override
	public ResponseEntity<?> ProcessData(processBasecamAPIRequest request) {
		log.info("####################__PROCESAR ARCHIVO BASECAMP__######################");
		ResponseEntity<?> res = ResponseEntity.ok().build();
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			String nameFile = NAME_SHEET_BASECAMP_API + "_" + utilities.getDateString();
			List<List<Object>> listItems = new ArrayList<List<Object>>();
			//Revisión de existencia de hoja
			boolean existSheet = googleImpl.validateExistSheet(request.getSpreadsheet_id(), nameFile.toLowerCase());
			if(!existSheet) {
				//Crear sheet
				googleImpl.createSheet(request.getSpreadsheet_id(),nameFile);
				//Insertar encabezados
				String[] Headers = {"Proyecto", "Descripción", "Bloque", "Progreso", "Nombre tarjeta", "Creador", "Asignados", 
						"Estatus", "Fecha de creación", "URL Tarjeta"};
	        	List<List<Object>> valuesHeader = new ArrayList<List<Object>>();
				List<Object> valHead = new ArrayList<Object>();
				for (int i = 0; i < Headers.length; i++) {
					valHead.add(Headers[i]);
				}
				valuesHeader.add(valHead);
				String headersRange = nameFile + "!A1";
				boolean addHeaders = googleImpl.addHeadersSheet(valuesHeader, headersRange, request.getSpreadsheet_id());
				
				//####->Obtener la información a procesar (por ahora por JSON, después consumir función)
				List<ProyectModel> dataResponse = utilities.getDataFile();
				//Procesar elementos
				//Obtener datos de proyecto
				String nameProyect = "";
				String descProyect = "";
				String nameToDo = "";
				String ratioToDo = "";
				
				for (int i = 0; i < dataResponse.size(); i++) {
				//for (int i = 0; i < 5; i++) {
					nameProyect = dataResponse.get(i).getName();
					descProyect = dataResponse.get(i).getDescription();
					//item.add(dataResponse.get(i).getName());
					//item.add(dataResponse.get(i).getDescription());
					//Obtener datos de ToDo
					for (int j = 0; j < dataResponse.get(i).getTodoSets().size(); j++) {
						nameToDo = dataResponse.get(i).getTodoSets().get(j).getName();
						ratioToDo = dataResponse.get(i).getTodoSets().get(j).getCompleted_ratio();
						//item.add(dataResponse.get(i).getTodoSets().get(j).getName());
						//item.add(dataResponse.get(i).getTodoSets().get(j).getCompleted_ratio());
						//Obtener datos de tarjeta
						if(dataResponse.get(i).getTodoSets().get(j).getList_cards() != null && dataResponse.get(i).getTodoSets().get(j).getList_cards().size() > 0) {
							for (int x = 0; x < dataResponse.get(i).getTodoSets().get(j).getList_cards().size(); x++) {
								List<Object> item = new ArrayList<Object>();
								item.add(nameProyect);
								item.add(descProyect);
								item.add(nameToDo);
								item.add(ratioToDo);
								item.add(dataResponse.get(i).getTodoSets().get(j).getList_cards().get(x).getName());
								item.add(dataResponse.get(i).getTodoSets().get(j).getList_cards().get(x).getUserCreator().getName());
								//Obtener datos de Asignados en tarjeta
								StringBuilder UA = new StringBuilder();
								if(dataResponse.get(i).getTodoSets().get(j).getList_cards().get(x).getUserAssignees() != null && dataResponse.get(i).getTodoSets().get(j).getList_cards().get(x).getUserAssignees().size() > 0) {
									for (int k = 0; k < dataResponse.get(i).getTodoSets().get(j).getList_cards().get(x).getUserAssignees().size(); k++) {
										UA.append(dataResponse.get(i).getTodoSets().get(j).getList_cards().get(x).getUserAssignees().get(k).getName()).append(", ");
									}
									String userAssignees = UA.toString();
									userAssignees = userAssignees.substring(0, userAssignees.length() - 2);
									item.add(userAssignees);
								}else {
									item.add("");
								}
								
								//Estatus de tarjeta
								item.add(dataResponse.get(i).getTodoSets().get(j).getList_cards().get(x).isCompletedCard());
								item.add(dataResponse.get(i).getTodoSets().get(j).getList_cards().get(x).getCreated_at());
								item.add(dataResponse.get(i).getTodoSets().get(j).getList_cards().get(x).getUrlCard());
								//item.add("URL_CARD");
								listItems.add(item);
							}
						}
					}
					
					System.out.println("");
				}
				
			}else {
				log.info("Ya existe la hoja a procesar");
			}
			
			nameFile = nameFile + "!A2";
			googleImpl.updateAndReplaceData(listItems, nameFile, request.getSpreadsheet_id());
			map.put("code", 200);
			map.put("message", "CREATED");
			map.put("result", "OK");
			res = utilities.getResponseEntity(map);
			return res;
		} catch (Exception ex) {
			log.error("##########==> PROBLEMAS AL PROCESAR LA DATA DE BASECAMP");
			log.error(ex.getMessage());
			map.put("code", 500);
			map.put("message", "ERROR");
			map.put("result", null);
			res = utilities.getResponseEntity(map);
			return res;
		}
	}
    
}
