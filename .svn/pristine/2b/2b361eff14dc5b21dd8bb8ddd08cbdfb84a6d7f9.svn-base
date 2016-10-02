/**
 * @author Ilkan Esiyok
 * @date 2014-04-15
 * @category main class
 * @purpose this is the starting point of the application and it calls the GUI
 *
 */

import java.util.concurrent.ExecutionException;
import javax.swing.SwingUtilities;

public class Start {

	public static void main(String[] args) throws InterruptedException, ExecutionException {

		try {
			final DaisyWorldModel m = new DaisyWorldModel();
			/*
			 * SwingUtilities.invokeLater is a part of concurrency in java
			 * it is used for observing the GUI asynchronously while the process is been executed
			 */
			SwingUtilities.invokeLater(new Runnable() {
				
				@Override
				public void run() {
					Window w = new Window(m);w.show();
				}
			});			
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
	}



}
