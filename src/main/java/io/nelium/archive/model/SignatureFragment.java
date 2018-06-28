package io.nelium.archive.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;

import org.hibernate.annotations.Type;

@Entity
public class SignatureFragment {

	@Id
	private String hash;
	
	@Lob
	@Type(type = "org.hibernate.type.TextType")
	private String trytes;
	
	public String getHash() {
		return hash;
	}
	public void setHash(String hash) {
		this.hash = hash;
	}
	public String getTrytes() {
		return trytes;
	}
	public void setTrytes(String trytes) {
		this.trytes = trytes;
	}
	
}
