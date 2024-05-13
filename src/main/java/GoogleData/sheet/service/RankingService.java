package GoogleData.sheet.service;

import org.springframework.stereotype.Component;

import GoogleData.sheet.dto.request.AddCampaignRequest;
import GoogleData.sheet.dto.response.AddCampaignResponse;

@Component
public interface RankingService {
	AddCampaignResponse addRanking (AddCampaignRequest request);
}
