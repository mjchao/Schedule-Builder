import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JApplet;
import javax.swing.JPanel;
import javax.swing.JScrollPane;


public class Main extends JApplet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Main() {
		setLayout( new BorderLayout() );
		
		Course c1 = new Course( "ENGR151" );
		Course c2 = new Course( "EECS280" );
			c2.m_prereqs.add( c1 );
		Course c3 = new Course( "EECS203" );
			c3.m_prereqs.add( c1 );
		Course c4 = new Course( "EECS281" );
			c4.m_prereqs.add( c2 );
			c4.m_prereqs.add( c3 );
		Course c5 = new Course( "EECS370" );
			c5.m_prereqs.add( c3 );
			c5.m_prereqs.add( c2 );
		
		Sandbox pnlSandbox = new Sandbox();
		JPanel pnlSandboxPadding = new JPanel( new FlowLayout( FlowLayout.LEFT ) );
		pnlSandboxPadding.add( pnlSandbox );
		JScrollPane scrSandbox = new JScrollPane( pnlSandboxPadding );
		add( scrSandbox , BorderLayout.WEST );
		
		ReqsPanel pnlReqs = new ReqsPanel();
			ReqCourse courseReq1 = new ReqCourse( c1 );
			ReqCourse courseReq2 = new ReqCourse( c2 );
			ReqCourse courseReq3 = new ReqCourse( c3 );
			ReqCourse courseReq4 = new ReqCourse( c4 );
			ReqCourse courseReq5 = new ReqCourse( c5 );
			pnlReqs.addReqs( courseReq1 , courseReq2 , courseReq3 , courseReq4 , courseReq5 );
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
		
		pnlCourses.addCourseToList( c1 );
		pnlCourses.addCourseToList( c2 );
		pnlCourses.addCourseToList( c3 );
		pnlCourses.addCourseToList( c4 );
	}
	
}
