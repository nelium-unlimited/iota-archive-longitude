package io.nelium.archive.repository;

import io.nelium.archive.model.Transaction;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * Stores transaction confirmation to a SQL database. Supported databases are standard SQL as well as NewSQL databases.
 * There is no separate table for confirmation even received from IRI. Instead, the flags persistence and milestone are 
 * updated in the transaction table.
 * 
 * @author pfenkam
 *
 */
@Component
public class PersistenceRepository  {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public void saveAll(List<Transaction> transactions){
		  String sql = "update transaction set milestone=?, persistence=true where hash=?";
		  jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				Transaction transaction = transactions.get(i);
				ps.setLong(1, transaction.getMilestone());
				ps.setString(2, transaction.getHash());
			}
					
			@Override
			public int getBatchSize() {
				return transactions.size();
			}
		  });
	}
	
}
