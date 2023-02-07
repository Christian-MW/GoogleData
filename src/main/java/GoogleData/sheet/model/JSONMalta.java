package GoogleData.sheet.model;

import java.util.List;

public class JSONMalta {
	public JSONMaltaID _id;
	public List<Integer> weight;
	public List<String> possible_drugs_added;
	public List<String> particle_types;
	public List<String> images_particle;
	public List<String> images;
	public List<String> featured_in;
	public String item_number;
	public String item_name;
	public String item_description;
	public String long_description;
	public String item_shortname;
	public String brand_code;
	public String division_code;
	public String subdivision;
	public String price_cathegory;
	public String sub_brand;
	public String cathegory_inventory;
	public String stage;
	public String item_status;
	public String sales_product_type_code;
	public String technology_code;
	public Integer protein;
	public Integer fat;
	public Integer humidity;
	public String benefits;
	public Integer fiber;
	public Integer ashes;
	public Integer eln;
	public String particle;
	public JSONMaltaDate createdAt;
	public JSONMaltaDate updatedAt;
	public Boolean active;
	public JSONMaltaMeta metadata;
	public String notes;
	
	
	public JSONMaltaID get_id() {
		return _id;
	}
	public void set_id(JSONMaltaID _id) {
		this._id = _id;
	}
	public List<Integer> getWeight() {
		return weight;
	}
	public void setWeight(List<Integer> weight) {
		this.weight = weight;
	}

	public List<String> getPossible_drugs_added() {
		return possible_drugs_added;
	}
	public void setPossible_drugs_added(List<String> possible_drugs_added) {
		this.possible_drugs_added = possible_drugs_added;
	}
	public List<String> getParticle_types() {
		return particle_types;
	}
	public void setParticle_types(List<String> particle_types) {
		this.particle_types = particle_types;
	}
	public List<String> getImages_particle() {
		return images_particle;
	}
	public void setImages_particle(List<String> images_particle) {
		this.images_particle = images_particle;
	}
	public List<String> getImages() {
		return images;
	}
	public void setImages(List<String> images) {
		this.images = images;
	}
	public List<String> getFeatured_in() {
		return featured_in;
	}
	public void setFeatured_in(List<String> featured_in) {
		this.featured_in = featured_in;
	}
	public String getItem_number() {
		return item_number;
	}
	public void setItem_number(String item_number) {
		this.item_number = item_number;
	}
	public String getItem_name() {
		return item_name;
	}
	public void setItem_name(String item_name) {
		this.item_name = item_name;
	}
	public String getItem_description() {
		return item_description;
	}
	public void setItem_description(String item_description) {
		this.item_description = item_description;
	}
	public String getLong_description() {
		return long_description;
	}
	public void setLong_description(String long_description) {
		this.long_description = long_description;
	}
	public String getItem_shortname() {
		return item_shortname;
	}
	public void setItem_shortname(String item_shortname) {
		this.item_shortname = item_shortname;
	}
	public String getBrand_code() {
		return brand_code;
	}
	public void setBrand_code(String brand_code) {
		this.brand_code = brand_code;
	}
	public String getDivision_code() {
		return division_code;
	}
	public void setDivision_code(String division_code) {
		this.division_code = division_code;
	}
	public String getSubdivision() {
		return subdivision;
	}
	public void setSubdivision(String subdivision) {
		this.subdivision = subdivision;
	}
	public String getPrice_cathegory() {
		return price_cathegory;
	}
	public void setPrice_cathegory(String price_cathegory) {
		this.price_cathegory = price_cathegory;
	}
	public String getSub_brand() {
		return sub_brand;
	}
	public void setSub_brand(String sub_brand) {
		this.sub_brand = sub_brand;
	}
	public String getCathegory_inventory() {
		return cathegory_inventory;
	}
	public void setCathegory_inventory(String cathegory_inventory) {
		this.cathegory_inventory = cathegory_inventory;
	}
	public String getStage() {
		return stage;
	}
	public void setStage(String stage) {
		this.stage = stage;
	}
	public String getItem_status() {
		return item_status;
	}
	public void setItem_status(String item_status) {
		this.item_status = item_status;
	}
	public String getSales_product_type_code() {
		return sales_product_type_code;
	}
	public void setSales_product_type_code(String sales_product_type_code) {
		this.sales_product_type_code = sales_product_type_code;
	}
	public String getTechnology_code() {
		return technology_code;
	}
	public void setTechnology_code(String technology_code) {
		this.technology_code = technology_code;
	}
	public Integer getProtein() {
		return protein;
	}
	public void setProtein(Integer protein) {
		this.protein = protein;
	}
	public Integer getFat() {
		return fat;
	}
	public void setFat(Integer fat) {
		this.fat = fat;
	}
	public Integer getHumidity() {
		return humidity;
	}
	public void setHumidity(Integer humidity) {
		this.humidity = humidity;
	}
	public String getBenefits() {
		return benefits;
	}
	public void setBenefits(String benefits) {
		this.benefits = benefits;
	}
	public Integer getFiber() {
		return fiber;
	}
	public void setFiber(Integer fiber) {
		this.fiber = fiber;
	}
	public Integer getAshes() {
		return ashes;
	}
	public void setAshes(Integer ashes) {
		this.ashes = ashes;
	}
	public Integer getEln() {
		return eln;
	}
	public void setEln(Integer eln) {
		this.eln = eln;
	}
	public String getParticle() {
		return particle;
	}
	public void setParticle(String particle) {
		this.particle = particle;
	}
	public JSONMaltaDate getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(JSONMaltaDate createdAt) {
		this.createdAt = createdAt;
	}
	public JSONMaltaDate getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(JSONMaltaDate updatedAt) {
		this.updatedAt = updatedAt;
	}
	public Boolean getActive() {
		return active;
	}
	public void setActive(Boolean active) {
		this.active = active;
	}
	public JSONMaltaMeta getMetadata() {
		return metadata;
	}
	public void setMetadata(JSONMaltaMeta metadata) {
		this.metadata = metadata;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
}
