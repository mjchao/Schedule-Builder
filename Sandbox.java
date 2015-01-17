import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.AbstractBorder;
import javax.swing.border.Border;


public class Sandbox extends JPanel {

	final public static int SEMESTER_WIDTH = 500;
	final public static int SEMESTER_HEIGHT = 100;
	final public static int NUM_SEMESTERS = 8;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private ArrayList< CourseButton > m_courses = new ArrayList< CourseButton >();
	private ArrayList< SemesterPanel > m_semesters = new ArrayList< SemesterPanel >();
	
	private ReqsPanel pnlReqs;
	private CoursesPanel pnlCourses;
	private SemesterPanel pnlSelectedSemester;
	
	public Sandbox() {
		setLayout( null );
		setPreferredSize( new Dimension( SEMESTER_WIDTH , SEMESTER_HEIGHT*NUM_SEMESTERS ) );
		setMaximumSize( getPreferredSize() );
		setSize( getPreferredSize() );
		for ( int i=0 ; i<NUM_SEMESTERS ; i++ ) {
			addSemester( i );
		}
		setSelectedSemester( this.m_semesters.get( 0 ) );
	}
	
	public void setReqsPanel( ReqsPanel reqs ) {
		this.pnlReqs = reqs;
	}
	
	public void refreshPrereqs() {
		ArrayList< Course > completedCourses = new ArrayList< Course >();
		for ( CourseButton b : this.m_courses ) {
			if ( b.arePrereqsOkay() ) {
				completedCourses.add( b.getCourse() );
			}
		}
		ArrayList< Course > possiblyCompletedCourses = new ArrayList< Course >();
		for ( CourseButton b : this.m_courses ) {
			if ( !b.arePrereqsOkay() ) {
				possiblyCompletedCourses.add( b.getCourse() );
			}
		}
		this.pnlReqs.refreshCompletedReqs( completedCourses , possiblyCompletedCourses );
	}
	
	public void setCoursesPanel( CoursesPanel courses ) {
		this.pnlCourses = courses;
	}
	
	public void addSemester( int semesterId ) {
		SemesterPanel newSemester = new SemesterPanel( semesterId );
		SemesterPanelListener l = new SemesterPanelListener( newSemester );
		newSemester.addMouseListener( l );
		newSemester.setLocation(0 , SEMESTER_HEIGHT*semesterId );
		newSemester.setSize( SEMESTER_WIDTH , SEMESTER_HEIGHT );
		add( newSemester );
		Sandbox.this.setComponentZOrder( newSemester , semesterId );
		this.m_semesters.add( newSemester );
	}
	
	public void addCourse( Course c ) {
		CourseButton newCourse = createCourseButton( c , this.pnlSelectedSemester );
		add( newCourse );
		Sandbox.this.setComponentZOrder( newCourse , 0 );
		m_courses.add( newCourse );
		revalidate();
		repaint();
		checkRequirements();
		this.pnlCourses.updateCourseStatus( c , true );
	}
	
	public boolean isCourseAdded( Course c ) {
		for ( CourseButton b : this.m_courses ) {
			if ( b.getCourse().equals( c ) ) {
				return true;
			}
		}
		return false;
	}
	
	public void deleteCourse( CourseButton c ) {
		m_courses.remove( c );
		remove( c );
		checkRequirements();
		this.pnlCourses.updateCourseStatus( c.getCourse() , false );
	}
	
	public void drawDependencies( Graphics g ) {
		for ( CourseButton c : m_courses ) {
			for ( CourseButton c2 : m_courses ) {
				if ( c.isPrereqFor( c2 ) ) {
					drawDependency( g , c , c2 );
				}
			}
		}
	}
	
