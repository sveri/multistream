package de.sveri.multistream.db;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OAuth2AuthorizedClientRepository extends JpaRepository<OAuth2AuthorizedClientEntity, Long> {

	Optional<OAuth2AuthorizedClientEntity> findByClientRegistrationIdAndPrincipalName(String clientRegistrationId,
			String principalName);

	void deleteByClientRegistrationIdAndPrincipalName(String clientRegistrationId, String principalName);

	List<OAuth2AuthorizedClientEntity> findByPrincipalName(String principalName);

	@Modifying
	@Query("DELETE FROM OAuth2AuthorizedClientEntity o WHERE o.accessTokenExpiresAt < :expiredBefore")
	void deleteExpiredTokens(@Param("expiredBefore") Instant expiredBefore);
}
