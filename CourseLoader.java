import java.io.File;
import java.util.ArrayList;


public class CourseLoader {

	private ArrayList< Course > m_courseList;
	public CourseLoader() {
		File f = new File( "/Users/mjchao/Desktop/Java/Eclipse Workspace/Graduation Planner/data" );
		ArrayList< Course > courses = new ArrayList< Course >();
		createCourses( courses , f );
		createPrereqs( courses , f );
		this.m_courseList = courses;
	}
	
	public ArrayList< Course > getCourses() {
		return this.m_courseList;
	}
	
	public void createCourses( ArrayList< Course > courses , File f ) {
		
		//avoid ./DS_STORE
		if ( f.getName().startsWith( "." ) ) {
			return;
		}
		if ( !f.isDirectory() ) {
			CourseDataParser p = new CourseDataParser( f.getPath() );
			Course c = createCourse( p );
			courses.add( c );
		}
		File[] subdirectories = f.listFiles();
		if ( subdirectories != null ) {
			for ( File subfile : subdirectories ) {
				createCourses( courses , subfile );
			}
		}
	}
	
	public void createPrereqs( ArrayList< Course > courses , File f ) {
		if ( !f.isDirectory() ) {
			CourseDataParser p = new CourseDataParser( f.getPath() );
			for ( Course c : courses ) {
				if ( c.m_courseId.equals( p.getTitle() ) ) {
					addPrereqs( c , p , courses );
					break;
				}
			}
		}
		File[] subdirectories = f.listFiles();
		if ( subdirectories != null ) {
			for ( File subfile : subdirectories ) {
				createPrereqs( courses , subfile );
			}
		}
	}
	
	public Course createCourse( CourseDataParser p ) {
		Course rtn = new Course( p.getTitle() );
		rtn.m_numCredits = p.getNumCredits();
		rtn.m_courseName = p.getCourseName();
		rtn.m_description = p.getDescription();
		return rtn;
	}
	
	public void addPrereqs( Course c , CourseDataParser p , ArrayList< Course > courses ) {
		ArrayList< ArrayList< String > > prereqs = p.getPrereqs();
		for( ArrayList< String > reqGroup : prereqs ) {
			ArrayList< Course > coursesInGroup = new ArrayList< Course >();
			for ( String s : reqGroup ) {
				for ( int i=0 ; i<courses.size() ; i++ ) {
					if ( courses.get( i ).m_courseId.equals( s ) ) {
						coursesInGroup.add( courses.get( i ) );
					}
				}
			}
			c.m_prereqs.add( new CourseGroup( coursesInGroup ) );
		}
	}
	
	/*
	final public static void main( String[] args ) throws IOException {
		new CourseLoader();
	}//*/
}
