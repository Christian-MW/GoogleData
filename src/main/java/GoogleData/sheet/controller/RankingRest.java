package GoogleData.sheet.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import GoogleData.sheet.dto.request.AddCampaignRequest;
import GoogleData.sheet.dto.response.AddCampaignResponse;
import GoogleData.sheet.service.RankingService;

@RestController
@RequestMapping(value="/GoogleData")
@CrossOrigin("*")
public class RankingRest {

	private static Logger log = Logger.getLogger(CampaignRest.class);
	@Autowired
	RankingService rankingService;
	
	@PostMapping(value="/ranking/addRanking", 
	consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public AddCampaignResponse addRanking(@RequestBody AddCampaignRequest request) {
		log.info("####################################################");
		log.info("######_--------------ADD--RANKING------------_######");
		log.info("####################################################");
		return rankingService.addRanking(request);
	}
}
