package de.ksquared.test.system.keyboard;


import java.awt.*;
import java.awt.image.*;
import javax.imageio.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ddf.EscherClientAnchorRecord;
import org.apache.poi.ddf.EscherRecord;
import org.apache.poi.hssf.record.EscherAggregate;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.util.IOUtils;


public class AddPic {

	ArrayList<Integer> getCol1 = new ArrayList<Integer>(), getRow1 = new ArrayList<Integer>(), getDx1= new ArrayList<Integer>(), getDy1 = new ArrayList<Integer>(); 
	ArrayList<Integer> getCol2 = new ArrayList<Integer>(),getRow2 = new ArrayList<Integer>(), getDx2 = new ArrayList<Integer>(), getDy2 = new ArrayList<Integer>();

	String excelFile, imageFile; 
	

	public AddPic(String excelFile, String imageFile) {
		this.excelFile = excelFile;
		this.imageFile = imageFile;

	}

	public void insertImage(boolean isOldFile) {
		try {
			if(isOldFile) {
				setLastImageLocation();
			} else {
				createNewExcelFile();
				setAnchorDetails();
			}
			addImage();

		} catch (InvalidFormatException | IOException e) {
			//Constants.myControl.AddToFrame("Something wrong with evidence folder..", false);
		}
	}

	private void createNewExcelFile() {

		Workbook wb = new HSSFWorkbook();
		wb.createSheet("sheet");
		try {

			FileOutputStream fileOut = new FileOutputStream(excelFile);
			wb.write(fileOut);
			fileOut.close();

		} catch (Exception e) {
			//Constants.myControl.AddToFrame("Something wrong with evidence folder..", false);
		}


	}

	private void setLastImageLocation() throws InvalidFormatException, FileNotFoundException, IOException {		

		Workbook workbook = WorkbookFactory.create(new FileInputStream(excelFile)); 

		EscherAggregate drawingAggregate = null; 
		HSSFSheet sheet = null; 
		List<EscherRecord> recordList = null; 
		Iterator<EscherRecord> recordIter = null; 
		int numSheets = workbook.getNumberOfSheets(); 
		for(int i = 0; i < numSheets; i++) { 
			//System.out.println("Processing sheet number: " + (i + 1)); 
			sheet = (HSSFSheet) workbook.getSheetAt(i); 
			drawingAggregate = sheet.getDrawingEscherAggregate(); 
			if(drawingAggregate != null) { 
				recordList = drawingAggregate.getEscherRecords(); 
				recordIter = recordList.iterator(); 
				while(recordIter.hasNext()) { 
					this.iterateRecords(recordIter.next(), 1); 
				} 
			} 
		} 
	}

	private void iterateRecords(EscherRecord escherRecord, int level) { 
		List<EscherRecord> recordList = null; 
		Iterator<EscherRecord> recordIter = null; 
		EscherRecord childRecord = null; 
		recordList = escherRecord.getChildRecords(); 
		recordIter = recordList.iterator(); 
		while(recordIter.hasNext()) { 
			childRecord = recordIter.next(); 
			if(childRecord instanceof EscherClientAnchorRecord) { 
				this.setAnchorDetails((EscherClientAnchorRecord)childRecord); 
			} 
			if(childRecord.getChildRecords().size() > 0) { 
				this.iterateRecords(childRecord, ++level); 
			} 
		} 
	} 

	private void setAnchorDetails(EscherClientAnchorRecord anchorRecord) { 

		getCol1.add((int)anchorRecord.getCol1());
		getRow1.add((int)anchorRecord.getRow1());
		getDx1.add((int)anchorRecord.getDx1());
		getDy1.add((int)anchorRecord.getDy1());

		getCol2.add((int)anchorRecord.getCol2());
		getRow2.add((int)anchorRecord.getRow2());
		getDx2.add((int)anchorRecord.getDx2());
		getDy2.add((int)anchorRecord.getDy2());

	} 

	private void setAnchorDetails() {
		getCol1.add(0);
		getRow2.add(0);		
	} 

	private void addImage() throws InvalidFormatException, FileNotFoundException, IOException {

		Workbook workbook = WorkbookFactory.create(new FileInputStream(excelFile));
		HSSFSheet sheet = null; 

		InputStream is = new FileInputStream(imageFile);

		byte[] bytes = IOUtils.toByteArray(is);
		int pictureIdx = workbook.addPicture(bytes, Workbook.PICTURE_TYPE_JPEG);
		is.close();

		CreationHelper helper = workbook.getCreationHelper();
		sheet = (HSSFSheet) workbook.getSheetAt(0);

		// Create the drawing patriarch.  This is the top level container for all shapes. 
		Drawing drawing = sheet.createDrawingPatriarch();

		//add a picture shape
		ClientAnchor anchor = helper.createClientAnchor();
		//set top-left corner of the picture,
		//subsequent call of Picture#resize() will operate relative to it
		anchor.setCol1(getCol1.get(getCol1.size()-1));
		anchor.setRow1(getRow2.get(getRow2.size()-1) + 1);

		org.apache.poi.ss.usermodel.Picture pict = drawing.createPicture(anchor, pictureIdx);

		//auto-size picture relative to its top-left corner
		((org.apache.poi.ss.usermodel.Picture) pict).resize();

		//save workbook
		FileOutputStream output_file =new FileOutputStream(new File(excelFile));
		//write changes
		workbook.write(output_file);
		//close the stream
		output_file.close();       

	} 

	public void grabScreen() {

		try
		{
			//Get the screen size
			// this block is for ALT + Print Screen
			/* Robot robot = new Robot();

	            robot.keyPress(KeyEvent.VK_ALT);
	            robot.keyPress(KeyEvent.VK_PRINTSCREEN);
	            robot.keyRelease(KeyEvent.VK_PRINTSCREEN);
	            robot.keyRelease(KeyEvent.VK_ALT);

	            try {
	                Thread.sleep(1000 * 2);
	            } catch (InterruptedException e) {
	                throw new RuntimeException( e );
	            }

	            Transferable t = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
	            RenderedImage image = (RenderedImage)t.getTransferData(DataFlavor.imageFlavor);*/

			/* boolean isSuccess = ImageIO.write(image, "png", new File("altScreen.png"));

	            System.out.println(isSuccess);
			 */
			Toolkit toolkit = Toolkit.getDefaultToolkit();
			Dimension screenSize = toolkit.getScreenSize();
			Rectangle rect = new Rectangle(0, 0,
					screenSize.width,
					screenSize.height);
			Robot robot = new Robot();
			BufferedImage image = robot.createScreenCapture(rect);			
			//Save the screenshot as a jpg
			File file = new File(imageFile);
			String currentDate = new SimpleDateFormat(Constants.DATE_fORMAT).format(new Date());
			File fileWithDateAndTime = new File(Constants.IMAGE_FILE_PATH_WITHOUT_EXT+currentDate+".jpg");
			ImageIO.write(image, "jpg", file);
			ImageIO.write(image, "jpg", fileWithDateAndTime);
			file.deleteOnExit();
			//System.out.println("Screen Grab done....");			

		}
		catch (Exception e)
		{
			//Constants.myControl.AddToFrame("Something wrong with evidence folder..", false);
		}
	}
}
