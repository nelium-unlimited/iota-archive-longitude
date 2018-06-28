package io.nelium.iota.utils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.jdbc.support.MetaDataAccessException;

public class DBUtils {

    private static Logger logger = LoggerFactory.getLogger(DBUtils.class);

	public static String getUpsertKeyWord(JdbcTemplate template){
		String driverName=null;
		try {
			driverName = (String) JdbcUtils.extractDatabaseMetaData(template.getDataSource(),"getDriverName");
		} catch (MetaDataAccessException e) {
			logger.warn(e.getMessage(),e);
		}
		if( !StringUtils.isEmpty(driverName) && (driverName.contains("postgres") || driverName.contains("cockroach"))){
			return "upsert ";
		}
		return "merge ";
	}
}
