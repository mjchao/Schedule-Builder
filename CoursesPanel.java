import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.Border;


public class CoursesPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Sandbox pnlSandbox;
	
	private ArrayList< Course > m_allCourses = new ArrayList< Course >();
	
	private GraphicList< Course , CourseDisplay > m_list = new GraphicList< Course , CourseDisplay >( 20 );
	
	public CoursesPanel() {
		setLayout( new BorderLayout() );
		add( this.m_list , BorderLayout.CENTER );
	}
	
	public void setSandbox( Sandbox s ) {
		this.pnlSandbox = s;
	}
	
	public void loadAllCourses( ArrayList< Course > allCourses ) {
		this.m_allCourses = allCourses;
	}
	
	public void filter( Req r ) {
		//TODO
		this.m_list = new GraphicList( 20 );
		
	}
	
	public void addCourseToList( Course c ) {
		CourseDisplay disp = new CourseDisplay( c );
		CourseDisplayListener l = new CourseDisplayListener( disp );
		disp.addMouseListener( l );
		disp.addMouseMotionListener( l );
		this.m_list.addElement( c , disp );
	}
	
	public void loadReq( Req r ) {
		
	}
	
	public void updateCourseStatus( Course c , boolean taken ) {
		this.m_list.getGraphics( c ).setIsAdded( taken );
	}
	
	private class CourseDisplay extends JPanel {
		
		final private Course m_course;
		final private JLabel lblStatus;
		final private JLabel lblCourseName;
		
		final private Border FOCUS_BORDER = BorderFactory.createRaisedBevelBorder();
		final private Border LOST_FOCUS_BORDER = BorderFactory.createEmptyBorder();
		
		private boolean m_isAdded;
		
		public CourseDisplay( Course c ) {
			this.m_course = c;
			setLayout( new FlowLayout( FlowLayout.LEFT ) );
			this.lblStatus = new JLabel();
			add( this.lblStatus );
			
			this.lblCourseName = new JLabel();
			this.lblCourseName.setText( this.m_course.m_courseName );
			add( this.lblCourseName );
			
			reflectNotAdded();
		}
		
		public Course getCourse() {
			return this.m_course;
		}
		
		public void setGainedFocus() {
			if ( !this.m_isAdded || this.m_course.m_isRepeatable ) {
				//this.setBorder( FOCUS_BORDER );
				this.lblCourseName.setText( "<html><u>" + this.m_course.m_courseName + "</u><html>" );
				
				Color fg = Color.blue;
				this.lblCourseName.setForeground( fg );
				
				Color bg = Color.gray;
				this.setBackground( bg );
			}
		}
		
		public void setLostFocus() {
			//this.setBorder( LOST_FOCUS_BORDER );
			this.lblCourseName.setText( this.m_course.m_courseName );
			
			Color fg = Color.black;
			this.lblCourseName.setForeground( fg );
			
			Color bg = UIManager.getLookAndFeelDefaults().getColor( "Label.background" );
			this.setBackground( bg );
		}
		
		public void setIsAdded( boolean isAdded ) {
			this.m_isAdded = isAdded;
			if ( isAdded ) {
				reflectAdded();
			}
			else {
				reflectNotAdded();
			}
			if ( isAdded && !this.m_course.m_isRepeatable ) {
				reflectDisabled();
			}
			else {
				reflectEnabled();
			}
		}
		
		public void reflectAdded() {
			this.lblStatus.setText( "\u2713" );
		}
		
		public void reflectNotAdded() {
			this.lblStatus.setText( " " );
		}
		
		public void reflectEnabled() {
			this.lblCourseName.setForeground( Color.black );
		}
		
		public void reflectDisabled() {
			this.lblCourseName.setForeground( Color.gray );
		}
		
		public boolean isAdded() {
			return this.m_isAdded;
		}
		
		public boolean isAddable() {
			return !this.m_isAdded || this.m_course.m_isRepeatable;
		}
	}
	
	private class CourseDisplayListener extends MouseAdapter {
		
		final private CourseDisplay m_courseDisp;
		
		public CourseDisplayListener( CourseDisplay disp ) {
			this.m_courseDisp = disp;
		}
		
		@Override
		public void mouseMoved( MouseEvent e ) {
			this.m_courseDisp.setGainedFocus();
		}
		
		@Override
		public void mouseExited( MouseEvent e ) {
			this.m_courseDisp.setLostFocus();
		}
		
		@Override
		public void mousePressed( MouseEvent e ) {
			if ( this.m_courseDisp.isAddable() ) {
				CoursesPanel.this.pnlSandbox.addCourse( this.m_courseDisp.getCourse() );
				this.m_courseDisp.setLostFocus();
				this.m_courseDisp.setGainedFocus();
			}
		}
	}
	
}
