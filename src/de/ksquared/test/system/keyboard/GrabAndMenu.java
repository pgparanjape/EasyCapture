package de.ksquared.test.system.keyboard;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;


public class GrabAndMenu {

	static void printScreenAndSendMail() {
		AddPic addpic = new AddPic(Constants.EXCEL_FILE, Constants.IMAGE_FILE );
		addpic.grabScreen();
		Runtime rt = Runtime.getRuntime();
		try {
			rt.exec(OutlookInstalled.getOutLookPath() + " /c ipm.note /a " + Constants.IMAGE_FILE);
		} catch (Exception e) {
			//Constants.myControl.AddToFrame("Something wrong with evidence folder..", false);
		}
	}

	static void printScreenAndSaveToExcel() {
		AddPic addpic = new AddPic(Constants.EXCEL_FILE, Constants.IMAGE_FILE );
		addpic.grabScreen();
		final boolean isOldFile = checkForExcelFile();
		addpic.insertImage(isOldFile);
	}
	static void createNewExcelFile() {
		File file = new File(Constants.EXCEL_FILE);
		if(file.exists()){
			String currentDate = new SimpleDateFormat(Constants.DATE_fORMAT).format(new Date());
			File file2 = new File(file.getParentFile().getPath() + "/" + file.getName().replaceFirst(".xls", currentDate) + ".xls"); 
			file.renameTo(file2);
		}		
	}
	private static boolean checkForExcelFile() {
		File f = new File(Constants.EXCEL_FILE);
		if(f.exists()){
			return true;
		}else{
			return false;
		}
	}

	static void createTray() {
		Image image;
		URL url = GrabAndMenu.class.getResource(Constants.TRAY_IMAGE); 
		image = Toolkit.getDefaultToolkit().getImage(url);
		final SystemTray tray = SystemTray.getSystemTray();
		final PopupMenu popup = new PopupMenu();
		final TrayIcon trayIcon = new TrayIcon(image, Constants.MENU_HEADER_NAME_AND_POP_UP_NAME, popup);
		//trayIcon.setImageAutoSize(false);

		/*trayIcon.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 1) {
                	popup.setEnabled(true);
                }
            }
        });*/

		MenuItem item = new MenuItem(Constants.MENU_PRINT_SCREEN);

		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				customWait(300);
				printScreenAndSaveToExcel();
			}
		});
		popup.add(item);

		item = new MenuItem(Constants.MENU_NEW_EXCEL);
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				customWait(300);
				createNewExcelFile();
			}
		});
		popup.add(item);

		item = new MenuItem(Constants.MENU_SEND_MAIL);
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				customWait(300);
				printScreenAndSendMail();
				//trayIcon.displayMessage("New Email", "Screen Grabbed", TrayIcon.MessageType.INFO);
			}
		});
		popup.add(item);

		item = new MenuItem(Constants.MENU_CLOSE);
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tray.remove(trayIcon);
				Constants.myControl.AddToFrame(Constants.EXIT_MESSAGE, true);
				Constants.iAmAlive= false;
				//System.exit(0);
			}
		});
		popup.add(item);
		try {
			tray.add(trayIcon);
		} catch (AWTException e) {
			//System.err.println("Can't add to tray");
		}
	}

	static void customWait(int time){
		try {
			Thread.sleep(time);
		} catch (InterruptedException e1) {	}
	}
}
