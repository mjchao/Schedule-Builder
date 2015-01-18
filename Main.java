import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JApplet;
import javax.swing.JPanel;
import javax.swing.JScrollPane;


public class Main extends JApplet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Main() throws IOException {
		CourseLoader loader = new CourseLoader();
		ArrayList< Course > courses = loader.getCourses();
		
		ReqDataParser reqParser = new ReqDataParser( "/Users/mjchao/Desktop/Java/Eclipse Workspace/Graduation Planner/reqdata/eecsmajor" ,  courses );
		ArrayList< Req > reqs = reqParser.getReqs();
		
		setLayout( new BorderLayout() );
		
		Sandbox pnlSandbox = new Sandbox();
		JPanel pnlSandboxPadding = new JPanel( new FlowLayout( FlowLayout.LEFT ) );
		pnlSandboxPadding.add( pnlSandbox );
		JScrollPane scrSandbox = new JScrollPane( pnlSandboxPadding );
		add( scrSandbox , BorderLayout.WEST );
		
		ReqsPanel pnlReqs = new ReqsPanel();
		
		CoursesPanel pnlCourses = new CoursesPanel();
		CommandsTab tbCommands = new CommandsTab( pnlCourses , pnlReqs );
		JScrollPane scrCommands = new JScrollPane( tbCommands );
		add( scrCommands , BorderLayout.CENTER );
		setVisible( true );
		
		pnlSandbox.setReqsPanel( pnlReqs );
		pnlSandbox.setCoursesPanel( pnlCourses );
		
		pnlReqs.setCoursesPanel( pnlCourses );
		
		pnlCourses.setSandbox( pnlSandbox );
		
		/*pnlSandbox.addCourse( c3 );
		pnlSandbox.addCourse( c2 );
		pnlSandbox.addCourse( c1 );
		pnlSandbox.addCourse( c4 );
		pnlSandbox.repaint();*/
		
		for ( Course c : courses ) {
			pnlCourses.addCourseToList( c );
			if ( c.m_courseName.equals( "EECS281" ) ) {
				for ( CourseGroup g : c.m_prereqs ) {
					for ( Course c2 : g.m_courses ) {
						System.out.println( c2.m_courseName );
					}
				}
			}
		}
		
		for ( Req r : reqs ) {
			pnlReqs.addReqs( r );
		}
	}
	
}
