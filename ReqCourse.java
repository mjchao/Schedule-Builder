
public class ReqCourse extends Req {

	public Course course;
	
	public ReqCourse( Course c ) {
		this.course = c;
	}

	@Override
	String getDisplay() {
		return this.course.getDisplayString();
	}

	@Override
	boolean satisfiesReq( Course c ) {
		return this.course.equals( c );
	}
}
