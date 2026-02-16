export default function PendingInterviewList({
    interviews = [],
    onSchedule
  }) {
    return (
      <div className="pending-interview-list">
            {interviews.map((intv) => (
          <div key={intv.candidateJobId} className="pending-card-flex">
            
            {/* LEFT */}
            <div className="pending-left">
              <div className="avatar">👤</div>
              <div>
                <h4>{intv.candidateName}</h4>
                <p>{intv.candidateEmail}</p>
              </div>
            </div>
            {/* MIDDLE */}
            <div className="pending-middle">
              <p className="job-title">{intv.jobTitle}</p>
              <p className="round-name"> JOB-{String(intv.jobId).padStart(4, "0")}</p>
              <span className="badge pending">To Be Scheduled</span>
            </div>
  
            {/* RIGHT */}
            <div className="pending-right">
            <button
  className="schedule-btn"
  onClick={() => onSchedule(intv)}
>
  Schedule
</button>
            </div>
  
          </div>
        ))}
      </div>
    );
  }
  