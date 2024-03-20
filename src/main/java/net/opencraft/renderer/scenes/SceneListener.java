package net.opencraft.renderer.scenes;

import net.opencraft.util.Resource;

public interface SceneListener {

	void onSceneUpdated(Resource scene_res);
	
	void onSceneChanged(Resource scene1, Resource scene2);
	
}
