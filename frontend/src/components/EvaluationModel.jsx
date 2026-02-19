import React, { useState, useEffect, useRef } from "react";
import {
  X,
  Star,
  Save,
  Download,
  Edit,
} from "lucide-react";
import * as XLSX from "xlsx";

/* ==============================
   TEXTAREA COMPONENT
================================ */
const StableCommentArea = ({ value, onChange, placeholder, disabled }) => {
  return (
    <textarea
      className={`w-full h-20 p-3 text-xs border rounded-lg outline-none resize-none transition-all
        ${
          disabled
            ? "bg-gray-100 border-gray-200 text-gray-500 cursor-not-allowed"
            : "bg-white border-gray-200 focus:border-blue-500"
        }`}
      placeholder={placeholder}
      value={value}
      disabled={disabled}
      onChange={(e) => onChange(e.target.value)}
    />
  );
};

/* ==============================
   🔥 FIXED METRIC ROW (MOVED OUTSIDE)
================================ */
const MetricRow = ({
  title,
  fieldKey,
  scores,
  setScores,
  isViewMode,
}) => {
  return (
    <div className="p-4 bg-gray-50 rounded-xl border space-y-3">
      <div className="flex justify-between items-center">
        <h4 className="text-sm font-bold">{title}</h4>
        <div className="flex gap-1">
          {[1, 2, 3, 4, 5].map((star) => (
            <button
              key={star}
              type="button"
              disabled={isViewMode}
              onClick={() =>
                setScores((prev) => ({
                  ...prev,
                  [fieldKey]: {
                    ...prev[fieldKey],
                    rating: star,
                  },
                }))
              }
            >
              <Star
                size={22}
                fill={
                  scores[fieldKey].rating >= star
                    ? "#fbbf24"
                    : "white"
                }
                stroke={
                  scores[fieldKey].rating >= star
                    ? "#fbbf24"
                    : "#cbd5e1"
                }
              />
            </button>
          ))}
        </div>
      </div>

      <StableCommentArea
        placeholder="Enter feedback..."
        value={scores[fieldKey].comment}
        disabled={isViewMode}
        onChange={(val) =>
          setScores((prev) => ({
            ...prev,
            [fieldKey]: {
              ...prev[fieldKey],
              comment: val,
            },
          }))
        }
      />
    </div>
  );
};

/* ==============================
   MAIN COMPONENT
================================ */

