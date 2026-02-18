import JobActionsMenu from "./JobActionsMenu";
import { buildJobCounts } from "./JobCountsUtil";
import { useNavigate } from "react-router-dom";

export default function JobCard({
  job,
  allCandidates,
  pipeline,
  rounds,
  onJobDeleted,
  onJobClosed,
}) {
  const counts = buildJobCounts({
    allCandidates,
    pipeline,
    rounds,
  });

  const navigate = useNavigate();

  const isClosed = job.status === "CLOSED";

  return (
    <div
      className="bg-white rounded-lg shadow border relative cursor-pointer hover:shadow-md transition"
      onClick={() => navigate(`/recruiter/interviews/${job.id}`)}
    >
      {/* Header */}
      <div className="flex justify-between p-6">
        <div>
          <div className="flex items-center gap-3">
            <h2 className="text-xl font-semibold">{job.title}</h2>

            {/* ✅ STATUS BADGE */}
            <span
              className={`text-xs font-semibold px-2 py-1 rounded ${
                isClosed
                  ? "bg-red-100 text-red-700"
                  : "bg-green-100 text-green-700"
              }`}
            >
              {job.status}
            </span>
          </div>

          <p className="text-sm text-gray-500 mt-1">
            BXA-{String(job.id).padStart(4, "0")}
          </p>

          <p className="text-sm text-gray-600 mt-2">
            {job.location} · {job.experienceMin}-{job.experienceMax} yrs
          </p>
        </div>

        {/* ACTIONS */}
        <JobActionsMenu
          jobId={job.id}
          jobStatus={job.status}
          onJobDeleted={onJobDeleted}
          onJobClosed={onJobClosed}
        />
      </div>

      {/* COUNTS */}
      <div className="flex justify-between px-10 py-6 text-center">
        <CountItem label="Pool" value={counts.pool} />
        <CountItem label="Shortlist" value={counts.shortlisted} />

        {counts.rounds.map((r) => (
          <CountItem key={r.name} label={r.name} value={r.count} />
        ))}

        <CountItem label="Hired" value={counts.hired} />
        <CountItem label="Offer" value={counts.offer} />
        <CountItem label="Rejected" value={counts.rejected} />
      </div>

      {/* Bottom Border */}
      <div className="h-1 bg-blue-500 rounded-b-lg" />
    </div>
  );
}

function CountItem({ label, value }) {
  return (
    <div>
      <div className="text-3xl font-semibold text-gray-800">{value}</div>
      <div className="text-sm text-gray-500 mt-1">{label}</div>
    </div>
  );
}
