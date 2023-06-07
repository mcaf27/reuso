import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class VotingStrategy {
    public Map<Integer, President> presidentCandidates = new HashMap<Integer, President>();
    public Map<String, FederalDeputy> federalDeputyCandidates = new HashMap<String, FederalDeputy>();

    protected int nullPresidentVotes;
    protected int nullFederalDeputyVotes;
    protected int presidentProtestVotes;
    protected int federalDeputyProtestVotes;
    public boolean secondTurn;

    public abstract boolean computeVote(
        Election election, ArrayList<Candidate> candidates, Voter voter
    );
    
    public abstract boolean computeNullVote(Election election, Voter voter, String type);
    public abstract boolean computeProtestVote(Election election, Voter voter, String type);
    public abstract String getResults();

    public String results(
        int totalVotesP,
        int totalVotesFD,
        List<Candidate> sortedPresidentRank,
        List<Candidate> sortedFederalDeputyRank
    ) {
        var decimalFormater = new DecimalFormat("0.00");
        var builder = new StringBuilder();
        builder.append("Resultado da eleicao:\n");

        builder.append("  Votos presidente:\n");
        builder.append("  Total: " + totalVotesP + "\n");
        builder.append("  Votos nulos: " + nullPresidentVotes + " ("
            + decimalFormater.format((double) nullPresidentVotes / (double) totalVotesFD * 100) + "%)\n");
        builder.append("  Votos brancos: " + presidentProtestVotes + " ("
            + decimalFormater.format((double) presidentProtestVotes / (double) totalVotesFD * 100) + "%)\n");
        builder.append("\tNumero - Partido - Nome  - Votos  - % dos votos totais\n");
        
        for (Candidate candidate : sortedPresidentRank) {
          builder.append("\t" + candidate.number + " - " + candidate.party + " - " + candidate.name + " - "
              + candidate.numVotes + " - "
              + decimalFormater.format((double) candidate.numVotes / (double) totalVotesP * 100)
              + "%\n");
        }

        Candidate electPresident = sortedPresidentRank.get(0);
        builder.append("\n\n  Presidente eleito:\n");

		double presidentPercentage = (double) electPresident.numVotes / (double) totalVotesP * 100;
		double nullFederalDeputyPercentage = (double) nullFederalDeputyVotes / (double) totalVotesFD * 100;
		double federalDeputyProtestPercentage = (double) federalDeputyProtestVotes / (double) totalVotesFD * 100;

		builder.append("\n\n  Presidente eleito:\n");
		builder.append("  " + electPresident.name + " do " + electPresident.party + " com "
			+ decimalFormater.format(presidentPercentage) + "% dos votos\n");

		if (presidentPercentage >= 50) {
			builder.append("A elei??o foi decidida no primeiro turno!");
		} else {
			builder.append("Elei??o ser? decidida no segundo turno!");
		}

		builder.append("\n=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=\n\n");

		builder.append("\n\n  Votos deputado federal:\n");
		builder.append("  Votos nulos: " + nullFederalDeputyVotes + " ("
			+ decimalFormater.format(nullFederalDeputyPercentage) + "%)\n");
		builder.append("  Votos brancos: " + federalDeputyProtestVotes + " ("
			+ decimalFormater.format(federalDeputyProtestPercentage) + "%)\n");
		builder.append("  Total: " + totalVotesFD + "\n");
		builder.append("\tNumero - Partido - Nome - Votos - % dos votos totais\n");
		for (Candidate candidate : sortedFederalDeputyRank) {
			double candidatePercentage = (double) candidate.numVotes / (double) totalVotesFD * 100;
			builder.append(
				"\t" + candidate.number + " - " + candidate.party + " - " + candidate.name + " - "
				+ candidate.numVotes + " - "
				+ decimalFormater.format(candidatePercentage)
				+ "%\n");
		}

        Candidate firstDeputy = sortedFederalDeputyRank.get(0);
        Candidate secondDeputy = sortedFederalDeputyRank.get(1);

		double firstDeputyPercentage = (double) firstDeputy.numVotes / (double) totalVotesFD * 100;
		double secondDeputyPercentage = (double) secondDeputy.numVotes / (double) totalVotesFD * 100;

		builder.append("\n\n  Deputados eleitos:\n");
		builder.append("  1? " + firstDeputy.name + " do " + firstDeputy.party + " com "
			+ decimalFormater.format(firstDeputyPercentage) + "% dos votos\n");
		builder.append("  2? " + secondDeputy.name + " do " + secondDeputy.party + " com "
			+ decimalFormater.format(secondDeputyPercentage) + "% dos votos\n");

		return builder.toString();
    }
}
