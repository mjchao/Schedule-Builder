import javax.swing.JPanel;


public class CourseInfoPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private CommandsPanel pnlParent;
	
	private Sandbox pnlSandbox;
	
	public CourseInfoPanel() {
		
	}
	
	public void setCommandsTab( CommandsPanel tb ) {
		this.pnlParent = tb;
	}
	
	public void setSandbox( Sandbox pnlSandbox ) {
		this.pnlSandbox = pnlSandbox;
	}
	
	
}
