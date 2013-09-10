/* A queue containing the list of the last n visited URLs
 * 
 */
package jaw;

public class RecentHistory {

	protected class Address {
		
		protected String myAddress;
		protected Address previous;
		protected Address next;
		
		public Address(String address) {
			this.myAddress = address;
			this.next = null;
			this.previous = null;
		}
		
		public void setNext(Address address) {
			this.next = address;
		}
		
		public void setPrevious(Address address) {
			this.previous = address;
		}
		
		public Address getPrevious() {
			return this.previous;
		}
		
		public String getAddress() {
			return this.myAddress;
		}
		
	}
	
	protected Address myFirstAddress;
	protected Address myLastAddress;
	protected int addressCount;
	
	public RecentHistory() {
		this.myFirstAddress = null;
		this.myLastAddress = null;
		
		this.addressCount = 0;
	}
	
	public void push(String address) {
		if (address == null) return;
		
		if (this.myLastAddress != null && this.myLastAddress.getAddress().equals(address)) return;
		
		Address newAddress = new Address(address);
		
		if (this.myFirstAddress == null)
			this.myFirstAddress = newAddress;
		else {
			newAddress.setPrevious(this.myLastAddress);
			if (this.myLastAddress != null) this.myLastAddress.setNext(newAddress);
			this.myLastAddress = newAddress;
		}
		
		this.addressCount++;
	}
	
	public String pop() {
		if (this.addressCount < 1) return null;
		
		String address = this.myLastAddress.getAddress();
		
		if (this.addressCount == 1) {
			this.myFirstAddress = null;
			this.myLastAddress = null;
		} else {
			Address previous = this.myLastAddress.getPrevious();
			previous.setNext(null);
			this.myLastAddress = previous;
		}
		
		this.addressCount--;
		
		return address;
	}
	
	public int count() {
		return this.addressCount;
	}
	
}
