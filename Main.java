import java.awt.BorderLayout;

import javax.swing.JApplet;
import javax.swing.JScrollPane;


public class Main extends JApplet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Main() {
		setLayout( new BorderLayout() );
		Sandbox pnlSandbox = new Sandbox();
			Course c1 = new Course( "ENGR151" );
			Course c2 = new Course( "EECS280" );
				c2.m_prereqs.add( c1 );
			Course c3 = new Course( "EECS203" );
				c3.m_prereqs.add( c1 );
		pnlSandbox.addCourse( c3 );
		pnlSandbox.addCourse( c2 );
		pnlSandbox.addCourse( c1 );
		pnlSandbox.repaint();
		add( new JScrollPane( pnlSandbox ) );
		setVisible( true );
	}
	
}
