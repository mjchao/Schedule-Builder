import java.util.ArrayList;


public class Course {

	public String m_courseName;
	public boolean m_isRepeatable = false;
	public ArrayList< CourseGroup > m_prereqs = new ArrayList< CourseGroup >();
	
	
	public Course( String courseName ) {
		this.m_courseName = courseName;
	}
	
	public boolean isPrereqFor( Course other ) {
		for ( CourseGroup g : other.m_prereqs ) {
			for ( Course c : g.m_courses ) {
				if ( this.equals( c ) ) {
					return true;
				}
			}
		}
		return false;
	}
	
	@Override
	public boolean equals( Object o ) {
		if ( o instanceof Course ) {
			return this.m_courseName.equals( ( (Course) o ).m_courseName );
		}
		else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}
}
