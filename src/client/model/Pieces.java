package client.model;


public class Pieces {
	
	private String flipped, unflipped;
	private boolean isFlipped, isSelected;
	
	public Pieces(String flipped){
		this.flipped = flipped;
		this.unflipped = "  ";
		this.isFlipped = false;
//		this.isSelected = true;
		this.isSelected = false;
	}
	
	public void setFlipped(String flipped) {
		this.flipped = flipped;
	}

	public boolean isFlipped() {
		return isFlipped;
	}

	public void setFlipped(boolean isFlipped) {
		this.isFlipped = isFlipped;
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isValid) {
		this.isSelected = isValid;
	}

	public String getUnflipped() {
		return unflipped;
	}

	public String getFlipped(){
		return this.flipped;
	}
	

}
