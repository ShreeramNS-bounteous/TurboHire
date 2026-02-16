export default function InterviewTabs({ activeTab, onChange }) {
  return (
    <div className="tabs">
      <button
        className={`tab ${activeTab === "PENDING" ? "active" : ""}`}
        onClick={() => onChange("PENDING")}
      >
        To Be Scheduled
      </button>

      <button
        className={`tab ${activeTab === "SCHEDULED" ? "active" : ""}`}
        onClick={() => onChange("SCHEDULED")}
      >
        Scheduled
      </button>

      <button
        className={`tab ${activeTab === "COMPLETED" ? "active" : ""}`}
        onClick={() => onChange("COMPLETED")}
      >
        Completed
      </button>
    </div>
  );
}
