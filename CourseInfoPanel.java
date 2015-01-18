import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class CourseInfoPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private CommandsPanel pnlParent;
	
	private Sandbox pnlSandbox;
	
	private GraphicList< Course , InfoPanel > m_list = new GraphicList< Course , InfoPanel >( 10 );
	
	public CourseInfoPanel() {
		setLayout( new BorderLayout() );
		add( this.m_list , BorderLayout.NORTH );
		
	}
	
	public void addCourse( Course c ) {
		this.m_list.add( new InfoPanel( c ) );
	}
	
	public void loadCourses( Course... courses ) {
		this.remove( this.m_list );
		this.m_list = new GraphicList< Course , InfoPanel >( 10 );
		for( Course c : courses ) {
			addCourse( c );
		}
		add( this.m_list , BorderLayout.NORTH );
	}
	
	public void setCommandsTab( CommandsPanel tb ) {
		this.pnlParent = tb;
	}
	
	public void setSandbox( Sandbox pnlSandbox ) {
		this.pnlSandbox = pnlSandbox;
	}
	
	private class InfoPanel extends JPanel {
		
		public InfoPanel( final Course c ) {
			setLayout( new BoxLayout( this , BoxLayout.Y_AXIS ) );
			
			JLabel title = new JLabel( c.getDisplayString() );
			title.setFont( new Font( Font.SANS_SERIF , Font.BOLD , 15 ) );
			add( title );
			
			add( new JLabel( "ID: " + c.m_courseId ) );
			
			add( new JLabel( "Name: " + c.m_courseName ) );
			
			String reqsDisplayString;
			if ( c.m_prereqs.size() > 0 ) {
				reqsDisplayString = "<li>" + c.getReqsDisplayString();
				reqsDisplayString = reqsDisplayString.replace( "\n" , "<li>" );
				reqsDisplayString = "<ul>" + reqsDisplayString + "</ul>";
			}
			else {
				reqsDisplayString = " None ";
			}
			JLabel reqs = new JLabel( "<html>Reqs: " + reqsDisplayString + "</html>" );
			add( reqs );
			
			JButton cmdViewReqs = new JButton( "View Reqs" );
			cmdViewReqs.addActionListener( new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					ArrayList< Course > reqs = new ArrayList< Course >();
					for ( CourseGroup g : c.m_prereqs ) {
						for ( Course course : g.m_courses ) {
							reqs.add( course );
						}
					}
					pnlParent.loadReq( new ReqCourses( reqs ) );
				}
				
			});
			add( cmdViewReqs );
			
			String description = c.m_description;
			for ( int i=0 ; i*75 < c.m_description.length() ; i++ ) {
				description = description.substring( 0 , i*75 ) + "<p>" + description.substring( i*75 , description.length() );
			}
			
			JLabel lblDescription = new JLabel( "<html>Description: <p>" + description + "</html>" );
			add( lblDescription );
			
			this.setBorder( BorderFactory.createEtchedBorder() );
			
		}
	}
	
}
