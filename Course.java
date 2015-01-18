import java.util.ArrayList;


public class Course {

	public String m_courseId;
	public String m_courseName;
	public int m_numCredits;
	public boolean m_isRepeatable = false;
	public String m_description = "";
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
	
	public String getReqsDisplayString() {
		if ( this.m_prereqs.size() == 0 ) {
			return "";
		}
		else if ( this.m_prereqs.size() == 1 ) {
			return this.m_prereqs.get( 0 ).toString();
		}
		else {
			String rtn = this.m_prereqs.get( 0 ).toString();
			for ( int i=1 ; i<this.m_prereqs.size() ; i++ ) {
				rtn += "\n" + this.m_prereqs.get( i ).toString();
			}
			return rtn;
		}
	}
}
