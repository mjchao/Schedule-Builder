import java.util.ArrayList;


public class Course {

	public String m_courseName;
	public ArrayList< Course > m_prereqs = new ArrayList< Course >();
	
	public Course( String courseName ) {
		this.m_courseName = courseName;
	}
	
	public boolean isPrereqFor( Course other ) {
		if ( other.m_prereqs.contains( this ) ) {
			return true;
		}
		else {
			return false;
		}
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
}
