import java.util.ArrayList;


public class CourseGroup {

	public ArrayList< Course > m_courses;
	
	public CourseGroup( ArrayList< Course > courses ) {
		this.m_courses = courses;
	}
	
	public CourseGroup( Course... courses ) {
		this.m_courses = new ArrayList< Course >();
		for ( Course c : courses ) {
			this.m_courses.add( c );
		}
	}
	
	@Override
	public String toString() {
		if ( this.m_courses.size() == 0 ) {
			return "";
		}
		else if ( this.m_courses.size() == 1 ){
			return this.m_courses.get( 0 ).getDisplayString();
		}
		else {
			String rtn = this.m_courses.get( 0 ).m_courseId;
			for ( int i=1 ; i<this.m_courses.size() ; i++ ) {
				rtn += " OR " + this.m_courses.get( i ).m_courseId;
			}
			return rtn;
		}
	}
}
