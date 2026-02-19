import React, { useState, useEffect, useRef } from "react";
import { ChevronDown, ChevronUp } from "lucide-react";

const CandidateCard = ({
  candidate,
  feedbackToShow = [],
}) => {
  const [showDropdown, setShowDropdown] = useState(false);
  const dropdownRef = useRef(null);

  // Sort feedback (latest first)
  const sortedFeedback = [...feedbackToShow].sort(
    (a, b) => new Date(b.submittedAt) - new Date(a.submittedAt)
  );

  const latestFeedback = sortedFeedback[0];

  // Detect first round
  const isFirstRound =
    candidate.roundName?.toLowerCase().includes("1") ||
    candidate.roundName?.toLowerCase().includes("round 1");

  // Close when clicking outside
  useEffect(() => {
    function handleClickOutside(event) {
      if (
        dropdownRef.current &&
        !dropdownRef.current.contains(event.target)
      ) {
        setShowDropdown(false);
      }
    }

    document.addEventListener("mousedown", handleClickOutside);
    console.log(candidate)
    console.log(latestFeedback)
    return () =>
      document.removeEventListener("mousedown", handleClickOutside);
  }, []);

 console.log(candidate);

  return (
    <div className="bg-white rounded-2xl shadow-md border border-gray-200 overflow-visible">
      
      {/* ================= TOP JOB BAR ================= */}
      <div className="bg-[#3B4552] text-white px-6 py-3 flex justify-between text-sm font-medium">
        <div className="flex gap-6">
          <span>
            Job Code: <b>BXA-{String(candidate.jobId).padStart(4, "0")}</b>
          </span>
          <span>
            Job Name: <b>{candidate.jobTitle}</b>
          </span>
          <span>
            Department: <b>BU</b>
          </span>
          <span>
            Location: <b>Bangalore</b>
          </span>
        </div>
        
      </div>

      {/* ================= MAIN CONTENT ================= */}
      <div className="p-8 flex justify-between items-center">
        
        {/* ===== LEFT SIDE ===== */}
        <div className="flex gap-6 items-center">
          <div className="h-20 w-20 bg-yellow-500 text-white rounded-full flex items-center justify-center text-2xl font-bold">
            {(candidate.candidateName || "").charAt(0)}
          </div>

          <div>
            <h2 className="text-2xl font-bold text-[#101828]">
              {candidate.candidateName}
            </h2>

            <p className="text-gray-500 mt-1">{candidate.currentCompany }</p>

            <p className="text-gray-500 mt-3 text-sm">
              {candidate.candidateEmail || ""}
            </p>

          </div>
        </div>

        {/* ===== RIGHT SIDE (LATEST EVALUATION) ===== */}
        <div
          className="w-105 relative"
          ref={dropdownRef}
        >
          <div
            className="bg-[#F4EFEA] rounded-2xl p-6 cursor-pointer hover:shadow-md transition"
            onClick={() => {
              if (!isFirstRound && latestFeedback) {
                setShowDropdown(!showDropdown);
              }
            }}
          >
            <div className="flex justify-between items-center">
              <span className="text-sm font-semibold text-gray-600 uppercase">
                Latest Evaluation
              </span>

              {!isFirstRound && latestFeedback && (
                showDropdown ? (
                  <ChevronUp size={18} />
                ) : (
                  <ChevronDown size={18} />
                )
              )}
            </div>

            {/* FIRST ROUND */}
            {isFirstRound && (
              <div className="mt-4 text-gray-500 font-medium">
                Not Evaluated
              </div>
            )}

            {/* SECOND+ ROUND */}
            {!isFirstRound && latestFeedback && (
              <>
                <div className="mt-4 text-4xl font-bold text-blue-600">
                  {latestFeedback.rating}/5
                </div>

                <div className="mt-2 text-gray-700 font-medium">
                  {latestFeedback.recommendation}
                </div>
              </>
            )}
          </div>

          {/* ===== DROPDOWN (ALIGNED BELOW) ===== */}
          {showDropdown && latestFeedback && (
            <div className="absolute left-0 mt-3 w-full bg-white border border-gray-200 shadow-2xl rounded-xl p-5 z-50 animate-fadeIn">

              <p className="text-sm font-semibold mb-2">
                {latestFeedback.roundName}
              </p>

              <div className="space-y-2 text-sm text-gray-700">
              
                <p>
                  <b>Rating:</b> {latestFeedback.rating}/5
                </p>
                <p>
                  <b>Recommendation:</b> {latestFeedback.recommendation}
                </p>
                <p>
                  <b>Comments:</b> {latestFeedback.comments}
                </p>
              </div>

              <p className="text-xs text-gray-400 mt-3">
                Submitted:{" "}
                {new Date(
                  latestFeedback.submittedAt
                ).toLocaleString()}
              </p>
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default CandidateCard;