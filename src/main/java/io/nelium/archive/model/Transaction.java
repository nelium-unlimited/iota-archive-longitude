package  io.nelium.archive.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;



@Entity
@Table(indexes={
		@Index(name = "IDX_TRANSACTION_ADDRESS", columnList = "address"),
		@Index(name = "IDX_TRANSACTION_BUNDLE", columnList = "bundle")})
public class Transaction {

    @Id
    private String hash;
    private String address;
    private long value;
    private long timestamp;
    private long currentIndex;
    private long lastIndex;
    private String bundle;
    private String tag;
    private long arrivalTime;
    private boolean persistence=false;
    private String trunk;
    private String branch;
    private long milestone=-100;
    

	public long getMilestone() {
		return milestone;
	}

	public void setMilestone(long milestone) {
		this.milestone = milestone;
	}

	public String getTrunk() {
		return trunk;
	}

	public void setTrunk(String trunk) {
		this.trunk = trunk;
	}

	public String getBranch() {
		return branch;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}

	public boolean isPersistence() {
		return persistence;
	}

	public void setPersistence(boolean persistence) {
		this.persistence = persistence;
	}

	public Transaction() {
	}


	public long getArrivalTime() {
		return arrivalTime;
	}

	public void setArrivalTime(long arrivalTime) {
		this.arrivalTime = arrivalTime;
	}

	/**
     * Returns a String that represents this object.
     *
     * @return Returns a string representation of this object.
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

    public String getHash() {
        return hash;
    }

   
    public void setHash(String hash) {
        this.hash = hash;
    }

 

  
    public String getAddress() {
        return address;
    }

   
    public void setAddress(String address) {
        this.address = address;
    }

   

	
    public long getValue() {
		return value;
	}

	public void setValue(long value) {
		this.value = value;
	}

	public String getTag() {
        return tag;
    }

   
    public void setTag(String tag) {
        this.tag = tag;
    }

  
    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

   
    public long getCurrentIndex() {
        return currentIndex;
    }

 
    public void setCurrentIndex(long currentIndex) {
        this.currentIndex = currentIndex;
    }

 
    public long getLastIndex() {
        return lastIndex;
    }

  
    public void setLastIndex(long lastIndex) {
        this.lastIndex = lastIndex;
    }

  
    public String getBundle() {
        return bundle;
    }

    public void setBundle(String bundle) {
        this.bundle = bundle;
    }

}
