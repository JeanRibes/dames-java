public class Case {
	public boolean blanc;

	public Case() {
		this.blanc = false;
	}
	public void setBlanc() {
		this.blanc = true;
	}

	public boolean isBlanc() {
		return blanc;
	}

	@Override
	public String toString() {
		if (this.blanc)
				return "_";
		else
			return "@";
	}
}
