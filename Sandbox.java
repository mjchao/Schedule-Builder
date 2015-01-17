import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;


public class Sandbox extends JPanel {

	final public static int SEMESTER_WIDTH = 500;
	final public static int SEMESTER_HEIGHT = 100;
	final public static int NUM_SEMESTERS = 8;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private ArrayList< CourseButton > m_courses = new ArrayList< CourseButton >();

	public Sandbox() {
		setLayout( null );
		setPreferredSize( new Dimension( SEMESTER_WIDTH , SEMESTER_HEIGHT*NUM_SEMESTERS ) );
		for ( int i=0 ; i<NUM_SEMESTERS ; i++ ) {
			addSemester( i );
		}
	}
	
	public void addSemester( int semesterId ) {
		SemesterPanel newSemester = new SemesterPanel( semesterId );
		newSemester.setLocation(0 , SEMESTER_HEIGHT*semesterId );
		newSemester.setSize( SEMESTER_WIDTH , SEMESTER_HEIGHT );
		add( newSemester );
		Sandbox.this.setComponentZOrder( newSemester , semesterId );
	}
	
	public void addCourse( Course c ) {
		CourseButton newCourse = createCourseButton( c );
		add( newCourse );
		Sandbox.this.setComponentZOrder( newCourse , 0 );
		m_courses.add( newCourse );
		revalidate();
		repaint();
	}
	
	public void deleteCourse( CourseButton c ) {
		m_courses.remove( c );
		remove( c );
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
	
	@Override
	public void paintComponent( Graphics g ) {
		super.paintComponent( g );
		drawDependencies( g );
	}
	
	private class SemesterPanel extends JPanel {
		
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
		
		@Override
		public void paintComponent( Graphics g ) {
			super.paintComponent( g );
			Graphics2D g2d = (Graphics2D) g;
			g2d.setStroke( new BasicStroke( 3 ) );
			g2d.drawRect( 0 , 0 , getWidth(), getHeight() );
		}
	}
	
	private CourseButton createCourseButton( Course c ) {
		CourseButton rtn = new CourseButton( c );
		rtn.setSize( rtn.getPreferredSize() );
		rtn.setLocation( 0 , 0 );
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
		
		public CourseButton( Course c ) {
			this.m_course = c;
			setText( c.m_courseName );
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
			Sandbox.this.repaint();
			if ( newX + m_courseButton.getWidth() < 0 || 
					newX - m_courseButton.getHeight() > m_courseButton.getParent().getWidth() ||
					newY + m_courseButton.getHeight() < 0 ) {
				deleteCourse( m_courseButton );
			}
		}
	}
}
