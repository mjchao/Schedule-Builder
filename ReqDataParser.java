import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;


public class ReqDataParser {

	private ArrayList< Req > m_reqs = new ArrayList< Req >();
	
	public ReqDataParser( String filename , ArrayList< Course > courses ) throws IOException {
		BufferedReader f = new BufferedReader( new FileReader( filename ) );
		String nextLine = f.readLine();
		while( nextLine != null ) {
			
			//course:
			StringTokenizer st = new StringTokenizer( nextLine );
			
			String reqType = st.nextToken();
			if ( reqType.equals( "Course" ) ) {
				ArrayList< Course > coursesInReq = new ArrayList< Course >();
				while( st.hasMoreTokens() ) {
					String courseName = st.nextToken();
					for ( Course c : courses ) {
						if ( c.m_courseName.equals( courseName ) ) {
							coursesInReq.add( c );
							break;
						}
					}
				}
				if ( coursesInReq.size() <= 0 ) {
					f.close();
					System.out.println( this.m_reqs.size() );
					throw new RuntimeException( "OOPS" );
				}
				ReqCourses reqToAdd = new ReqCourses( coursesInReq );
				this.m_reqs.add( reqToAdd );
			}
			nextLine = f.readLine();
		}
		f.close();
	}
	
	public ArrayList< Req > getReqs() {
		return this.m_reqs;
	}
}
