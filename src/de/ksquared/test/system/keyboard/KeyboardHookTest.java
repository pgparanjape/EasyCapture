package de.ksquared.test.system.keyboard;

import java.awt.EventQueue;
import java.awt.SystemTray;
import java.io.File;

import de.ksquared.system.keyboard.GlobalKeyListener;
import de.ksquared.system.keyboard.KeyAdapter;
import de.ksquared.system.keyboard.KeyEvent;

public class KeyboardHookTest {

	public static void main(String[] args) {

		Runnable runner = new Runnable() {
			public void run() {
				if (SystemTray.isSupported()) {
					GrabAndMenu.createTray();
				} else {
					//System.err.println("Tray unavailable");
				}
			}
		};
		EventQueue.invokeLater(runner);
		//System.out.println("Application started");
		try {			
			(new File(Constants.IMAGE_FILE_PATH)).mkdirs();
		} catch (Exception e) {	}

		new GlobalKeyListener().addKeyListener(new KeyAdapter() {
			@Override public void keyPressed(KeyEvent event) {
				//System.out.println(event);
				boolean isControlKeypress = event.isCtrlPressed();
				boolean isAlterKeypress = event.isAltPressed();
				boolean isShiftKeypress = event.isShiftPressed();

				//Grab Screen and send Mail
				if(isShiftKeypress && isControlKeypress){
					if(event.getVirtualKeyCode() == 45) GrabAndMenu.printScreenAndSendMail();
				}
				if (isControlKeypress && !isShiftKeypress && !isAlterKeypress) {
					if(event.getVirtualKeyCode() == 45)	GrabAndMenu.printScreenAndSaveToExcel();					
				}
				if(isAlterKeypress && !isControlKeypress && !isShiftKeypress){
					if (event.getVirtualKeyCode() == 46) GrabAndMenu.createNewExcelFile();
				}
				if(isShiftKeypress && !isAlterKeypress && !isControlKeypress){
					if (event.getVirtualKeyCode() == 19){
						Constants.myControl.AddToFrame(Constants.EXIT_MESSAGE, true);
						Constants.iAmAlive= false;
					}
				}
			}
				@Override public void keyReleased(KeyEvent event) {
				
					//System.out.println(event);
					System.out.println(event.getVirtualKeyCode());
					//System.out.println(event.VK_X);
			}
		});
		while(Constants.iAmAlive)
			GrabAndMenu.customWait(100);
	}

}
