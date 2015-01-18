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
}
