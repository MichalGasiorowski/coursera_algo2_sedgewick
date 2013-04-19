import java.util.HashMap;
import java.util.Map;

public class BaseballElimination {
	private int numTeams;
	private int vertNum;
	private int betweenTeamVertNum;
	private Map<String, Integer> teamNamesReverse; // int -> TeamName
	private TeamStat[] teamStats;
	private int[][] teamPairVertNum;
	private Map<Integer, TeamPair> teamPairVertNumReverse;
	//private FlowNetwork flowNetwork;
	private TeamStat highestWinTeam;
	
	private class TeamStat {
		private int index;
		private String teamName;
		private int wins;
		private int losses;
		private int remaining;
		private int[] remAgainst;
		private boolean elCalculated;
		private boolean isEliminated;
		private boolean elCertCalculated;
		private Bag<String> elCertificate;
		private TeamStat(int index, String teamName, int wins, int losses, 
				int remaining, int[] remAgainst) {
			this.index = index;
			this.teamName = teamName;
			this.wins = wins;
			this.losses = losses;
			this.remaining = remaining;
			this.remAgainst = remAgainst;
		}
		private TeamStat() {
		}
	}
	
	private class TeamPair {
		int t1;
		int t2;
		public TeamPair(int t1, int t2) { 
			this.t1 = t1; 
			this.t2 = t2; 
		}
	}
	
	public BaseballElimination(String filename) {
		highestWinTeam = new BaseballElimination.TeamStat();
		// create a baseball division from given filename in format specified below
		In in = new In(filename);
		numTeams = Integer.parseInt(in.readLine());
		betweenTeamVertNum = numTeams*(numTeams - 1)/2;
		vertNum = betweenTeamVertNum + numTeams + 2;
		
		teamStats = new TeamStat[numTeams];  
		teamNamesReverse = new HashMap<String, Integer>();
		int[] tempTeamRem;
		int currentTeam = 0;
		String line;
		String[] chopped;
		TeamStat teamStat;
		
		teamPairVertNum = new int[numTeams][numTeams];
		teamPairVertNumReverse = new HashMap<Integer, BaseballElimination.TeamPair>();
		while (currentTeam < numTeams) {
			line = in.readLine();
			chopped = line.trim().split("\\s+");
			/*
			StdOut.println();
			for (int i=0; i<chopped.length; i++) {
				StdOut.print(chopped[i] + "|");
			}
			StdOut.println();
			*/
			teamNamesReverse.put(chopped[0], currentTeam);
			teamStat = new TeamStat();
			tempTeamRem = new int[numTeams];
			teamStat.index = currentTeam;
			teamStat.teamName = chopped[0];
			teamStat.wins = Integer.parseInt(chopped[1]);
			teamStat.losses = Integer.parseInt(chopped[2]);
			teamStat.remaining = Integer.parseInt(chopped[3]);
			for (int i = 0; i < numTeams; i++) {
				tempTeamRem[i] = Integer.parseInt(chopped[i+4]);
			}
			teamStat.remAgainst = tempTeamRem;
			if (teamStat.wins > highestWinTeam.wins)
				highestWinTeam = teamStat;
			teamStats[currentTeam] = teamStat;
			currentTeam++;
		}
		TeamPair tP;
		int teamVertN;
		for (int team1 = 0; team1 < numTeams; team1++) {
			for (int team2 = team1 + 1; team2 < numTeams; team2++) {
				tP = new TeamPair(team1, team2);
				teamVertN = getTeamVertNum(team1, team2);
				teamPairVertNum[team1][team2] = teamVertN;
				teamPairVertNumReverse.put(teamVertN, tP);
			}
		}
			
	}
	
	private int getFinalVertNum() {
		return vertNum - 1;
	}
	
	private int getBeginVertNum() {
		return 0;
	}
	
	private int getTeamVertNum(int team) {
		return 1 + betweenTeamVertNum + team;
	}
	
	private int getTeamVertNum(int team1, int team2) {
		if (team1 > team2) { return getTeamVertNum(team2, team1); }
		return betweenTeamVertNum - (numTeams - team1) * (numTeams - team1 - 1)/2 
				+ team2 - team1; 
	}
	
	public int numberOfTeams() {
		// number of teams
		return numTeams;
	}
	
	public Iterable<String> teams() {
		// all teams
		return teamNamesReverse.keySet();
	}
	
	public int wins(String team) {
		// number of wins for given team
		if (!teamNamesReverse.containsKey(team)) {
			throw new java.lang.IllegalArgumentException();
		}
		return teamStats[teamNamesReverse.get(team)].wins;
	}
	
	public int losses(String team) {
		// number of losses for given team
		if (!teamNamesReverse.containsKey(team)) {
			throw new java.lang.IllegalArgumentException();
		}
		return teamStats[teamNamesReverse.get(team)].losses;
	}
	
