package GoogleData.sheet.dao.entity;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="account", schema="public")
public class AccountEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="id", unique = true, nullable = false)
	private UUID id;
	
	@Column(name="screan_name")
	private String screanName;
	
	@Column(name="social_network")
	private String socialNetwork;
	
	@Column(name="followers")
	private long followers;

	@Column(name="link")
	private String link;
	
	@Column(name="image")
	private String image;
	
	@Column(name="name")
	private String name;
	
	 public UUID getId() {
			return id;
		}

		public void setId(UUID id) {
			this.id = id;
		}

		public String getScreanName() {
			return screanName;
		}

		public void setScreanName(String screanName) {
			this.screanName = screanName;
		}

		public String getSocialNetwork() {
			return socialNetwork;
		}

		public void setSocialNetwork(String socialNetwork) {
			this.socialNetwork = socialNetwork;
		}

		public long getFollowers() {
			return followers;
		}

		public void setFollowers(long followers) {
			this.followers = followers;
		}
		
		public String getLink() {
			return link;
		}

		public void setLink(String link) {
			this.link = link;
		}

		@Override
	    public int hashCode() {

	        int hash = 7;

	        hash = 79 * hash + Objects.hashCode(this.id);
	        hash = 79 * hash + Objects.hashCode(this.screanName);
	        hash = 79 * hash + Objects.hashCode(this.socialNetwork);

	        return hash;

	    }
	    
	    @Override
	    public boolean equals(Object obj) {

	        if (this == obj) {

	            return true;
	        }

	        if (obj == null) {

	            return false;
	        }

	        if (getClass() != obj.getClass()) {

	            return false;
	        }

	        final AccountEntity other = (AccountEntity) obj;

	        if (this.screanName != other.screanName || this.socialNetwork != other.socialNetwork) {

	            return false;
	        }

	        return Objects.equals(this.id, other.id);

	    }

	    public String getImage() {
			return image;
		}

		public void setImage(String image) {
			this.image = image;
		}
		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		@Override
	    public String toString() {

	        var builder = new StringBuilder();
	        builder.append("AccountEntity{id=").append(id).append(", screan_name=").append(screanName).append(", social_network=").append(socialNetwork).append("}");

	        return builder.toString();
	    }
		

}
