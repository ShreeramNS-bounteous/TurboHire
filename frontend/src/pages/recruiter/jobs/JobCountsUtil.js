export function buildJobCounts({
    allCandidates = [],
    pipeline = [],
    rounds = [],
    globalPipelineCandidates = new Set(),
  }) {
    const totalCandidates = allCandidates.length;
  
    const pool = totalCandidates - globalPipelineCandidates.size;
  
    const countByStage = (stage) =>
      pipeline.filter((c) => c.currentStage === stage).length;
  
    const roundCounts = rounds.map((r) => ({
      name: r.roundName,
      count: countByStage(r.roundName),
    }));
  
    return {
      pool,
      shortlisted: countByStage("SHORTLISTED"),
      rounds: roundCounts,
      offer: countByStage("OFFER"),
      hired: countByStage("HIRED"),
      rejected: countByStage("REJECTED")
    };
  }
  