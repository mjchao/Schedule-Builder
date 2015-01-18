import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Stack;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


public class CommandsPanel extends JPanel {

	final private CommandsTab tbCommands;
	final private BackPanel pnlBack;
	
	public CommandsPanel( CoursesPanel pnlCourses , ReqsPanel pnlReqs , CourseInfoPanel pnlInfo ) {
		setLayout( new BorderLayout() );
		tbCommands = new CommandsTab( pnlCourses , pnlReqs , pnlInfo );
		add( tbCommands , BorderLayout.CENTER );
		this.pnlBack = new BackPanel();
		add( pnlBack , BorderLayout.NORTH );
		
	}
	
	private class CommandsTab extends JTabbedPane {
		final private CoursesPanel pnlCourses;
		final private ReqsPanel pnlReqs;
		final private CourseInfoPanel pnlInfo;
		
		final private Stack< Integer > m_backStack = new Stack< Integer >();
		
		public CommandsTab( CoursesPanel pnlCourses , ReqsPanel pnlReqs, CourseInfoPanel pnlInfo ) {
			this.pnlReqs = pnlReqs;
			this.addTab( "Reqs" , new JScrollPane( this.pnlReqs ) );
			this.pnlReqs.setCommandsTab( CommandsPanel.this );
			this.pnlCourses = pnlCourses;
			this.addTab( "Courses" , new JScrollPane( this.pnlCourses ) );
			this.pnlInfo = pnlInfo;
			this.pnlInfo.setCommandsTab( CommandsPanel.this );
			this.addTab( "Course Info" , new JScrollPane( pnlInfo ) );
		}
		
		@Override
		public void setSelectedIndex( int index ) {
			if ( getSelectedIndex() != -1 ) {
				m_backStack.push( getSelectedIndex() );
			}
			super.setSelectedIndex( index );
			if ( this.getTabComponentAt( index ) instanceof JScrollPane ) {
				( (JScrollPane) this.getTabComponentAt( index ) ).getVerticalScrollBar().setValue( 0 );
			}
		}
		
		public void goBack() {
			if ( !m_backStack.isEmpty() ) {
				super.setSelectedIndex( m_backStack.pop().intValue() );
			}
		}
	}
	
	public void showCourseTab() {
		tbCommands.setSelectedIndex( 1 );
	}
	
	public void showCourseInfoTab() {
		tbCommands.setSelectedIndex( 2 );
	}
	
	private class BackPanel extends JPanel {
		
		final private JButton cmdBack;
		
		public BackPanel() {
			setLayout( new FlowLayout( FlowLayout.CENTER ) );
			this.cmdBack = new JButton( "<==" );
			this.cmdBack.addActionListener( new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					CommandsPanel.this.tbCommands.goBack();
				}
				
			});
			
			add( this.cmdBack );
		}
	}
	
	public void loadCourseData( Course... courses) {
		this.tbCommands.pnlInfo.loadCourses( courses );
		showCourseInfoTab();
	}
	
	public void loadReq( Req r ) {
		this.tbCommands.pnlCourses.filter( r );
		showCourseTab();
	}
}
