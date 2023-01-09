package GoogleData.sheet.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import GoogleData.sheet.dto.request.*;
import GoogleData.sheet.dto.response.*;
import GoogleData.sheet.service.CampaignService;

@RestController
@RequestMapping(value="/GoogleData")
public class CampaignRest {
	private static Logger log = Logger.getLogger(CampaignRest.class);
	@Autowired
	CampaignService campaignService;
	
	@PostMapping(value="/campaign/addcampaign", 
	consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public AddCampaignResponse getDataSpreadsheet(@RequestBody AddCampaignRequest request) {
		log.info("##################################");
		log.info("##########_ADD-CAMPAIGN_##########");
		log.info("##################################");
		return campaignService.addCampaign(request);
	}
	
	@PostMapping(value="/campaign/updateFile", 
	consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public void updateFileCampaign(@RequestBody UpdateFileCampaignRequest request) {
		log.info("##################################");
		log.info("######_UPDATE-FILE-CAMPAIGN_######");
		log.info("##################################");
		campaignService.updateFileCampaign(request);
	}
	
	@PostMapping(value="/campaign/updateStatus", 
	consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public void updateStatusCampaign(@RequestBody updateStatusCampaignRequest request) {
		log.info("##################################");
		log.info("######_UPDATE-*STATUS*-CAMPAIGN_######");
		log.info("##################################");
		campaignService.updateStatusCampaign(request);
	}
}
