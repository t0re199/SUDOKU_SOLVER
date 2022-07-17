package io;

import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import documents.ExportableDocument;

public final class FileUtils
{
	public static final String SYSTEM_LINE_SEPARATOR = System.getProperty("line.separator");
	private static JFileChooser FILE_CHOOSER;
	
	private static final int MAX_FILE_NAME_LENGTH = 0x40;
	private static final String[] NOT_AMMISSED_CHARS = {"\\","/",":","?","*","<",">","|","\""};
	private static final String INVALID_FILE_NAME_MESSAGE = "<html><h4>File name cannot contains any of these chars:</h4><center><h3> <  >  \\   |  /  *  ?  :  \"</h3></center></html>";
	private static final String DEFAULT_DIR_PATH = FileUtils.getUserHome() + "\\";
	private static final String DEFAULT_FILE_NAME = "board.xml";
	private static final String FILE_CHOOSER_TITLE = "Select Folder";
	private static final String SUCCESSFULLY_EXPORTED_MESSAGE = "File Successfully Created!";
	
	private FileUtils()
	{	
	}
	
	public static String getUserHome()
	{
		return System.getProperty("user.home");
	}
	
	public static String getFileSeparator()
	{
		return util.OsUtil.osIsWindows() ? "\\" : "/";
	}
	
	
	public static boolean manageFileOnDisk(File file)
	{
		int option = JOptionPane.NO_OPTION;
		if(file.exists())
		{
			option = askAndGetDeleteOption();
			if(option == JOptionPane.YES_OPTION)
			{
				return file.delete();
			}
			else
			{
			
				return false;
			}
		}
		return true;
	}
	
	
	private static int askAndGetDeleteOption() 
	{
		int option = JOptionPane.showConfirmDialog(null, "Override?",
													"Already Existing File",JOptionPane.YES_NO_OPTION);
		return option;
	}
	
	public static void export(ExportableDocument exportableDocument)
	{
		FILE_CHOOSER = new JFileChooser();
		FILE_CHOOSER.setDialogTitle(FILE_CHOOSER_TITLE);
		FILE_CHOOSER.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		FILE_CHOOSER.setCurrentDirectory(new File(getUserHome()));
		int option = FILE_CHOOSER.showOpenDialog(null);
		if(option == JFileChooser.APPROVE_OPTION)
		{
			try
			{
				File file = new File(FILE_CHOOSER.getSelectedFile().getAbsolutePath() + getFileSeparator() + DEFAULT_FILE_NAME);
				if(FileUtils.manageFileOnDisk(file))
				{
					exportableDocument.export(file);
					showExportCompletedDialog();
				}
				else
				{
					JOptionPane.showMessageDialog(null, "Board has not been exported!", "Warning", JOptionPane.WARNING_MESSAGE);
				}
			}
			catch(IOException e)
			{
				JOptionPane.showMessageDialog(null, "Error while writing file on disk.", "Warning", JOptionPane.WARNING_MESSAGE);
			}
		}
	}
	
	
	public static void exportWithName(ExportableDocument exportableDocument)
	{
		FILE_CHOOSER = new JFileChooser();
		FILE_CHOOSER.setDialogTitle(FILE_CHOOSER_TITLE);
		FILE_CHOOSER.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		FILE_CHOOSER.setCurrentDirectory(new File(DEFAULT_DIR_PATH));
		int option = FILE_CHOOSER.showOpenDialog(null);
		if(option == JFileChooser.APPROVE_OPTION)
		{
			String fileName = "";
			boolean flag = false;
			while(!flag)
			{
				fileName = getFileName();
				if(fileName == null || !checkAndAdjustFileName(fileName))
				{
					int defaultNameOption = JOptionPane.showConfirmDialog(null, "<html><h5>Do you want to use default name?</h5>"
							+ "<h4>Click no to type a new file name</h4></html>",
							"Attenzione",JOptionPane.YES_NO_OPTION);
					if(defaultNameOption == JOptionPane.YES_OPTION)
					{
						export(exportableDocument);
						return;
					}
				}
				else
				{
					flag = true;
				}
			}
			try
			{
				File f = new File(FILE_CHOOSER.getSelectedFile().getAbsolutePath() + getFileSeparator() + fileName + ".xml");
				if(FileUtils.manageFileOnDisk(f))
				{
					exportableDocument.export(f);
					showExportCompletedDialog();
				}
			}
			catch(IOException e)
			{
				JOptionPane.showMessageDialog(null, "Error while writing file on disk.", "Warning", JOptionPane.WARNING_MESSAGE);
			}
		}
	}
	
	private static void showExportCompletedDialog()
	{
		JOptionPane.showMessageDialog(null, SUCCESSFULLY_EXPORTED_MESSAGE, 
				"Info", JOptionPane.INFORMATION_MESSAGE);
	}
	
	private static String getFileName()
	{
		return JOptionPane.showInputDialog(null, "Insert file name without extension");
	}

	
	private static boolean checkAndAdjustFileName(String str)
	{
		String fName = str.trim();
		int strLen = fName.length(); 
		if(strLen == 0)
		{
			return false;
		}
		if(strLen > MAX_FILE_NAME_LENGTH)
		{
			fName = fName.substring(0, MAX_FILE_NAME_LENGTH);
		}
		for(String invalid : NOT_AMMISSED_CHARS)
		{
			if(fName.contains(invalid))
			{
				Toolkit.getDefaultToolkit().beep();
				JOptionPane.showMessageDialog(null, INVALID_FILE_NAME_MESSAGE, 
												"Warning", JOptionPane.WARNING_MESSAGE);
				return false;
			}
		}
		return true;
	}
}
