import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;


public class Main99999 extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Main99999() throws IOException {
		CourseLoader loader = new CourseLoader();
		ArrayList< Course > courses = loader.getCourses();
		
		ReqDataParser reqParser = new ReqDataParser( "/Users/mjchao/Desktop/Java/Eclipse Workspace/Graduation Planner/reqdata/eecsmajor" ,  courses );
		ArrayList< Req > reqs = reqParser.getReqs();
		
		setLayout( new BorderLayout() );
		
		Sandbox pnlSandbox = new Sandbox();
		JPanel pnlSandboxPadding = new JPanel( new FlowLayout( FlowLayout.LEFT ) );
		pnlSandboxPadding.setPreferredSize( new Dimension( Sandbox.SEMESTER_WIDTH+30 , Sandbox.SEMESTER_HEIGHT*Sandbox.NUM_SEMESTERS+10 ) );
		pnlSandboxPadding.add( pnlSandbox );
		JScrollPane scrSandbox = new JScrollPane( pnlSandboxPadding );
		add( scrSandbox , BorderLayout.WEST );
		
		ReqsPanel pnlReqs = new ReqsPanel();
		
		CourseInfoPanel pnlCourseInfo = new CourseInfoPanel();
		
		CoursesPanel pnlCourses = new CoursesPanel();
		CommandsPanel tbCommands = new CommandsPanel( pnlCourses , pnlReqs , pnlCourseInfo );
		add( tbCommands , BorderLayout.CENTER );
		setVisible( true );
		
		pnlSandbox.setReqsPanel( pnlReqs );
		pnlSandbox.setCoursesPanel( pnlCourses );
		pnlSandbox.setCommandsPanel( tbCommands );
		
		pnlReqs.setCoursesPanel( pnlCourses );
		
		pnlCourses.setSandbox( pnlSandbox );
		
		/*pnlSandbox.addCourse( c3 );
		pnlSandbox.addCourse( c2 );
		pnlSandbox.addCourse( c1 );
		pnlSandbox.addCourse( c4 );
		pnlSandbox.repaint();*/
		
		pnlCourses.loadAllCourses( courses );
		
		for ( Req r : reqs ) {
			pnlReqs.addReqs( r );
		}
	}
	
	final public static void main( String[] args ) throws IOException {
		Main99999 m = new Main99999();
		m.pack();
		m.setVisible( true );
		m.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
	}
	
}
