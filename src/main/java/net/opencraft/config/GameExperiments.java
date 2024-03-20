package net.opencraft.config;

/**
 * <h1>GameExperiments</h1>
 * This class is specially designed for developers.
 * This class contains a lot of experiments that are created
 * for upgrade the player experience or for best performance.
 */
public class GameExperiments {

	private GameExperiments() {
	}
	
	/**
	 * <h2>GameExperiments.PLAY_SOUND_ONE_TIME</h2>
	 * This experiment is used to control the sound checker
	 * system. If activated, the sound manager only will
	 * check the sound if the screen is changed.<br><br>
	 * 
	 * PROS:</br>
	 *  • Best FPS
	 *  
	 * <br><br>
	 * <b>Going to be implemented soon!</b>
	 */
	public static final boolean PLAY_SOUND_ONCE = true;
	
	/**
	 * <h2>GameExperiments.CLASSIC_LOAD_SCREEN</h2>
	 * This experiment enables the classic load screen of the game
	 * which is a static image.</br></br>
	 * 
	 * PROS:</br>
	 *  • More efficiency
	 * </br></br>
	 * FAILS:</br>
	 *  • It takes so long to quit
	 */
	public static final boolean CLASSIC_LOAD_SCENE = false;
	
	/**
	 * <h2>GameExperiments.SKIP_LOAD_SCREEN</h2>
	 * This experiment skip completly the load screen.<br>
	 * Recommended for developers!
	 * */
	public static final boolean SKIP_LOAD_SCENE = true;
	
}