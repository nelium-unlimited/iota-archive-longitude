package io.nelium.archive.repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import io.nelium.archive.model.Transaction;
import io.nelium.iota.utils.DBUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class TransactionRepository {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public void saveAll(List<Transaction> transactions){
		
		String sql=DBUtils.getUpsertKeyWord(jdbcTemplate)
				+"into transaction (ADDRESS, ARRIVAL_TIME, BUNDLE, CURRENT_INDEX, HASH, "
				+"LAST_INDEX,  TAG, TIMESTAMP, VALUE,TRUNK,BRANCH,PERSISTENCE,MILESTONE) values "
				+"(?,?,?,?,?,?,?,?,?,?,?,?,?)";
		  jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				Transaction t = transactions.get(i);
				ps.setString(1, t.getAddress());
				ps.setLong(2, t.getArrivalTime());
				ps.setString(3, t.getBundle());
				ps.setLong(4,t.getCurrentIndex());
				ps.setString(5,t.getHash());
				ps.setLong(6,t.getLastIndex());
				ps.setString(7,t.getTag());
				ps.setLong(8,t.getTimestamp());
				ps.setLong(9,t.getValue());
				ps.setString(10, t.getTrunk());
				ps.setString(11, t.getBranch());
				ps.setBoolean(12, t.isPersistence());
				ps.setLong(13, t.getMilestone());
			}
					
			@Override
			public int getBatchSize() {
				return transactions.size();
			}
		  });
	}
	
}