	public int remaining(String team) {
		// number of remaining games for given team
		if (!teamNamesReverse.containsKey(team)) {
			throw new java.lang.IllegalArgumentException();
		}
		return teamStats[teamNamesReverse.get(team)].remaining;
	}
	
	public int against(String team1, String team2) {
		// number of remaining games between team1 and team2
		if (!teamNamesReverse.containsKey(team1) || 
				!teamNamesReverse.containsKey(team2)) {
			throw new java.lang.IllegalArgumentException();
		}
		return teamStats[teamNamesReverse.get(team1)]
				.remAgainst[teamNamesReverse.get(team2)];
	}
	
	private boolean isTriviallyEliminated(String team) {
		TeamStat thisTeam = teamStats[teamNamesReverse.get(team)];
		if (thisTeam.wins + thisTeam.remaining < highestWinTeam.wins) {
			return true;
		}
		return false;
	}
	
	public boolean isEliminated(String team) {
		// is given team eliminated?
		if (!teamNamesReverse.containsKey(team)) {
			throw new java.lang.IllegalArgumentException();
		}
		//Trivial elimination
		TeamStat thisTeam = teamStats[teamNamesReverse.get(team)];
		if (thisTeam.elCalculated) {
			return thisTeam.isEliminated;
		}
		
		
		if (isTriviallyEliminated(team)) {
			thisTeam.elCalculated = true;
			thisTeam.isEliminated = true;
			thisTeam.elCertCalculated = true;
			Bag<String> elCert = new Bag<String>();
			elCert.add(highestWinTeam.teamName);
			thisTeam.elCertificate = elCert;
			return true;
		}
		
		FlowNetwork flowNetwork = new FlowNetwork(vertNum);
		int beginVert = getBeginVertNum();
		int finalVert = getFinalVertNum();
		FlowEdge tempEdge;
		TeamStat teamStat1, teamStat2;
		for (int team1 = 0; team1 < numTeams; team1++) {
			teamStat1 = teamStats[team1];
			//StdOut.print(teamStat1 == null);
			tempEdge = new FlowEdge(getTeamVertNum(team1), finalVert, 
					thisTeam.wins + thisTeam.remaining - teamStat1.wins);
			flowNetwork.addEdge(tempEdge);
			for (int team2 = team1 + 1; team2 < numTeams; team2++) {
				teamStat2 = teamStats[team2];
				tempEdge = new FlowEdge(beginVert, teamPairVertNum[team1][team2], 
						teamStat1.remAgainst[team2]);
				flowNetwork.addEdge(tempEdge);
				tempEdge = new FlowEdge(teamPairVertNum[team1][team2], 
						getTeamVertNum(team1), Double.POSITIVE_INFINITY);
				flowNetwork.addEdge(tempEdge);
				tempEdge = new FlowEdge(teamPairVertNum[team1][team2], 
						getTeamVertNum(team2), Double.POSITIVE_INFINITY);
				flowNetwork.addEdge(tempEdge);
			}
		}
		//StdOut.println();
		//StdOut.println();
		//StdOut.print(flowNetwork.toString());
		FordFulkerson ff = new FordFulkerson(flowNetwork, beginVert, finalVert);
		Bag<String> elCert;
		for (FlowEdge edge: flowNetwork.adj(beginVert)) {
			if (edge.capacity() - edge.flow() > 0.001) {
				thisTeam.elCalculated = true;
				thisTeam.isEliminated = true;
				elCert = new Bag<String>();
				for (int i = 0; i < numTeams; i++) {
					if (ff.inCut(getTeamVertNum(i))) 
						elCert.add(teamStats[i].teamName);		
				}
				thisTeam.elCertCalculated = true;
				thisTeam.elCertificate = elCert;
				return true;
			}
				
		}
		//StdOut.println();
		//StdOut.println();
		//StdOut.print(flowNetwork.toString());
		thisTeam.elCalculated = true;
		thisTeam.isEliminated = false;
		thisTeam.elCertCalculated = true;
		thisTeam.elCertificate = null;
		return false;
	}
	
	public Iterable<String> certificateOfElimination(String team) {
		// subset R of teams that eliminates given team; null if not eliminated
		if (!teamNamesReverse.containsKey(team))
			throw new java.lang.IllegalArgumentException();
		TeamStat thisTeam = teamStats[teamNamesReverse.get(team)];
		if (thisTeam.elCertCalculated) {
			return thisTeam.elCertificate;
		}
		isEliminated(team); 
		return thisTeam.elCertificate;
		
	}
	
	
	public static void main(String[] args) {
		
		//BaseballElimination division = new BaseballElimination("data/teams54.txt");
		BaseballElimination division = new BaseballElimination(args[0]);
	    for (String team : division.teams()) {
	        if (division.isEliminated(team)) {
	            StdOut.print(team + " is eliminated by the subset R = { ");
	            for (String t : division.certificateOfElimination(team))
	                StdOut.print(t + " ");
	            StdOut.println("}");
	        }
	        else {
	            StdOut.println(team + " is not eliminated");
	        }
	    }
	}
	
}
