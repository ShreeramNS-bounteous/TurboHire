import { useEffect, useState } from "react";
import Navbar from "../../components/Navbar";
import InterviewerTabs from "./InterviewerTabs";
import Availability from "./Availability";
import ScheduledTab from "./ScheduledTab";
import CompletedTab from "./CompletedTab";
import EvaluationModal from "../../components/EvaluationModel";

import {
  getMyInterviews,
  submitInterviewFeedback,
  getMyInterviewerProfile
} from "../../api/interviewer.api";

import toast from "react-hot-toast";
import { useAuth } from "../../auth/AuthContext";

export default function InterviewerDashboard() {

  const { user } = useAuth();

  const [activeTab, setActiveTab] = useState("AVAILABILITY");
  const [interviews, setInterviews] = useState([]);
  const [loading, setLoading] = useState(true);
  const [interviewer, setInterviewer] = useState(null);

  const [isModalOpen, setIsModalOpen] = useState(false);
  const [selectedCandidate, setSelectedCandidate] = useState(null);

  /* ================= LOAD PROFILE ================= */
  const loadProfile = async () => {
    try {
      const profile = await getMyInterviewerProfile();
      setInterviewer(profile);
    } catch (err) {
      console.error("Failed to load profile", err);
    }
  };

  /* ================= LOAD INTERVIEWS ================= */
  const loadInterviews = async () => {
    try {
      setLoading(true);
      const data = await getMyInterviews();
      setInterviews(data || []);
    } catch (err) {
      console.error("Failed to fetch interviews", err);
      toast.error("Failed to load interviews");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    if (!user?.userId) return;
    loadProfile();
    loadInterviews();
  }, [user?.userId]);

  /* ================= FILTER ================= */
  const scheduled = interviews.filter(
    (i) => i.status?.trim()?.toUpperCase() === "SCHEDULED"
  );

  const completed = interviews.filter(
    (i) => i.status?.trim()?.toUpperCase() === "COMPLETED"
  );

  /* ================= FEEDBACK ================= */
  const handleOpenFeedback = (candidate) => {
    setSelectedCandidate(candidate);
    setIsModalOpen(true);
  };

  const handleSaveFeedback = async (payload) => {
    try {
      await submitInterviewFeedback(payload.interviewId, {
        rating: payload.rating,
        recommendation: payload.recommendation,
        comments: payload.comments,
      });

      toast.success("Feedback submitted successfully ✅");

      setIsModalOpen(false);
      setSelectedCandidate(null);
      await loadInterviews();

    } catch (err) {
      toast.error("Failed to submit feedback");
    }
  };

  return (
    <>
      <Navbar interviewer={interviewer} onOpenFilters={() => {}} />

      <InterviewerTabs
        activeTab={activeTab}
        onChange={setActiveTab}
        counts={{
          SCHEDULED: scheduled.length,
          COMPLETED: completed.length,
        }}
      />

      <div className="tab-content-wrapper">
        <div key={activeTab} className="tab-content">

          {activeTab === "AVAILABILITY" && <Availability />}

          {activeTab === "SCHEDULED" && (
            <ScheduledTab
              interviews={scheduled}
              loading={loading}
            />
          )}

          {activeTab === "COMPLETED" && (
            <CompletedTab
              interviews={completed}
              loading={loading}
            />
          )}

        </div>
      </div>

      <EvaluationModal
        isOpen={isModalOpen}
        onClose={() => setIsModalOpen(false)}
        candidate={selectedCandidate}
        onSave={handleSaveFeedback}
      />
    </>
  );
}
