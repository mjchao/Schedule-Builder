import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.Border;


public class ReqsPanel extends JPanel {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	CoursesPanel pnlCourses;
	private GraphicList< Req , ReqDisplay > pnlList;
	
	private CommandsPanel pnlParent;
	
	public ReqsPanel() {
		setLayout( new BorderLayout() );
		this.pnlList = new GraphicList< Req , ReqDisplay >( 20 );
		this.add( this.pnlList , BorderLayout.CENTER );
	}
	
	public void setCommandsTab( CommandsPanel ct ) {
		this.pnlParent = ct;
	}
	
	public void setCoursesPanel( CoursesPanel courses ) {
		this.pnlCourses = courses;
	}

	public void addReqs( Req... reqs ) {
		for ( Req r : reqs ) {
			createReq( r );
		}
	}
	
	public void createReq( Req req ) {
		ReqDisplay newReq = new ReqDisplay( req );
		ReqDisplayListener l = new ReqDisplayListener( newReq );
		newReq.addMouseListener( l );
		newReq.addMouseMotionListener( l );
		this.pnlList.addElement( req , newReq );
	}

	static Comparator< Req > reqComparator = new Comparator< Req >() {
		
		final public int COURSE_REQ_PRIORITY = 1;
		final public int OTHER_PRIORITY = 99999;
		
		public int getPriority( Req o1 ) {
			if ( o1 instanceof ReqCourse ) {
				return COURSE_REQ_PRIORITY;
			}
			return OTHER_PRIORITY;
		}
		
		@Override
		public int compare(Req o1, Req o2) {
			int p1 = getPriority( o1 );
			int p2 = getPriority( o2 );
			if ( p1 == p2 ) {
				return o1.getDisplay().compareTo( o2.getDisplay() );
			}
			else {
				return new Integer( p1 ).compareTo( new Integer( p2 ) );
			}
		}
		
	};
	
	public void refreshCompletedReqs( ArrayList< Course > completedCoursework , ArrayList< Course > possiblyCompleteCoursework ) {
		ArrayList< Req > allReqs = this.pnlList.getData();
		Collections.sort( allReqs , reqComparator );
		for ( Req r : allReqs ) {
			if ( r instanceof ReqCourse || r instanceof ReqCourses ) {
				ReqDisplay disp = this.pnlList.getGraphics( r );
				disp.markAsIncomplete();
				for ( Course c : completedCoursework ) {
					if ( r.satisfiesReq( c ) ) {
						completedCoursework.remove( c );
						disp.markAsComplete();
						break;
					}
				}
				for ( Course c : possiblyCompleteCoursework ) {
					if ( r.satisfiesReq( c ) ) {
						possiblyCompleteCoursework.remove( c );
						disp.markAsPossiblyComplete();
						break;
					}
				}
			}
			//TODO other types of reqs
		}
	}
	
	public void markAsComplete( Req r ) {
		this.pnlList.getGraphics( r ).markAsComplete();
	}
	
	public void markAsPossiblyComplete( Req r ) {
		this.pnlList.getGraphics( r ).markAsPossiblyComplete();
	}
	
	public void markAsIncomplete( Req r ) {
		this.pnlList.getGraphics( r ).markAsIncomplete();
	}
	
	private class ReqDisplay extends JPanel {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		final private Req m_req;
	
		final private JLabel lblComplete;
		final private JLabel lblDisplay;
		
		final public int COMPLETE = 2;
		final public int POSSIBLY_COMPLETE = 1;
		final public int INCOMPLETE = 0;
		private int m_status = INCOMPLETE;
	
		
		final private Border FOCUS_BORDER = BorderFactory.createRaisedBevelBorder();
		final private Border LOST_FOCUS_BORDER = BorderFactory.createEmptyBorder();
		
		final private Color COMPLETE_COLOR = new Color( 100 , 200 , 0 );
		final private Color POSSIBLY_COMPLETE_COLOR = Color.gray;
		
		public ReqDisplay( Req r ) {
			this.m_req = r;
			this.setLayout( new BorderLayout() );
			
			JPanel pnlCenter = new JPanel( new FlowLayout( FlowLayout.LEFT ) );
			pnlCenter.setOpaque( false );
			
			Font f = new Font( "Dialog" , Font.PLAIN , 12 );
			this.lblComplete = new JLabel( "\u2713" );
			this.lblComplete.setFont( f );
			this.lblComplete.setOpaque( false );
			pnlCenter.add( this.lblComplete );

			this.lblDisplay = new JLabel( this.m_req.getDisplay() , SwingConstants.LEFT );
			this.lblDisplay.setFont( f );
			pnlCenter.add( this.lblDisplay );
			this.lblDisplay.setOpaque( false );
			add( pnlCenter , BorderLayout.CENTER );
			
			this.setOpaque( true );
			markAsIncomplete();
			
			this.setCursor( new Cursor( Cursor.HAND_CURSOR ) );
		}
		
		public void setGainedFocus() {
			//this.setBorder( FOCUS_BORDER );
			this.lblDisplay.setText( "<html><u>" + this.m_req.getDisplay() + "</u></html>" );
			
			Color fg = Color.blue;
			this.lblDisplay.setForeground( fg );
			
			Color bg = Color.lightGray;
			this.setBackground( bg );
		}
		
		public void setLostFocus() {
			//this.setBorder( LOST_FOCUS_BORDER );
			this.lblDisplay.setText( this.m_req.getDisplay() );
			
			Color fg = Color.black;
			this.lblDisplay.setForeground( fg );
			
			Color bg = UIManager.getLookAndFeelDefaults().getColor( "Label.background" );
			this.setBackground( bg );
		}
		
		public Req getReq() {
			return this.m_req;
		}
		
		public void markAsComplete() {
			this.m_status = COMPLETE;
			this.lblComplete.setText( "\u2713" );
			this.lblComplete.setForeground( COMPLETE_COLOR );
		}
		
		public void markAsPossiblyComplete() {
			this.m_status = POSSIBLY_COMPLETE;
			this.lblComplete.setText( "?" );
			this.lblComplete.setForeground( POSSIBLY_COMPLETE_COLOR );
		}
		
		public void markAsIncomplete() {
			this.m_status = INCOMPLETE;
			this.lblComplete.setText( " " );
		}
	}
	
	private class ReqDisplayListener extends MouseAdapter {
		
		final private ReqDisplay m_reqDisp;
		
		public ReqDisplayListener( ReqDisplay disp ) {
			this.m_reqDisp = disp;
		}
		
		@Override
		public void mouseMoved( MouseEvent e ) {
			this.m_reqDisp.setGainedFocus();
		}
		
		@Override
		public void mouseExited( MouseEvent e ) {
			this.m_reqDisp.setLostFocus();
		}
		
		@Override
		public void mousePressed( MouseEvent e ) {
			ReqsPanel.this.pnlCourses.loadReq( this.m_reqDisp.getReq() );
			pnlParent.showCourseTab();
		}
	}
}
