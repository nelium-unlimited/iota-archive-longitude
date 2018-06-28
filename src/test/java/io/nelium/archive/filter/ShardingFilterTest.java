package io.nelium.archive.filter;

import static org.junit.Assert.*;
import io.nelium.archive.model.Transaction;

import org.junit.Test;

public class ShardingFilterTest {

	
	@Test
	public void testEmptyShardRule(){
		ShardingFilter filter= new ShardingFilter(null);
		Transaction t= new Transaction();
		t.setHash("A");
		assertTrue(filter.accept(t));
	}
	
	@Test
	public void testFullRangeShardRule(){
		ShardingFilter filter= new ShardingFilter("9-Z");
		Transaction t= new Transaction();
		t.setHash("AGDGEE");
		assertTrue(filter.accept(t));
	}
	
	@Test
	public void testThreeCharacterShardRule(){
		ShardingFilter filter= new ShardingFilter("9DE-ZOI");
		Transaction t= new Transaction();
		t.setHash("AGDGEE");
		assertTrue(filter.accept(t));
	}
	
	@Test
	public void testThreeCharacterShardRuleNoMatch(){
		ShardingFilter filter= new ShardingFilter("BDE-ZOI");
		Transaction t= new Transaction();
		t.setHash("AGDGEE");
		assertFalse(filter.accept(t));
	}
}
