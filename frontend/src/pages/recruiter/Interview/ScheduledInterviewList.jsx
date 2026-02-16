import { useEffect, useState } from "react";

function getInterviewState(date, startTime, endTime) {
  const start = new Date(`${date}T${startTime}`);
  const end = new Date(`${date}T${endTime}`);
  const now = new Date();

  if (now >= start && now < end) {
    return { status: "LIVE" };
  }

  if (now < start) {
    const diff = start - now;
    const totalSeconds = Math.floor(diff / 1000);

    const minutes = Math.floor(totalSeconds / 60);
    const seconds = totalSeconds % 60;
    const hours = Math.floor(minutes / 60);

    return {
      status: "UPCOMING",
      hours,
      minutes: minutes % 60,
      seconds,
      totalMinutes: Math.floor(diff / 60000),
    };
  }

  // After end time but NOT completed in backend
  return { status: "WAITING_FEEDBACK" };
}

export default function ScheduledInterviewList({ interviews = [] }) {
  const [stateMap, setStateMap] = useState({});

  useEffect(() => {
    function update() {
      const updated = {};

      interviews.forEach((intv) => {
        updated[intv.interviewId] = getInterviewState(
          intv.slotDate,
          intv.startTime,
          intv.endTime
        );
      });

      setStateMap(updated);
    }

    update(); // run immediately
    const interval = setInterval(update, 1000); // 🔥 every second

    return () => clearInterval(interval);
  }, [interviews]);

  return (
    <div className="scheduled-list">
      {interviews.map((intv) => {
        const state = stateMap[intv.interviewId];

        const isJoinDisabled =
          !intv.meetingUrl ||
          !state ||
          (state.status === "UPCOMING" && state.totalMinutes > 5);

        const isCompleted = intv.status === "COMPLETED";

        return (
          <div
            key={intv.interviewId}
            className={`scheduled-card premium ${
              isCompleted ? "completed-card" : ""
            }`}
          >
            {/* LEFT */}
            <div className="scheduled-left">
              <div className="avatar">👤</div>
              <div>
                <h4>{intv.candidateName}</h4>
                <div className="email">{intv.candidateEmail}</div>
                <div className="job-title">{intv.jobTitle}</div>
                <div className="round-name">{intv.roundName}</div>
              </div>
            </div>

            {/* CENTER */}
            <div className="scheduled-center">
              <div className="interviewer-name">
                Interviewer: <strong>{intv.interviewerName}</strong>
              </div>

              <div className="datetime-row">
                {intv.slotDate} • {intv.startTime} – {intv.endTime}
              </div>

              <div className="badge-row">
                <span
                  className={`badge ${isCompleted ? "completed" : "scheduled"}`}
                >
                  {isCompleted
                    ? intv.attendanceStatus === "NO_SHOW"
                      ? "No Show"
                      : "Completed"
                    : "Scheduled"}
                </span>

                {state?.status === "UPCOMING" && (
                  <span className="badge countdown">
                    {state.hours > 0 && `${state.hours}h `}
                    {state.minutes}m {state.seconds}s
                  </span>
                )}

                {state?.status === "LIVE" && (
                  <span className="badge live pulse">Live Now</span>
                )}
              </div>
            </div>

            {/* RIGHT */}
            <div className="scheduled-right">
              {(() => {
                let buttonText = "Join Meet";

                if (isCompleted) {
                  buttonText = intv.feedbackSubmitted
                    ? "Completed"
                    : "Feedback Pending";
                } else if (state?.status === "LIVE") {
                  buttonText = "Join Now";
                } else if (state?.status === "WAITING_FEEDBACK") {
                  buttonText = "Awaiting Attendance";
                }

                return (
                  <a
                    href={intv.meetingUrl || "#"}
                    target="_blank"
                    rel="noreferrer"
                    className={`join-btn ${isJoinDisabled ? "disabled" : ""}`}
                    onClick={(e) => {
                      if (isJoinDisabled) e.preventDefault();
                    }}
                  >
                    {buttonText}
                  </a>
                );
              })()}
            </div>
          </div>
        );
      })}
    </div>
  );
}
