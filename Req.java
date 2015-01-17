
abstract public class Req {

	public Req() {
		
	}
	
	abstract String getDisplay();
	
	abstract boolean satisfiesReq( Course c );
}
