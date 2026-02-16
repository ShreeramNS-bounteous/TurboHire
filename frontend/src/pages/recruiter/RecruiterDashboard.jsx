import { useEffect, useState } from "react";
import { getJobs } from "../../api/jobs.api";

export default function RecruiterDashboard() {
  const [openJobsCount, setOpenJobsCount] = useState(0);

  useEffect(() => {
    const loadOpenJobs = async () => {
      try {
        const jobs = await getJobs();

        const openJobs = jobs.filter(
          (job) => job.status === "OPEN"
        ).length;

        setOpenJobsCount(openJobs);
      } catch (err) {
        console.error("Failed to load jobs", err);
      }
    };

    loadOpenJobs();
  }, []);

  return (
    <div>
      <h1 className="text-2xl font-semibold mb-4">Dashboard</h1>

      <div className="grid grid-cols-3 gap-4">
        {/* Open Jobs */}
        <div className="bg-white p-4 rounded shadow">
          <p className="text-sm text-gray-500">Open Jobs</p>
          <p className="text-2xl font-bold">{openJobsCount}</p>
        </div>

        {/* Active Candidates (later) */}
        <div className="bg-white p-4 rounded shadow">
          <p className="text-sm text-gray-500">Active Candidates</p>
          <p className="text-2xl font-bold">—</p>
        </div>

        {/* Interviews Today (later) */}
        <div className="bg-white p-4 rounded shadow">
          <p className="text-sm text-gray-500">Interviews Today</p>
          <p className="text-2xl font-bold">—</p>
        </div>
      </div>
    </div>
  );
}
