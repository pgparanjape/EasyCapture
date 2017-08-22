package de.ksquared.system.keyboard;

import java.util.List;
import java.util.Vector;

public class GlobalKeyListener {
	protected PoolHook hook;
	public GlobalKeyListener() { (hook=new PoolHook(this)).start();	}
	protected List<KeyListener> listeners = new Vector<KeyListener>();

	public void addKeyListener(KeyListener listener) { listeners.add(listener); }
	public void removeKeyListener(KeyListener listener) { listeners.remove(listener); }

	void keyPressed(KeyEvent event) {
		try {
			for(KeyListener listener:listeners)
				listener.keyPressed(event);
		} catch(Exception e) { e.printStackTrace(); }
	}
	void keyReleased(KeyEvent event) {
		try {
			for(KeyListener listener:listeners)
				listener.keyReleased(event);
		} catch(Exception e) { e.printStackTrace(); }
	}
}
