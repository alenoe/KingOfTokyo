package ch.fhnw.itprojekt.noobsquad.gameLogic;

/**
 * 
 * @author Simon Zahnd
 *
 */

public class Button_Lock_Unlock {

	private boolean pressed;

	public Button_Lock_Unlock() {
		this.pressed = false;
	}

	public void setPressed(boolean pressed) {
		this.pressed = pressed;
	}

	public boolean getPressed() {
		return this.pressed;
	}

}
