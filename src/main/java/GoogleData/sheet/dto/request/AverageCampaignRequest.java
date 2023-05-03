package GoogleData.sheet.dto.request;

import java.util.List;
import GoogleData.sheet.model.*;

public class AverageCampaignRequest {
	public ReachCampaignModel reach;
	public String spreadsheetId;

	
	public ReachCampaignModel getReach() {
		return reach;
	}
	public void setReach(ReachCampaignModel reach) {
		this.reach = reach;
	}
	public String getSpreadsheetId() {
		return spreadsheetId;
	}
	public void setSpreadsheetId(String spreadsheetId) {
		this.spreadsheetId = spreadsheetId;
	}
	
}
