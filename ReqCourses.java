import java.util.ArrayList;


public class ReqCourses extends Req {

	final private ArrayList< Course > m_courses;
	
	public ReqCourses( ArrayList< Course > courses ) {
		this.m_courses = courses;
	}
	@Override
	String getDisplay() {
		if ( m_courses.size() == 1 ) {
			return m_courses.get( 0 ).getDisplayString();
		}
		else {
			String rtn = m_courses.get( 0 ).getShortDisplayString();
			for ( int i=1 ; i<m_courses.size() ; i++ ) {
				rtn = rtn + " OR " + m_courses.get( i ).getShortDisplayString();
			}
			return rtn;
		}
	}

	@Override
	boolean satisfiesReq(Course c) {
		for ( Course reqCourse : m_courses ) {
			if ( reqCourse.equals( c ) ) {
				return true;
			}
		}
		return false;
	}
	
}
