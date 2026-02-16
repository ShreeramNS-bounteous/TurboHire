import { useState } from "react";
import {
  moveToNextRound,
  hireCandidate,
  rejectCandidate,
} from "../../../api/interview.api";
import toast from "react-hot-toast";
 
export default function CompletedDecisionPanel({
  interview,
  onActionComplete,
}) {
  const [loading, setLoading] = useState(false);
 
  if (!interview) return null;
 
  // =========================
  // SAFE BOOLEAN HANDLING
  // =========================
  const isFeedbackMissing = interview.feedbackSubmitted === false;
 
  const decisionTaken =
    interview.decisionStatus &&
    interview.decisionStatus !== "PENDING_DECISION";
 
  const hasNextRound = interview.hasNextRound === true;
 
  // =========================
  // BUTTON DISABLE LOGIC
  // =========================
  const disableMoveNext =
    loading ||
    isFeedbackMissing ||
    decisionTaken ||
    !hasNextRound;
 
  const disableHire =
    loading ||
    isFeedbackMissing ||
    decisionTaken ||
    hasNextRound;
 
  const disableReject =
    loading ||
    isFeedbackMissing ||
    decisionTaken;
 
  // =========================
  // ACTION HANDLERS
  // =========================
  const handleMoveNext = async () => {
    try {
      setLoading(true);
        await moveToNextRound(interview.interviewId);
        toast.success("Moved to next round");
      await onActionComplete?.();
    } catch (err) {
      toast.error("Move next failed", err);
    } finally {
      setLoading(false);
    }
  };
 
  const handleHire = async () => {
    try {
      setLoading(true);
        await hireCandidate(interview.interviewId);
        toast.success("Candidate hired successfully 🎉");
      await onActionComplete?.();
    } catch (err) {
      toast.error("Hire failed", err);
    } finally {
      setLoading(false);
    }
  };
 
  const handleReject = async () => {
    try {
      setLoading(true);
        await rejectCandidate(interview.interviewId);
        toast.success("Candidate rejected");
      await onActionComplete?.();
    } catch (err) {
      toast.error("Reject failed", err);
    } finally {
      setLoading(false);
    }
  };
 
  return (
    <div className="decision-wrapper">
      {/* ========================= HEADER ========================= */}
      <div className="decision-header">
        <div className="avatar-lg">👤</div>
 
        <div className="candidate-meta">
          <h3>{interview.candidateName}</h3>
          <p className="muted">{interview.candidateEmail}</p>
          <p className="job">{interview.jobTitle}</p>
          <p className="round">{interview.roundName}</p>
        </div>
      </div>
 
      {/* ========================= DETAILS ========================= */}
      <div className="info-card">
        <div className="info-row">
          <span>Interviewer</span>
          <strong>{interview.interviewerName}</strong>
        </div>
 
        <div className="info-row">
          <span>Date</span>
          <strong>{interview.slotDate}</strong>
        </div>
 
        <div className="info-row">
          <span>Time</span>
          <strong>
            {interview.startTime} – {interview.endTime}
          </strong>
        </div>
 
        <div className="info-row">
          <span>Attendance</span>
          <span className="badge attended">
            {interview.attendanceStatus}
          </span>
        </div>
 
        <div className="info-row">
          <span>Decision</span>
          <span className="badge success">
            {interview.decisionStatus}
          </span>
        </div>
      </div>
 
      {/* ========================= FEEDBACK ========================= */}
      <div className="feedback-card">
        <h4>Interview Feedback</h4>
 
        {isFeedbackMissing ? (
          <div className="comments muted">
            Feedback not submitted yet.
          </div>
        ) : (
          <>
            <div className="rating-row">
              <span>Rating</span>
              <div>⭐ {interview.rating}/5</div>
            </div>
 
            <div className="recommendation">
              Recommendation:
              <span className="badge recommend">
                {interview.recommendation}
              </span>
            </div>
 
            <div className="comments">
              {interview.comments || "No additional comments."}
            </div>
          </>
        )}
      </div>
 
      {/* ========================= ACTIONS ========================= */}
      <div className="decision-actions">
        <button
          className="btn hire-btn"
          disabled={disableHire}
          onClick={handleHire}
        >
          {loading ? "Processing..." : "Hire Candidate"}
        </button>
 
        <button
          className="btn reject-btn"
          disabled={disableReject}
          onClick={handleReject}
        >
          Reject
        </button>
 
        <button
          className="btn ghost-btn"
          disabled={disableMoveNext}
          onClick={handleMoveNext}
        >
          Move to Next Round
        </button>
      </div>
    </div>
  );
}