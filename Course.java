import java.util.ArrayList;


public class Course {

	public String m_courseId;
	public String m_courseName;
	public int m_numCredits;
	public boolean m_isRepeatable = false;
	public ArrayList< CourseGroup > m_prereqs = new ArrayList< CourseGroup >();
	
	
	public Course( String courseName ) {
		this.m_courseId = courseName;
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
			return this.m_courseId.equals( ( (Course) o ).m_courseId );
		}
		else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}
	
	public String getDisplayString() {
		return this.m_courseId + ": " + this.m_courseName;
	}
	
	public String getShortDisplayString() {
		return this.m_courseId;
	}
}
