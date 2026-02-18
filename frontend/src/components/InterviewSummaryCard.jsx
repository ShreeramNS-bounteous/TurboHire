import { useNavigate } from "react-router-dom";
import { ExternalLink } from "lucide-react";

export default function InterviewSummaryCard({ interview }) {
  const navigate = useNavigate();

  const handleClick = () => {
    navigate(`/interviewer/interview/${interview.interviewId}`, {
        state: { interview }
      });
  };
  
  

  return (
    <div
      onClick={handleClick}
      className="bg-white border border-gray-200 rounded-2xl p-6 flex justify-between items-center hover:shadow-md transition cursor-pointer"
    >
      <div className="flex items-center gap-4">
        <div className="h-12 w-12 bg-orange-500 text-white rounded-xl flex items-center justify-center text-lg font-bold">
          {interview.candidateName?.charAt(0)}
        </div>

        <div>
          <h3 className="font-bold text-[#101828] text-lg">
            {interview.candidateName}
          </h3>

          <p className="text-sm text-gray-500">
            {interview.jobTitle} • {interview.roundName}
          </p>

          <p className="text-xs text-gray-400 mt-1">
            {interview.slotDate} • {interview.startTime} – {interview.endTime}
          </p>
        </div>
      </div>

      <div className="flex flex-col items-end gap-2">
        {interview.status === "SCHEDULED" && (
          <a
            href={interview.meetingUrl}
            target="_blank"
            rel="noreferrer"
            onClick={(e) => e.stopPropagation()}
            className="bg-[#101828] text-white px-4 py-2 rounded-xl text-xs font-bold hover:bg-black transition"
          >
            Join
          </a>
        )}

        <span className="text-xs font-semibold text-blue-600 flex items-center gap-1">
          View Details <ExternalLink size={12} />
        </span>
      </div>
    </div>
  );
}
