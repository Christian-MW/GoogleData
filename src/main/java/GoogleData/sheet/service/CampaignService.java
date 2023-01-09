package GoogleData.sheet.service;

import org.springframework.stereotype.Component;

import GoogleData.sheet.dto.request.*;
import GoogleData.sheet.dto.response.*;

@Component
public interface CampaignService {
	AddCampaignResponse addCampaign (AddCampaignRequest request);
	void updateFileCampaign(UpdateFileCampaignRequest request);
	void updateStatusCampaign(updateStatusCampaignRequest request);
}
