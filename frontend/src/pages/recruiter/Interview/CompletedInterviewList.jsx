import { useState } from "react";
import RightDrawer from "../../../components/RightDrawer.jsx";
import CompletedDecisionPanel from "./CompletedDecisionPanel.jsx";

export default function CompletedInterviewList({
  interviews = [],
  onActionComplete
}) {
  const [selected, setSelected] = useState(null);

  return (
    <>
      <div className="scheduled-list">
        {interviews.map((intv) => {

          const decisionTaken =
            intv.decisionStatus &&
            intv.decisionStatus !== "PENDING_DECISION";

          return (
            <div
              key={intv.interviewId}
              className={`scheduled-card premium ${
                decisionTaken ? "locked-card" : ""
              }`}
              onClick={() => {
                if (!decisionTaken) {
                  setSelected(intv);
                }
              }}
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
                  <span className="badge completed">
                    {intv.attendanceStatus}
                  </span>

                  <span className="badge success">
                    {intv.decisionStatus}
                  </span>
                </div>
              </div>

              {/* RIGHT */}
              <div className="scheduled-right">
                <button
                  className="join-btn secondary"
                  disabled={decisionTaken}
                >
                  {decisionTaken ? "Decision Locked" : "View Details"}
                </button>
              </div>
            </div>
          );
        })}
      </div>

      <RightDrawer
        open={!!selected}
        onClose={() => setSelected(null)}
        title="Interview Decision"
      >
        {selected && (
          <CompletedDecisionPanel
            interview={selected}
            onActionComplete={async () => {
              setSelected(null);
              await onActionComplete?.();
            }}
          />
        )}
      </RightDrawer>
    </>
  );
}