	public void drawDependency( Graphics g , CourseButton c1 , CourseButton c2 ) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.drawLine( c1.getCenterX() , c1.getCenterY() , c2.getCenterX(), c2.getCenterY() );
	}
	
	public void moveToNearestSemester( CourseButton b ) {
		if ( b.getY() / SEMESTER_HEIGHT != (b.getY()+b.getHeight()) / SEMESTER_HEIGHT ) {
			int centerY = b.getCenterY();
			for ( int i=1 ; i<NUM_SEMESTERS ; i++ ) {
				int dividingY = i*SEMESTER_HEIGHT;
				if ( b.getY() < dividingY && dividingY < b.getY()+b.getHeight() ) {
					if ( centerY-dividingY <= 0 ) {
						int newY = b.getY() - ( b.getY()+b.getHeight() - dividingY );
						b.setLocation( b.getX() , newY );
					}
					else {
						int newY = dividingY;
						b.setLocation( b.getX() , newY );
					}
				}
			}
		}
	}
	
	public ArrayList< CourseButton > getCoursesInSemester( int semesterId ) {
		ArrayList< CourseButton > rtn = new ArrayList< CourseButton >();
		int minY = semesterId*SEMESTER_HEIGHT;
		int maxY = (semesterId+1)*SEMESTER_HEIGHT;
		for ( int i=0 ; i<m_courses.size() ; i++ ) {
			int courseY = m_courses.get( i ).getY();
			if ( minY <= courseY && courseY < maxY ) {
				rtn.add( m_courses.get( i ) );
			}
		}
		return rtn;
	}
	
	public void checkRequirements() {
		ArrayList< Course > coursesTaken = new ArrayList< Course >();
		ArrayList< Course > coursesPossiblyTaken = new ArrayList< Course >();
		for ( int i=0 ; i<NUM_SEMESTERS ; i++ ) {
			ArrayList< CourseButton > courseButtons = getCoursesInSemester( i );
			for ( CourseButton b : courseButtons ) {
				int courseStatus = CourseButton.OKAY;
				for ( Course c : b.getPrereqs() ) {
					if ( !coursesTaken.contains( c ) ) {
						if ( coursesPossiblyTaken.contains( c )  ) {
							courseStatus = CourseButton.POSSIBLY_MISSING_PREREQS;
						}
						else {
							courseStatus = CourseButton.MISSING_PREREQ;
						}
						break;
					}
				}
				if ( courseStatus == CourseButton.OKAY ) {
					b.unflagMissingPrereq();
				}
				else if ( courseStatus == CourseButton.POSSIBLY_MISSING_PREREQS ) {
					b.flagPossiblyMissingPrereq();
				}
				else if ( courseStatus == CourseButton.MISSING_PREREQ ){
					b.flagMissingPrereq();
				}
			}
			for ( CourseButton b : courseButtons ) {
				if ( b.getStatus() == CourseButton.OKAY ) {
					coursesTaken.add( b.getCourse() );
				}
				else {
					coursesPossiblyTaken.add( b.getCourse() );
				}
			}
		}
		this.refreshPrereqs();
	}
	
	@Override
	public void paintComponent( Graphics g ) {
		super.paintComponent( g );
		drawDependencies( g );
	}
	
	public void setSelectedSemester( SemesterPanel pnlSemester ) {
		for ( SemesterPanel p : this.m_semesters ) {
			p.reflectUnselected();
		}
		pnlSemester.reflectSelected();
		this.pnlSelectedSemester = pnlSemester;
	}
	
	private class DashedBorder extends AbstractBorder {
	    /**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public DashedBorder() {
			
		}
		
		@Override
	    public void paintBorder(Component comp, Graphics g, int x, int y, int w, int h) {
	        Graphics2D gg = (Graphics2D) g;
	        gg.setColor(Color.GRAY);
	        gg.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{5}, 0));
	        gg.drawRect(x, y, w - 5, h - 5);
	    }
	}
	
	private class SemesterPanel extends JPanel {
		
		final public Border OUTLINE_BORDER = new DashedBorder();
		final public Border RAISED_BORDER = BorderFactory.createRaisedBevelBorder();
		final public Border RAISED_OUTLINE_BORDER = BorderFactory.createLineBorder( Color.black , 1 );
		final public Border SELECTED_BORDER = BorderFactory.createCompoundBorder( RAISED_OUTLINE_BORDER , RAISED_BORDER );
		final public Border UNSELECTED_BORDER = OUTLINE_BORDER;
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		private int m_semesterId;
		
		public SemesterPanel( int semesterId ) {
			setLayout( null );
			this.m_semesterId = semesterId;
			this.setOpaque( false );
			this.setBorder( BorderFactory.createLineBorder( Color.BLACK , 1 ) );
		}
		
		public int getSemesterId() {
			return this.m_semesterId;
		}
		
		public void reflectSelected() {
			this.setBorder( SELECTED_BORDER );
		}
		
		public void reflectUnselected() {
			this.setBorder( UNSELECTED_BORDER );
		}
		
		@Override
		public void paintComponent( Graphics g ) {
			super.paintComponent( g );
		}
	}
	
	private class SemesterPanelListener extends MouseAdapter {
		
		final private SemesterPanel pnlSemester;
		
		public SemesterPanelListener( SemesterPanel pnlSemester ) {
			this.pnlSemester = pnlSemester;
		}
		
		@Override
		public void mousePressed( MouseEvent e ) {
			setSelectedSemester( this.pnlSemester );
		}
	}
	
	private CourseButton createCourseButton( Course c , SemesterPanel semester ) {
		CourseButton rtn = new CourseButton( c );
		rtn.setSize( rtn.getPreferredSize() );
		int y = semester.getSemesterId() * SEMESTER_HEIGHT;
		rtn.setLocation( 10 , y );
		CourseButtonListener l = new CourseButtonListener( rtn );
		rtn.addMouseMotionListener( l );
		rtn.addMouseListener( l );
		return rtn;
	}
	
	private class CourseButton extends JButton {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		final private Course m_course;
		
		final private Font FLAGGED_FONT = new Font( Font.DIALOG , Font.BOLD , 14 );
		final private Color MISSING_PREREQ_COLOR = Color.red;
		final private Color POSSIBLY_MISSING_PREREQ_COLOR = new Color( 225 , 225 , 0 );
		final private Font UNFLAGGED_FONT = new Font( Font.DIALOG , Font.PLAIN , 12 );
		
		final static public int OKAY = 2;
		final static public int POSSIBLY_MISSING_PREREQS = 1;
		final static public int MISSING_PREREQ = 0;
		private int m_status = 0;
		
		public CourseButton( Course c ) {
			this.m_course = c;
			setText( c.m_courseName );
			this.unflagMissingPrereq();
		}
		
		public Course getCourse() {
			return this.m_course;
		}
		
		public ArrayList< Course > getPrereqs() {
			return this.m_course.m_prereqs;
		}
		
		public boolean isPrereqFor( CourseButton other ) {
			return this.m_course.isPrereqFor( other.m_course );
		}
		
		public int getCenterX() {
			return this.getX() + this.getWidth()/2;
		}
		
		public int getCenterY() {
			return this.getY() + this.getHeight()/2;
		}
		
		public boolean arePrereqsOkay() {
			return this.m_status == OKAY;
		}
		
		public int getStatus() {
			return this.m_status;
		}
		
		public void flagMissingPrereq() {
			this.m_status = MISSING_PREREQ;
			setForeground( MISSING_PREREQ_COLOR );
			setFont( FLAGGED_FONT );
		}
		
		public void flagPossiblyMissingPrereq() {
			this.m_status = POSSIBLY_MISSING_PREREQS;
			setForeground( POSSIBLY_MISSING_PREREQ_COLOR );
			setFont( FLAGGED_FONT );
		}
		
		public void unflagMissingPrereq() {
			this.m_status = OKAY;
			setForeground( Color.black );
			setFont( UNFLAGGED_FONT );
		}
	}
	
	private class CourseButtonListener extends MouseAdapter {
		
		final private CourseButton m_courseButton;
		private int m_mousePressedX;
		private int m_mousePressedY;
		
		public CourseButtonListener( CourseButton b ) {
			this.m_courseButton = b;
		}
		
		@Override
		public void mousePressed( MouseEvent e ) {
			this.m_mousePressedX = e.getX();
			this.m_mousePressedY = e.getY();
		}
		
		@Override
		public void mouseDragged( MouseEvent e ) {
			int newX = m_courseButton.getX() + ( e.getX() - m_mousePressedX );
			int newY = m_courseButton.getY() + ( e.getY() - m_mousePressedY );
			m_courseButton.setLocation( newX , newY );
			moveToNearestSemester( m_courseButton );
			Sandbox.this.checkRequirements();
			Sandbox.this.repaint();
		}
		
		@Override
		public void mouseReleased( MouseEvent e ) {
			if ( m_courseButton.getX() + m_courseButton.getWidth() < 10 || 
					m_courseButton.getX() + 20 > Sandbox.this.getWidth() ||
					m_courseButton.getY() + m_courseButton.getHeight() < 0 ) {
				deleteCourse( m_courseButton );
			}
			Sandbox.this.checkRequirements();
			Sandbox.this.repaint();
		}
	}
}
