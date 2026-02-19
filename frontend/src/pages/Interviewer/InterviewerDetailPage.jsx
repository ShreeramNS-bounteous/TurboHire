import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import Navbar from "../../components/Navbar";
import CandidateCard from "../../components/CandidateCard";
import EvaluationModel from "../../components/EvaluationModel";

import {
  getPreviousRoundFeedback,
  getMyInterviewerProfile,
  getMyInterviews,
  submitInterviewFeedback,
  markInterviewAttendance,
} from "../../api/interviewer.api";

import toast from "react-hot-toast";

export default function InterviewDetailPage() {
  const { id } = useParams();

  const [interview, setInterview] = useState(null);
  const [previousFeedback, setPreviousFeedback] = useState([]);
  const [interviewer, setInterviewer] = useState(null);

  const [isEvaluationOpen, setIsEvaluationOpen] = useState(false);
  const [showAttendanceModal, setShowAttendanceModal] = useState(false);

  const [timeLabel, setTimeLabel] = useState("");

  /* ================= LOAD PROFILE ================= */
  useEffect(() => {
    async function loadProfile() {
      try {
        const profile = await getMyInterviewerProfile();
        setInterviewer(profile);
      } catch {
        toast.error("Failed to load profile");
      }
    }
    loadProfile();
  }, []);

<<<<<<< HEAD
  /* ================= LOAD INTERVIEW FROM DB ================= */
  const loadInterview = async () => {
    try {
      const all = await getMyInterviews();

      const found = all.find(
        (i) => String(i.interviewId) === String(id)
      );
=======
  /* ================= LOAD INTERVIEW ================= */
  const loadInterview = async () => {
    try {
      const all = await getMyInterviews(); // ✅ FIXED

      const found = all.find((i) => String(i.interviewId) === String(id));
>>>>>>> f83d421 (Recovered local changes after accidental .git deletion)

      if (!found) {
        toast.error("Interview not found");
        return;
      }

      setInterview(found);
    } catch {
      toast.error("Failed to load interview");
    }
  };

  useEffect(() => {
    if (id) loadInterview();
  }, [id]);

  /* ================= LOAD PREVIOUS FEEDBACK ================= */
<<<<<<< HEAD
  useEffect(() => {
    async function loadFeedback() {
      try {
        const data = await getPreviousRoundFeedback(id);
        setPreviousFeedback(data || []);
      } catch {
        console.error("Failed loading previous feedback");
      }
    }

    if (id) loadFeedback();
  }, [id]);

  /* ================= DYNAMIC TIMER ================= */
  useEffect(() => {
    if (!interview) return;
  
=======
  const loadFeedback = async () => {
    // ✅ MOVED OUTSIDE
    try {
      const data = await getPreviousRoundFeedback(id);
      setPreviousFeedback(data || []);
    } catch {
      console.error("Failed loading previous feedback");
    }
  };

  useEffect(() => {
    if (id) loadFeedback();
  }, [id]);

  /* ================= TIMER ================= */
  useEffect(() => {
    if (!interview) return;

>>>>>>> f83d421 (Recovered local changes after accidental .git deletion)
    const interval = setInterval(() => {
      const startTime = new Date(
        `${interview.slotDate}T${interview.startTime}`
      );
<<<<<<< HEAD
  
      const now = new Date();
      const diff = startTime - now;
  
      /* ================= PRIORITY LOGIC ================= */
  
=======

      const now = new Date();
      const diff = startTime - now;

>>>>>>> f83d421 (Recovered local changes after accidental .git deletion)
      if (interview.attendanceStatus === "NO_SHOW") {
        setTimeLabel("No Show");
        return;
      }
<<<<<<< HEAD
  
=======

>>>>>>> f83d421 (Recovered local changes after accidental .git deletion)
      if (
        interview.attendanceStatus === "ATTENDED" &&
        !interview.feedbackSubmitted
      ) {
        setTimeLabel("Pending Decision");
        return;
      }
<<<<<<< HEAD
  
      if (interview.feedbackSubmitted) {
        setTimeLabel("Completed");
        return;
      }
  
      if (diff > 0) {
        const hours = Math.floor(diff / (1000 * 60 * 60));
        const minutes = Math.floor((diff / (1000 * 60)) % 60);
  
=======

      if (interview.feedbackSubmitted ) {
        setTimeLabel("Completed");
        return;
      }

      if (diff > 0) {
        const hours = Math.floor(diff / (1000 * 60 * 60));
        const minutes = Math.floor((diff / (1000 * 60)) % 60);

>>>>>>> f83d421 (Recovered local changes after accidental .git deletion)
        if (hours > 0) {
          setTimeLabel(`Starts in ${hours}h ${minutes}m`);
        } else {
          setTimeLabel(`Starts in ${minutes}m`);
        }
      } else {
        setTimeLabel("Interview in progress");
      }
    }, 500);
<<<<<<< HEAD
  
    return () => clearInterval(interval);
  }, [interview]);
  
=======

    return () => clearInterval(interval);
  }, [interview]);
>>>>>>> f83d421 (Recovered local changes after accidental .git deletion)

  /* ================= ATTENDANCE ================= */
  const handleAttendanceSubmit = async (status) => {
    try {
      await markInterviewAttendance(interview.interviewId, status);
<<<<<<< HEAD

      toast.success("Attendance marked");
      setShowAttendanceModal(false);

      await loadInterview(); // 🔥 reload from DB
=======
      toast.success("Attendance marked");
      setShowAttendanceModal(false);
      await loadInterview();
>>>>>>> f83d421 (Recovered local changes after accidental .git deletion)
    } catch {
      toast.error("Failed to mark attendance");
    }
  };

  /* ================= FEEDBACK ================= */
  const handleSaveFeedback = async (payload) => {
    try {
      await submitInterviewFeedback(id, payload);
<<<<<<< HEAD

      toast.success("Feedback submitted");
      setIsEvaluationOpen(false);

      await loadInterview(); // 🔥 reload from DB
=======
      toast.success("Feedback submitted");
      setIsEvaluationOpen(false);

      await loadInterview(); // refresh interview
      await loadFeedback(); // refresh previous feedback
>>>>>>> f83d421 (Recovered local changes after accidental .git deletion)
    } catch {
      toast.error("Feedback submission failed");
    }
  };

  if (!interview) return null;

  const interviewStart = new Date(
    `${interview.slotDate}T${interview.startTime}`
  );

  const canMarkAttendance =
<<<<<<< HEAD
    new Date() >= interviewStart &&
    !interview.attendanceStatus;

  const canEvaluate =
    interview.attendanceStatus === "ATTENDED" &&
    !interview.feedbackSubmitted;
=======
    new Date() >= interviewStart && !interview.attendanceStatus;

  const canEvaluate =
    interview.attendanceStatus === "ATTENDED" && !interview.feedbackSubmitted;
>>>>>>> f83d421 (Recovered local changes after accidental .git deletion)

  return (
    <>
      <Navbar interviewer={interviewer} />

      <div className="px-12 py-10 bg-[#F9FAFB] min-h-screen">
<<<<<<< HEAD

        {/* ================= HEADER ================= */}
        <div className="bg-white rounded-3xl border border-gray-200 shadow-sm p-8 mb-8">
          <div className="grid grid-cols-3 items-center">

            {/* LEFT */}
=======
        {/* ================= HEADER ================= */}
        <div className="bg-white rounded-3xl border border-gray-200 shadow-sm p-8 mb-8">
          <div className="grid grid-cols-3 items-center">
>>>>>>> f83d421 (Recovered local changes after accidental .git deletion)
            <div>
              <h1 className="text-2xl font-bold text-[#101828]">
                {interview.jobTitle}
              </h1>

              <p className="text-sm text-gray-500 mt-2">
<<<<<<< HEAD
                BXA-{String(interview.jobId).padStart(4, "0")} • {interview.roundName}
              </p>

              <p className="text-xs text-gray-400 mt-2">
                {interview.slotDate} | {interview.startTime} – {interview.endTime}
              </p>
            </div>

            {/* CENTER TIMER */}
=======
                BXA-{String(interview.jobId).padStart(4, "0")} •{" "}
                {interview.roundName}
              </p>

              <p className="text-xs text-gray-400 mt-2">
                {interview.slotDate} | {interview.startTime} –{" "}
                {interview.endTime}
              </p>
            </div>

>>>>>>> f83d421 (Recovered local changes after accidental .git deletion)
            <div className="flex justify-center">
              <span className="bg-yellow-100 text-yellow-800 px-4 py-2 rounded-full text-sm font-semibold">
                {timeLabel}
              </span>
            </div>

<<<<<<< HEAD
            {/* RIGHT */}
            <div className="flex flex-col items-end gap-3">

              {interview.status === "SCHEDULED" && canMarkAttendance && (
=======
            <div className="flex flex-col items-end gap-3">
              {interview.status === "SCHEDULED" && (
>>>>>>> f83d421 (Recovered local changes after accidental .git deletion)
                <a
                  href={interview.meetingUrl}
                  target="_blank"
                  rel="noreferrer"
                  className="bg-blue-600 hover:bg-blue-700 text-white px-6 py-2.5 rounded-lg text-sm font-semibold transition"
                >
                  Go To Microsoft Teams Interview
                </a>
              )}

              {canMarkAttendance && (
                <button
                  onClick={() => setShowAttendanceModal(true)}
                  className="px-6 py-2.5 rounded-lg text-sm font-semibold border border-gray-300 hover:bg-gray-50 transition"
                >
                  Candidate Attended Interview?
                </button>
              )}

              {interview.attendanceStatus === "ATTENDED" && (
                <span className="text-green-600 font-semibold text-sm">
                  ✅ Attended
                </span>
              )}

              {interview.attendanceStatus === "NO_SHOW" && (
                <span className="text-red-600 font-semibold text-sm">
                  ❌ No Show
                </span>
              )}
            </div>
          </div>
        </div>

        {/* ================= CANDIDATE CARD ================= */}
        <CandidateCard
          candidate={interview}
<<<<<<< HEAD
          previousFeedback={previousFeedback}
=======
          feedbackToShow={
            interview.feedbackSubmitted
              ? [
                  {
                    rating: interview.rating,
                    recommendation: interview.recommendation,
                    comments: interview.comments,
                    submittedAt: interview.submittedAt,
                    roundName: interview.roundName,
                  },
                ]
              : previousFeedback
          }
>>>>>>> f83d421 (Recovered local changes after accidental .git deletion)
        />

        {/* ================= EVALUATE SECTION ================= */}
        <div className="bg-white rounded-3xl border border-gray-200 shadow-sm p-8 my-8 flex justify-between items-center">
          <h3 className="text-lg font-bold text-[#101828]">
<<<<<<< HEAD
            Evaluate Candidate after the Interview
          </h3>

          <button
            disabled={!canEvaluate}
            onClick={() => setIsEvaluationOpen(true)}
            className={`px-6 py-2 rounded-xl font-semibold text-sm transition ${
              canEvaluate
                ? "bg-blue-600 hover:bg-blue-700 text-white"
                : "bg-gray-200 text-gray-400 cursor-not-allowed"
            }`}
          >
            Evaluate
=======
            {interview.feedbackSubmitted
              ? "Your Evaluation"
              : "Evaluate Candidate after the Interview"}
          </h3>

          <button
            onClick={() => setIsEvaluationOpen(true)}
            className="px-6 py-2 rounded-xl font-semibold text-sm bg-blue-600 hover:bg-blue-700 text-white transition"
          >
            {interview.feedbackSubmitted ? "View / Edit" : "Evaluate"}
>>>>>>> f83d421 (Recovered local changes after accidental .git deletion)
          </button>
        </div>

        {/* ================= BOTTOM INFO GRID ================= */}
        <div className="bg-white rounded-3xl border border-gray-200 shadow-sm p-8 mt-8">
          <div className="grid grid-cols-1 md:grid-cols-4 gap-6">
<<<<<<< HEAD

            <InfoCard
              title="Education"
              value={interview.education?.degree}
            />

=======
            <InfoCard title="Education" value={interview.education?.degree} />
>>>>>>> f83d421 (Recovered local changes after accidental .git deletion)
            <InfoCard
              title="Experience"
              value={`${interview.totalExperience || 0} Years`}
            />
<<<<<<< HEAD

=======
>>>>>>> f83d421 (Recovered local changes after accidental .git deletion)
            <InfoCard
              title="Skills"
              value={interview.skills?.join(", ") || "N/A"}
            />
<<<<<<< HEAD

            <InfoCard
              title="Current Stage"
              value={interview.roundName}
            />

          </div>
        </div>

=======
            <InfoCard title="Current Stage" value={interview.roundName} />
          </div>
        </div>
>>>>>>> f83d421 (Recovered local changes after accidental .git deletion)
      </div>

      {/* ================= ATTENDANCE MODAL ================= */}
      {showAttendanceModal && (
        <div className="fixed inset-0 bg-black/40 flex items-center justify-center z-50">
          <div className="bg-white rounded-2xl p-8 w-[400px] text-center">
            <h3 className="text-lg font-bold mb-4">
              Did the candidate attend the interview?
            </h3>

            <div className="flex justify-center gap-4">
              <button
                onClick={() => handleAttendanceSubmit("ATTENDED")}
                className="bg-green-600 text-white px-6 py-2 rounded-lg"
              >
                Yes
              </button>

              <button
                onClick={() => handleAttendanceSubmit("NO_SHOW")}
                className="bg-red-600 text-white px-6 py-2 rounded-lg"
              >
                No
              </button>
            </div>
          </div>
        </div>
      )}

      <EvaluationModel
        isOpen={isEvaluationOpen}
        onClose={() => setIsEvaluationOpen(false)}
        candidate={interview}
        onSave={handleSaveFeedback}
      />
    </>
  );
}

/* ================= INFO CARD ================= */
function InfoCard({ title, value }) {
  return (
    <div className="bg-[#F9F6F2] rounded-2xl p-6 hover:shadow-md transition">
<<<<<<< HEAD
      <p className="text-xs font-bold text-gray-400 uppercase mb-2">
        {title}
      </p>

=======
      <p className="text-xs font-bold text-gray-400 uppercase mb-2">{title}</p>
>>>>>>> f83d421 (Recovered local changes after accidental .git deletion)
      <p className="text-sm font-semibold text-[#101828]">
        {value || "Not Specified"}
      </p>
    </div>
  );
}
