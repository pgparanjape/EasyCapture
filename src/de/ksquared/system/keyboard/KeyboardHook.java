package de.ksquared.system.keyboard;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

class PoolHook extends Thread {
	private KeyboardHook hook;
	private GlobalKeyListener listener;

	PoolHook(GlobalKeyListener listener) {
		this.setDaemon(true);
		this.listener = listener;
	}

	public void run() {
		hook = new KeyboardHook();
		hook.registerHook(listener);
	}
}

class EventProcedure extends Thread {
	private KeyboardHook hook;

	EventProcedure(KeyboardHook hook) {
		this.setDaemon(true);
		this.hook = hook;
	}

	@Override public void run() {
		while(true) {
			if(!hook.buffer.isEmpty()) {
				KeyEvent event = hook.buffer.remove(0);
				GlobalKeyListener listener = event.listener;
				if(event.transitionState)
					listener.keyPressed(event);
				else listener.keyReleased(event);
			} else try { Thread.sleep(10); }	catch(InterruptedException e) { e.printStackTrace(); }
		}
	}
}

class KeyboardHook {	
	private boolean altPressed,shiftPressed,ctrlPressed,extendedKey;

	List<KeyEvent> buffer = Collections.synchronizedList(new LinkedList<KeyEvent>());
	private EventProcedure procedure = new EventProcedure(this);

	public KeyboardHook() { if(Native.load()) procedure.start(); }
	
	void processKey(boolean transitionState,int virtualKeyCode,GlobalKeyListener listener) {
		processControlKeys(transitionState,virtualKeyCode);
		buffer.add(new KeyEvent(this,listener,transitionState,virtualKeyCode,altPressed,shiftPressed,ctrlPressed,extendedKey));
	}

	native void registerHook(GlobalKeyListener listener);
	native void unregisterHook();

	void processControlKeys(boolean transitionState,int virtualKeyCode) {
		switch(virtualKeyCode) {
		case KeyEvent.VK_RWIN: extendedKey = transitionState; break;
		case KeyEvent.VK_RMENU: extendedKey = transitionState;
		case KeyEvent.VK_MENU: case KeyEvent.VK_LMENU:
			altPressed = transitionState;
			break;			
		case KeyEvent.VK_RSHIFT: extendedKey = transitionState;
		case KeyEvent.VK_SHIFT: case KeyEvent.VK_LSHIFT:
			shiftPressed = transitionState;
			break;
		case KeyEvent.VK_RCONTROL: extendedKey = transitionState;
		case KeyEvent.VK_CONTROL: case KeyEvent.VK_LCONTROL:
			ctrlPressed = transitionState;
			break;
		}
	}
}