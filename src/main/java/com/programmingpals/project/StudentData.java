package com.programmingpals.project;

import java.util.ArrayList;

public class StudentData {
	public ArrayList<ChallengeAttempt> ChallengeAttempts = new ArrayList<>();
	public String toString() {
		String dispData = "";
		for(ChallengeAttempt chal : ChallengeAttempts) {
			dispData += "[%" + chal.Score + "] '" + chal.ChallengeName + "'\n";
		}
		return dispData;
	}
}