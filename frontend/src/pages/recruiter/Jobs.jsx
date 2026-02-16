import { useEffect, useState } from "react";
import {
  getJobs,
  publishJob,
  closeJob,
} from "../../api/jobs.api";
import { getBusinessUnits } from "../../api/businessUnits.api";
import { useNavigate } from "react-router-dom";

export default function Jobs() {
  const navigate = useNavigate();
  const [jobs, setJobs] = useState([]);
  const [businessUnits, setBusinessUnits] = useState([]);
  const [loading, setLoading] = useState(true);

  const loadData = async () => {
    try {
      const [jobData, buData] = await Promise.all([
        getJobs(),
        getBusinessUnits(),
      ]);

      setJobs(jobData);
      setBusinessUnits(buData);
    } catch (err) {
      console.error("Failed to load jobs", err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadData();
  }, []);

  const getBuName = (job) => {
    const buId = job.buId || job.bu_id;
    const bu = businessUnits.find((b) => b.id === buId);
    return bu ? bu.name : "-";
  };

  const handlePublish = async (jobId) => {
    await publishJob(jobId);
    loadData();
  };

  const handleClose = async (jobId) => {
    await closeJob(jobId);
    loadData();
  };

  if (loading) {
    return <p>Loading jobs...</p>;
  }

  return (
    <div>
      {/* Header */}
      <div className="flex items-center justify-between mb-4">
        <h1 className="text-2xl font-semibold">Jobs</h1>

        <button
          onClick={() => navigate("/recruiter/jobs/create")}
          className="bg-blue-600 text-white px-4 py-2 rounded"
        >
          + Create Job
        </button>
      </div>

      {/* Jobs Table */}
      <div className="bg-white rounded shadow">
        <table className="w-full">
          <thead className="border-b">
            <tr>
              <th className="text-left p-4">Title</th>
              <th className="text-left p-4">Business Unit</th>
              <th className="text-left p-4">Location</th>
              <th className="text-left p-4">Status</th>
              <th className="text-right p-4">Actions</th>
            </tr>
          </thead>
          <tbody>
            {jobs.map((job) => (
              <tr key={job.id} className="border-b">
                <td className="p-4 font-medium">{job.title}</td>
                <td className="p-4">{getBuName(job)}</td>
                <td className="p-4">{job.location}</td>
                <td className="p-4 font-semibold">{job.status}</td>
                <td className="p-4 text-right space-x-2">
                  {job.status === "DRAFT" && (
                    <button
                      onClick={() => handlePublish(job.id)}
                      className="text-green-600 font-medium"
                    >
                      Publish
                    </button>
                  )}

                  {job.status === "OPEN" && (
                    <button
                      onClick={() => handleClose(job.id)}
                      className="text-red-600 font-medium"
                    >
                      Close
                    </button>
                  )}
                </td>
              </tr>
            ))}

            {jobs.length === 0 && (
              <tr>
                <td colSpan="5" className="p-4 text-gray-500">
                  No jobs found
                </td>
              </tr>
            )}
          </tbody>
        </table>
      </div>
    </div>
  );
}
