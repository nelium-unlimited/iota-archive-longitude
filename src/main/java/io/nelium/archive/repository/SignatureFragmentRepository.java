package io.nelium.archive.repository;

import io.nelium.archive.model.SignatureFragment;
import io.nelium.iota.utils.DBUtils;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * Stores signature fragments in SQL or NewSQL database. Signature fragments are stored separately 
 * from the parent transaction.
 * @author pfenkam
 *
 */
@Component
public class SignatureFragmentRepository {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public void saveAll(List<SignatureFragment> fragments){
		  String sql = DBUtils.getUpsertKeyWord(jdbcTemplate)+
				  "INTO SIGNATURE_FRAGMENT (HASH, TRYTES) VALUES (?, ?)";
		  jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				SignatureFragment fragment = fragments.get(i);
				ps.setString(1, fragment.getHash());
				ps.setString(2, fragment.getTrytes());
			}
					
			@Override
			public int getBatchSize() {
				return fragments.size();
			}
		  });
	}
	
}
