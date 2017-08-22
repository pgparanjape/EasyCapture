package de.ksquared.system.keyboard;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Native {
	private static Boolean loaded = null;

	static boolean load() {
		if(loaded!=null)
			return loaded==Boolean.TRUE;
		try {
			NativeUtils.loadLibraryFromJar("/keyboardhook-win-x86.dll");						
			return (loaded = Boolean.TRUE);
		} catch (IOException e1) {			
			e1.printStackTrace();
		}

		System.setProperty("de.ksquared.system.keyboard.lib.path","D:\\JNA\\KeyboardHook\\lib");
		System.setProperty("de.ksquared.system.keyboard.lib.name","keyboardhook-win-x86.dll");

		String libpath = System.getProperty("de.ksquared.system.keyboard.lib.path"),
				libname = System.getProperty("de.ksquared.system.keyboard.lib.name");
		if(libname==null)
			libname = System.mapLibraryName("keyboardhook");
		try {
			if (libpath == null) {
				System.loadLibrary("keyboardhook");
			} else {
				System.load(new File(libpath, libname).getAbsolutePath());
			}
			return (loaded = Boolean.TRUE);
		} catch (UnsatisfiedLinkError e) {
			e.printStackTrace();
		}

		String osname = System.getProperty("os.name").toLowerCase(),
				osarch = System.getProperty("os.arch");
		if(osname.startsWith("mac os")) { osname = "mac"; osarch = "universal"; }
		else if(osname.startsWith("windows")) osname = "win";
		else if(osname.startsWith("sunos")) osname = "solaris";
		if(osarch.startsWith("i")&&osarch.endsWith("86"))
			osarch = "x86";
		libname = "keyboardhook-"+osname+'-'+osarch+".lib";
		try {
			InputStream input = Native.class.getClassLoader().getResourceAsStream(libname);
			if(input==null)
				throw new Exception("libname: "+libname+" not found");
			File temp = File.createTempFile("keyboardhook-",".lib");
			temp.deleteOnExit();
			OutputStream out = new FileOutputStream(temp);
			byte[] buffer = new byte[1024];
			int read;
			while((read = input.read(buffer))!=-1)
				out.write(buffer,0,read);
			input.close(); out.close();
			System.load(temp.getAbsolutePath());
			return (loaded=Boolean.TRUE);
		} catch(Exception e) { /* do nothing, go on */ }
		return (loaded=Boolean.FALSE);
	}

}