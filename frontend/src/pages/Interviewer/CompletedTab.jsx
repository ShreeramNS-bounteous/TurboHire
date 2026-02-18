import InterviewSummaryCard from "../../components/InterviewSummaryCard";

export default function CompletedTab({ interviews, loading }) {

  if (loading) {
    return (
      <div className="p-8 text-gray-500">
        Loading interviews...
      </div>
    );
  }

  if (!interviews || interviews.length === 0) {
    return (
      <div className="p-8 text-gray-500">
        No completed interviews.
      </div>
    );
  }

  return (
    <div className="p-8 space-y-6">
      {interviews.map((intv) => (
        <InterviewSummaryCard
          key={intv.interviewId}
          interview={intv}
        />
      ))}
    </div>
  );
}
