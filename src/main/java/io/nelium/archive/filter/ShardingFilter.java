package io.nelium.archive.filter;


import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.nelium.archive.model.SignatureFragment;
import io.nelium.archive.model.Transaction;

/**
 * Allows sharding of the IRI input stream. A range such as X-Y means that this loader instance should 
 * process only events with has starting with a string between X and Y.
 * 
 * For instance CDRDGGD-YERRWRW means that this loader should process only hash in the interval [CDRDGGD,YERRWRW[.
 * When the tangel eventually reaches 800tps, distributing the load among multiple loader instances could be necessary.
 * 
 * Note also that the range could overlap. This allows for high availability. 
 * @author pfenkam
 *
 */
@Component
public class ShardingFilter {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

	
	private String shardStart;
	private String shardEnd;
	
	public ShardingFilter(@Value("${io.nelium.shard.range}") String shardRange){
		if(StringUtils.isNotEmpty(shardRange)){
			this.shardStart=shardRange.substring(0, shardRange.indexOf('-'));
			this.shardEnd=shardRange.substring(shardRange.indexOf('-')+1);
		}else{
			logger.info("No shard rules defined. We will accept all transactions");
		}
	}
	

	public boolean accept(Transaction transaction) {
		return accept(transaction.getHash());
	}

	public boolean accept(SignatureFragment fragment) {
		return accept(fragment.getHash());
	}
	
	protected boolean accept(String hash) {
		if(StringUtils.isAnyEmpty(shardEnd,shardStart)){
			return true;
		}
		String head=hash.substring(0, shardStart.length());
		boolean accept= head.compareTo(shardStart)>=0 && head.compareTo(shardEnd)<0;
		if(!accept){
			logger.debug("Rejecting transaction because of shard rule:{}",hash);
		}else{
			logger.debug("Accepting transaction because of shard rule:{}",hash);
		}
		return accept;
	}

}
