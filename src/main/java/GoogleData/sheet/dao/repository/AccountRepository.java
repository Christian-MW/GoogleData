package GoogleData.sheet.dao.repository;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import GoogleData.sheet.dao.entity.AccountEntity;


@Repository
public interface AccountRepository extends JpaRepository<AccountEntity,Serializable> {

	public List<AccountEntity> findByScreanNameIgnoreCaseContaining(String screaName);
	public List<AccountEntity> findByScreanNameIgnoreCase(String screaName);
	public AccountEntity findById(UUID id);
	public List<AccountEntity> findByScreanNameAndSocialNetworkIgnoreCase(String screaName, String socialN);
	
}
