import React, { useState, useEffect, useRef } from "react";
import {
  X,
  Star,
  Save,
  CheckCircle,
  XCircle,
  Clock,
  ArrowRightCircle,
  Download,
} from "lucide-react";
import * as XLSX from "xlsx";

/* ==============================
   STABLE COMMENT INPUT
================================ */
const StableCommentArea = ({
  value,
  onChange,
  placeholder,
  autoFocus,
}) => {
  const textareaRef = useRef(null);

  useEffect(() => {
    if (autoFocus && textareaRef.current) {
      const length = textareaRef.current.value.length;
      textareaRef.current.focus();
      textareaRef.current.setSelectionRange(length, length);
    }
  }, [value, autoFocus]);

  return (
    <textarea
      ref={textareaRef}
      className="w-full h-20 p-3 text-xs border border-gray-200 rounded-lg focus:border-[#007bff] outline-none resize-none transition-all"
      placeholder={placeholder}
      value={value}
      onChange={(e) => onChange(e.target.value)}
    />
  );
};

/* ==============================
   MAIN COMPONENT
================================ */

const EvaluationModal = ({
  isOpen,
  onClose,
  candidate,
  onSave, // 🔥 interviewer API handler
}) => {
  const [recommendation, setRecommendation] = useState("");
  const [activeField, setActiveField] = useState(null);

  const [scores, setScores] = useState({
    coreConcepts: { rating: 0, comment: "" },
    coding: { rating: 0, comment: "" },
    communication: { rating: 0, comment: "" },
    behavioral: { rating: 0, comment: "" },
  });

  /* ==============================
     RESET WHEN OPEN
  ================================= */
  useEffect(() => {
    if (isOpen) {
      setRecommendation("");
      setScores({
        coreConcepts: { rating: 0, comment: "" },
        coding: { rating: 0, comment: "" },
        communication: { rating: 0, comment: "" },
        behavioral: { rating: 0, comment: "" },
      });
    }
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
    return total / count;
  };

  /* ==============================
     EXCEL DOWNLOAD
  ================================= */
  const downloadExcel = () => {
    const data = [
      {
        Candidate: candidate.fullName,
        InterviewId: candidate.interviewId,
        CoreConcepts: scores.coreConcepts.rating,
        Coding: scores.coding.rating,
        Communication: scores.communication.rating,
        Behavioral: scores.behavioral.rating,
        Recommendation: recommendation,
      },
    ];

    const worksheet = XLSX.utils.json_to_sheet(data);
    const workbook = XLSX.utils.book_new();
    XLSX.utils.book_append_sheet(workbook, worksheet, "Evaluation");

    XLSX.writeFile(
      workbook,
      `${candidate.fullName}_evaluation.xlsx`
    );
  };

  /* ==============================
     SAVE HANDLER (UPDATED FOR YOUR BACKEND)
  ================================= */
  const handleSaveClick = () => {
    const techAvg = parseFloat(calculateAverage('technical'));
    const softAvg = parseFloat(calculateAverage('soft'));
    const finalScoreValue = (techAvg + softAvg) / 2;
  
    const normalizedRating =
      finalScoreValue > 0
        ? Math.round(finalScoreValue / 2)
        : 0;
  
    const payload = {
      interviewId: candidate.interviewId,
      rating: normalizedRating,
      recommendation: recommendation.toUpperCase(),
      comments: JSON.stringify(scores),
    };
  
    onSave(payload);
  };
  

  /* ==============================
     STAR RENDERER
  ================================= */
  const MetricRow = ({ title, fieldKey }) => (
    <div className="p-4 bg-gray-50 rounded-xl border border-gray-100 space-y-3">
      <div className="flex justify-between items-center">
        <h4 className="text-sm font-bold text-[#101828]">
          {title}
        </h4>
        <div className="flex gap-1">
          {[1, 2, 3, 4, 5].map((star) => (
            <button
              key={star}
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
        autoFocus={activeField === fieldKey}
        onChange={(val) => {
          setActiveField(fieldKey);
          setScores((prev) => ({
            ...prev,
            [fieldKey]: {
              ...prev[fieldKey],
              comment: val,
            },
          }));
        }}
      />
    </div>
  );

  /* ==============================
     UI
  ================================= */
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
            {candidate?.fullName}
          </h2>
          <span className="text-xs text-gray-400">
            Submit Interview Evaluation
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
        />
        <MetricRow title="Coding Skills" fieldKey="coding" />
        <MetricRow
          title="Communication"
          fieldKey="communication"
        />
        <MetricRow
          title="Behavioral"
          fieldKey="behavioral"
        />

        {/* Recommendation */}
        <div className="p-5 border rounded-2xl bg-white shadow-sm">
          <h4 className="text-[10px] font-black text-gray-400 uppercase tracking-widest mb-4">
            Select Recommendation
          </h4>

          <div className="grid grid-cols-2 gap-3">
            {[
              {
                key: "next_round",
                label: "NEXT ROUND",
                icon: <ArrowRightCircle size={16} />,
              },
              {
                key: "on_hold",
                label: "ON HOLD",
                icon: <Clock size={16} />,
              },
              {
                key: "hire",
                label: "HIRE",
                icon: <CheckCircle size={16} />,
              },
              {
                key: "reject",
                label: "REJECT",
                icon: <XCircle size={16} />,
              },
            ].map((item) => (
              <button
                key={item.key}
                onClick={() =>
                  setRecommendation(item.key)
                }
                className={`p-3 rounded-xl text-[10px] font-bold flex items-center justify-center gap-2 border-2 transition-all ${
                  recommendation === item.key
                    ? "border-blue-500 bg-blue-50 text-blue-700"
                    : "border-gray-50 text-gray-400"
                }`}
              >
                {item.icon} {item.label}
              </button>
            ))}
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

        <button
          onClick={handleSaveClick}
          className="flex-1 py-3 rounded-2xl font-bold bg-[#101828] text-white flex items-center justify-center gap-2 hover:bg-black"
        >
          <Save size={18} /> Save Evaluation
        </button>
      </div>
    </div>
  );
};

export default EvaluationModal;