const EvaluationModel = ({ isOpen, onClose, candidate, onSave }) => {
  const isSubmitted = candidate?.feedbackSubmitted === true;
  const hasNextRound = candidate?.hasNextRound === true;

  const [isEditMode, setIsEditMode] = useState(false);
  const [recommendation, setRecommendation] = useState("");
  const initializedRef = useRef(false);

  const [scores, setScores] = useState({
    coreConcepts: { rating: 0, comment: "" },
    coding: { rating: 0, comment: "" },
    communication: { rating: 0, comment: "" },
    behavioral: { rating: 0, comment: "" },
  });

  /* ==============================
     PARSE STORED COMMENTS
  ================================= */
  const parseStoredComments = (text) => {
    if (!text) return null;

    const result = {
      coreConcepts: { rating: 0, comment: "" },
      coding: { rating: 0, comment: "" },
      communication: { rating: 0, comment: "" },
      behavioral: { rating: 0, comment: "" },
    };

    const sections = text.split("\n\n");

    sections.forEach((section) => {
      const lines = section.split("\n");
      if (!lines.length) return;

      const header = lines[0];
      const comment = lines.slice(1).join("\n");

      const ratingMatch = header.match(/\((\d)\/5\)/);
      const rating = ratingMatch ? parseInt(ratingMatch[1]) : 0;

      if (header.includes("CORE CONCEPTS"))
        result.coreConcepts = { rating, comment };

      if (header.includes("CODING"))
        result.coding = { rating, comment };

      if (header.includes("COMMUNICATION"))
        result.communication = { rating, comment };

      if (header.includes("BEHAVIORAL"))
        result.behavioral = { rating, comment };
    });

    return result;
  };

  /* ==============================
     LOAD EXISTING DATA
  ================================= */
  useEffect(() => {
    if (!isOpen) {
      initializedRef.current = false;
      return;
    }

    if (initializedRef.current) return;

    if (isSubmitted && candidate?.comments) {
      setRecommendation(candidate.recommendation || "");
      const parsed = parseStoredComments(candidate.comments);
      if (parsed) setScores(parsed);
      setIsEditMode(false);
    } else {
      setRecommendation("");
      setScores({
        coreConcepts: { rating: 0, comment: "" },
        coding: { rating: 0, comment: "" },
        communication: { rating: 0, comment: "" },
        behavioral: { rating: 0, comment: "" },
      });
    }

    initializedRef.current = true;
  }, [isOpen]);

  /* ==============================
     CALCULATE AVERAGE
  ================================= */
  const calculateAverage = () => {
    let total = 0;
    let count = 0;

    Object.values(scores).forEach((item) => {
      if (item.rating > 0) {
        total += item.rating;
        count++;
      }
    });

    if (count === 0) return 0;
    return Math.round(total / count);
  };

  /* ==============================
     GENERATE TEXT
  ================================= */
  const generatePlainComment = () => {
    let text = "";

    const formatMetric = (label, data) => {
      if (data.rating > 0 || data.comment) {
        text += `${label} (${data.rating}/5)\n`;
        if (data.comment) text += `${data.comment}\n`;
        text += `\n`;
      }
    };

    formatMetric("CORE CONCEPTS", scores.coreConcepts);
    formatMetric("CODING", scores.coding);
    formatMetric("COMMUNICATION", scores.communication);
    formatMetric("BEHAVIORAL", scores.behavioral);

    return text.trim();
  };

  const handleSaveClick = () => {
    if (!recommendation) {
      alert("Please select recommendation");
      return;
    }

    const payload = {
      rating: calculateAverage(),
      recommendation,
      comments: generatePlainComment(),
    };

    onSave(payload);
  };

  const downloadExcel = () => {
    const data = [
      {
        Candidate: candidate?.candidateName,
        InterviewId: candidate?.interviewId,
        Rating: calculateAverage(),
        Recommendation: recommendation,
        Comments: generatePlainComment(),
      },
    ];

    const worksheet = XLSX.utils.json_to_sheet(data);
    const workbook = XLSX.utils.book_new();
    XLSX.utils.book_append_sheet(workbook, worksheet, "Evaluation");

    XLSX.writeFile(
      workbook,
      `${candidate?.candidateName}_evaluation.xlsx`
    );
  };

  const isViewMode = isSubmitted && !isEditMode;

  return (
    <div
      className={`fixed inset-y-0 right-0 w-[550px] bg-white shadow-2xl z-[70] transform transition-transform duration-300 flex flex-col ${
        isOpen ? "translate-x-0" : "translate-x-full"
      }`}
    >
      {/* HEADER */}
      <div className="p-6 border-b bg-[#101828] text-white flex justify-between items-center">
        <div>
          <h2 className="text-lg font-bold">
            {candidate?.candidateName}
          </h2>
          <span className="text-xs text-gray-300">
            {isSubmitted
              ? "View / Edit Evaluation"
              : "Submit Interview Evaluation"}
          </span>
        </div>

        <button
          onClick={onClose}
          className="p-2 hover:bg-white/10 rounded-full"
        >
          <X size={22} />
        </button>
      </div>

      {/* BODY */}
      <div className="flex-1 overflow-y-auto p-6 space-y-6">
        <MetricRow
          title="Core Concepts"
          fieldKey="coreConcepts"
          scores={scores}
          setScores={setScores}
          isViewMode={isViewMode}
        />
        <MetricRow
          title="Coding Skills"
          fieldKey="coding"
          scores={scores}
          setScores={setScores}
          isViewMode={isViewMode}
        />
        <MetricRow
          title="Communication"
          fieldKey="communication"
          scores={scores}
          setScores={setScores}
          isViewMode={isViewMode}
        />
        <MetricRow
          title="Behavioral"
          fieldKey="behavioral"
          scores={scores}
          setScores={setScores}
          isViewMode={isViewMode}
        />

        {/* Recommendation (UNCHANGED STYLING) */}
        <div className="p-6 rounded-3xl border shadow-lg">
          <h4 className="text-sm font-extrabold uppercase tracking-widest mb-6">
            Select Recommendation
          </h4>

          <div className="grid grid-cols-2 gap-6">
            {[
              {
                key: "MOVE TO NEXT",
                label: "Next Round",
                gradient:
                  "from-blue-500 via-indigo-500 to-purple-600",
                glow: "shadow-blue-400/40",
                enabled: hasNextRound,
              },
              {
                key: "ON HOLD",
                label: "On Hold",
                gradient:
                  "from-amber-400 via-orange-500 to-yellow-500",
                glow: "shadow-orange-400/40",
                enabled: true,
              },
              {
                key: "HIRE",
                label: "Hire",
                gradient:
                  "from-emerald-400 via-green-500 to-teal-600",
                glow: "shadow-green-400/40",
                enabled: !hasNextRound,
              },
              {
                key: "REJECT",
                label: "Reject",
                gradient:
                  "from-rose-400 via-red-500 to-pink-600",
                glow: "shadow-red-400/40",
                enabled: true,
              },
            ].map((item) => {
              const selected = recommendation === item.key;

              return (
                <button
                  key={item.key}
                  type="button"
                  disabled={isViewMode || !item.enabled}
                  onClick={() =>
                    setRecommendation(item.key)
                  }
                  className={`
                    relative rounded-2xl p-5 font-bold text-sm
                    flex items-center justify-center gap-3
                    transition-all duration-300 transform
                    ${
                      selected
                        ? `bg-gradient-to-r ${item.gradient} text-white shadow-2xl ${item.glow} scale-105`
                        : item.enabled && !isViewMode
                        ? `bg-gradient-to-r ${item.gradient} text-white opacity-80 hover:opacity-100 hover:shadow-xl hover:scale-105`
                        : "bg-gray-200 text-gray-400 cursor-not-allowed"
                    }
                  `}
                >
                  {item.label}

                  {selected && (
                    <div className="absolute inset-0 rounded-2xl ring-4 ring-white/30 animate-pulse"></div>
                  )}
                </button>
              );
            })}
          </div>
        </div>
      </div>

      {/* FOOTER */}
      <div className="p-6 border-t bg-gray-50 flex gap-4">
        <button
          onClick={downloadExcel}
          className="flex items-center gap-2 px-4 py-3 rounded-2xl font-bold text-sm bg-white border"
        >
          <Download size={16} /> Export Excel
        </button>

        {isSubmitted && !isEditMode && (
          <button
            onClick={() => setIsEditMode(true)}
            className="flex-1 py-3 rounded-2xl font-bold bg-blue-600 text-white flex items-center justify-center gap-2"
          >
            <Edit size={18} /> Edit Evaluation
          </button>
        )}

        {(!isSubmitted || isEditMode) && (
          <button
            onClick={handleSaveClick}
            className="flex-1 py-3 rounded-2xl font-bold bg-[#101828] text-white flex items-center justify-center gap-2"
          >
            <Save size={18} />
            {isSubmitted
              ? "Update Evaluation"
              : "Save Evaluation"}
          </button>
        )}
      </div>
    </div>
  );
};

export default EvaluationModel;
