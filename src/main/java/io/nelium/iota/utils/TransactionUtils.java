package io.nelium.iota.utils;


import io.nelium.archive.model.SignatureFragment;
import io.nelium.archive.model.Transaction;

public class TransactionUtils {
	
	public static SignatureFragment parseSignatureFragment(String event){
		String parts[]=event.split(" ");
		SignatureFragment fragment= new SignatureFragment();
		fragment.setTrytes(parts[1]);
		fragment.setHash(parts[2]);
		return fragment;
	}

	public static Transaction parseTransaction(String event){
		String parts[]=event.split(" ");
		//See: https://gist.github.com/ralfr/3a411a6449ff942b10b45adaaa8528ba
		Transaction transaction= new Transaction();
		if(parts[0].equals("tx")){
			transaction.setHash(parts[1]);
			transaction.setAddress(parts[2]);
			transaction.setValue(Long.parseLong(parts[3]));
			transaction.setTag(parts[4]);
			transaction.setTimestamp(Long.parseLong(parts[5]));
			transaction.setCurrentIndex(Long.parseLong(parts[6]));
			transaction.setLastIndex(Long.parseLong(parts[7]));
			transaction.setTrunk(parts[9]);
			transaction.setBranch(parts[10]);
			transaction.setArrivalTime(Long.parseLong(parts[11]));
		}else	if(parts[0].equals("sn")){
			transaction.setBundle(parts[parts.length-1]);
			transaction.setHash(parts[2]);
			transaction.setAddress(parts[3]);
			transaction.setMilestone(Long.parseLong(parts[1]));
		}
		return transaction;
	}
	
	
}
