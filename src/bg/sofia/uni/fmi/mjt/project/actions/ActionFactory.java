package bg.sofia.uni.fmi.mjt.project.actions;

import bg.sofia.uni.fmi.mjt.project.users.UserProfile;

public interface ActionFactory {

	boolean processAction(String line);

	void setUserProfile(UserProfile userProfile);
}
