import javax.swing.JTabbedPane;


public class CommandsTab extends JTabbedPane {

	final private CoursesPanel pnlCourses;
	final private ReqsPanel pnlReqs;
	
	public CommandsTab( CoursesPanel pnlCourses , ReqsPanel pnlReqs ) {
		this.pnlReqs = pnlReqs;
		this.addTab( "Reqs" , this.pnlReqs );
		this.pnlCourses = pnlCourses;
		this.addTab( "Courses" , this.pnlCourses );
	}
}
