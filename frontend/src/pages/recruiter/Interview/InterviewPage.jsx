import { useEffect, useState } from "react";
import InterviewTabs from "./InterviewTabs";
import FiltersPanel from "./FiltersPanel";
import InterviewerCard from "./InterviewerCard";
import PendingInterviewList from "./PendingInterviewList";
import ScheduledInterviewList from "./ScheduledInterviewList";
import BookSlotModal from "./BookSlotModal";
import CompletedInterviewList from "./CompletedInterviewList";
import { useParams } from "react-router-dom";


import { fetchAvailableInterviewers } from "../../../api/interviewer.api";
import {
  bookInterviewSlot,
  fetchPendingInterviews,
  createInterview,
  fetchScheduledInterviews,
  fetchCompletedInterviews,
} from "../../../api/interview.api";

import "./interview.css";

import toast from "react-hot-toast";

export default function InterviewPage() {
  const [activeTab, setActiveTab] = useState("PENDING");

  const [selectedInterview, setSelectedInterview] = useState(null);
  const [interviewers, setInterviewers] = useState([]);
  const [pendingInterviews, setPendingInterviews] = useState([]);
  const [scheduledInterviews, setScheduledInterviews] = useState([]);
  const [completedInterviews, setCompletedInterviews] = useState([]);

  const [filters, setFilters] = useState({
    date: "2026-02-10",
    from: "10:00",
    to: "13:00",
  });

  const [isModalOpen, setIsModalOpen] = useState(false);
  const [selectedSlot, setSelectedSlot] = useState(null);

  const { jobId } = useParams();



  // =====================================
  // LOAD DATA BASED ON TAB
  // =====================================
  useEffect(() => {
    if (activeTab === "PENDING") {
      fetchPendingInterviews(jobId).then(setPendingInterviews);
    }
  
    if (activeTab === "SCHEDULED") {
      fetchScheduledInterviews(jobId).then(setScheduledInterviews);
    }
  
    if (activeTab === "COMPLETED") {
      fetchCompletedInterviews(jobId).then(setCompletedInterviews);
    }
  }, [activeTab, jobId]);
  

  // =====================================
  // SCHEDULE CLICK (NO INTERVIEW CREATED YET)
  // =====================================
  const handleScheduleClick = (candidateJob) => {
    setSelectedInterview({
      candidateJobId: candidateJob.candidateJobId,
      candidateName: candidateJob.candidateName,
    });
  };

  // =====================================
  // FETCH AVAILABILITY
  // =====================================
  const handleSearch = async (newFilters) => {
    setFilters(newFilters);
    const res = await fetchAvailableInterviewers(newFilters);
    setInterviewers(res.data);
  };

  // =====================================
  // OPEN BOOKING MODAL
  // =====================================
  const openBookingModal = (slotId) => {
    setSelectedSlot(slotId);
    setIsModalOpen(true);
  };

  // =====================================
  // CONFIRM BOOKING
  // =====================================
  const confirmBooking = async (slotId, meetingUrl) => {
    if (!selectedInterview) return;

    try {
      const interview = await createInterview(selectedInterview.candidateJobId);

      await bookInterviewSlot(interview.id, {
        interviewerSlotId: slotId,
        meetingUrl,
      });
      
      toast.success("Interview scheduled sucessfully")
      setIsModalOpen(false);
      setSelectedInterview(null);

      const data = await fetchPendingInterviews(jobId);
setPendingInterviews(data);

    } catch (err) {
      toast.error("Error booking slot", err);
    }
  };

  // =====================================
  // LOAD AVAILABILITY WHEN SELECTED
  // =====================================
  useEffect(() => {
    if (selectedInterview) {
      handleSearch(filters);
    }
  }, [selectedInterview]);

  return (
    <div className="interview-page">
      <h1 className="page-title">Interview Module</h1>

      <InterviewTabs
        activeTab={activeTab}
        onChange={(tab) => {
          setActiveTab(tab);
          setSelectedInterview(null);
        }}
      />

      <div className="animated-container">
        {/* =========================
            PENDING TAB PANEL
        ========================== */}
        <div
          className={`panel ${activeTab === "PENDING" ? "visible" : "hidden"}`}
        >
          <div
            className={`pending-switch ${
              selectedInterview ? "slide-left" : ""
            }`}
          >
            {/* INTERVIEW LIST VIEW */}
            <div className="pending-view">
              <PendingInterviewList
                interviews={pendingInterviews}
                onSchedule={handleScheduleClick}
              />
            </div>

            {/* AVAILABILITY VIEW */}
            <div className="availability-view">
              {selectedInterview && (
                <div className="interview-layout">
                  <FiltersPanel filters={filters} onSearch={handleSearch} />

                  <div className="interview-content">
                    <div className="content-header">
                      <h3 className="section-title">
                        Schedule Interview – {selectedInterview.candidateName}
                      </h3>
                      <span className="meta-text">
                        {interviewers.length} Interviewers Available
                      </span>
                    </div>

                    <div className="interviewer-grid">
                      {interviewers.map((itv) => (
                        <InterviewerCard
                          key={itv.userId}
                          interviewer={itv}
                          onBook={openBookingModal}
                        />
                      ))}
                    </div>
                  </div>
                </div>
              )}
            </div>
          </div>
        </div>

        {/* =========================
            SCHEDULED TAB PANEL
        ========================== */}
        <div
          className={`panel ${
            activeTab === "SCHEDULED" ? "visible" : "hidden"
          }`}
        >
          <ScheduledInterviewList interviews={scheduledInterviews} />
        </div>

        {/* =========================
            COMPLETED TAB PANEL
        ========================== */}
        <div
          className={`panel ${
            activeTab === "COMPLETED" ? "visible" : "hidden"
          }`}
        >
          <CompletedInterviewList
            interviews={completedInterviews}
            onActionComplete={async () => {
              const updatedCompleted = await fetchCompletedInterviews(jobId);
              const updatedPending = await fetchPendingInterviews(jobId);
            

              setCompletedInterviews(updatedCompleted);
              setPendingInterviews(updatedPending);
            }}
          />
        </div>
      </div>

      {/* =========================
          BOOK SLOT MODAL
      ========================== */}
      <BookSlotModal
        isOpen={isModalOpen}
        slotId={selectedSlot}
        onClose={() => setIsModalOpen(false)}
        onConfirm={confirmBooking}
      />
    </div>
  );
}
