import { useEffect, useState } from "react";
import api from "../../api/axios";
import { getJobs } from "../../api/jobs.api";

import RightDrawer from "../../components/RightDrawer";
import AddNewCandidate from "./AddNewCandidate";
import AddExistingCandidate from "./AddExistingCandidate";

const STAGES = ["SHORTLISTED", "INTERVIEW", "OFFER", "HIRED"];

// Backend → UI stage mapping
const mapStageToPipeline = (backendStage) => {
  if (!backendStage) return null;

  if (backendStage === "SHORTLISTED") return "SHORTLISTED";
  if (backendStage.startsWith("ROUND") || backendStage === "INTERVIEW")
    return "INTERVIEW";
  if (backendStage === "OFFER") return "OFFER";
  if (backendStage === "HIRED") return "HIRED";

  return null;
};

export default function Pipeline() {
  const [jobs, setJobs] = useState([]);
  const [selectedJob, setSelectedJob] = useState(null);

  const [pipeline, setPipeline] = useState([]);
  const [loading, setLoading] = useState(false);

  const [showOptions, setShowOptions] = useState(false);
  const [openCreateDrawer, setOpenCreateDrawer] = useState(false);
  const [openExistingDrawer, setOpenExistingDrawer] = useState(false);

  // Load open jobs
  useEffect(() => {
    const loadJobs = async () => {
      const data = await getJobs();
      setJobs(data.filter((j) => j.status === "OPEN"));
    };
    loadJobs();
  }, []);

  // Load pipeline for job
  const loadPipeline = async (jobId) => {
    setLoading(true);
    try {
      const res = await api.get(`/api/pipeline/job/${jobId}`);

      const normalized = res.data.map((p) => ({
        ...p,
        pipelineStage: mapStageToPipeline(p.currentStage),
      }));

      setPipeline(normalized);
    } catch (err) {
      console.error("Failed to load pipeline", err);
    } finally {
      setLoading(false);
    }
  };

  const handleJobChange = (e) => {
    const job = jobs.find((j) => j.id === Number(e.target.value));
    setSelectedJob(job || null);
    setShowOptions(false);

    if (job) loadPipeline(job.id);
    else setPipeline([]);
  };

  const getCandidatesByStage = (stage) =>
    pipeline.filter((p) => p.pipelineStage === stage);

  return (
    <div>
      {/* Header */}
      <div className="flex items-center justify-between mb-4">
        <h1 className="text-2xl font-semibold">Pipeline</h1>

        {selectedJob && (
          <button
            onClick={() => setShowOptions(!showOptions)}
            className="bg-blue-600 text-white px-4 py-2 rounded"
          >
            + Add Candidate
          </button>
        )}
      </div>

      {/* Job Select */}
      <div className="mb-6 max-w-md">
        <select
          value={selectedJob?.id || ""}
          onChange={handleJobChange}
          className="w-full border p-2 rounded"
        >
          <option value="">Select Job</option>
          {jobs.map((job) => (
            <option key={job.id} value={job.id}>
              {job.title}
            </option>
          ))}
        </select>
      </div>

      {/* Add options */}
      {showOptions && (
        <div className="mb-6 flex gap-4">
          <button
            onClick={() => {
              setOpenCreateDrawer(true);
              setShowOptions(false);
            }}
            className="border px-4 py-2 rounded"
          >
            + Create New Candidate
          </button>

          <button
            onClick={() => {
              setOpenExistingDrawer(true);
              setShowOptions(false);
            }}
            className="border px-4 py-2 rounded"
          >
            + Add Existing Candidate
          </button>
        </div>
      )}

      {/* Pipeline board */}
      {loading && <p>Loading pipeline...</p>}

      {!loading && selectedJob && (
        <div className="grid grid-cols-4 gap-4">
          {STAGES.map((stage) => {
            const candidates = getCandidatesByStage(stage);

            return (
              <div key={stage} className="bg-gray-100 rounded p-3">
                <h3 className="font-medium mb-2 text-center">{stage}</h3>

                <div className="space-y-2">
                  {candidates.map((item) => (
                    <div
                      key={item.id} // ✅ FIXED key warning
                      className="bg-white p-2 rounded shadow text-sm"
                    >
                      {item.candidateName}
                    </div>
                  ))}

                  {candidates.length === 0 && (
                    <p className="text-xs text-gray-500 text-center">
                      No candidates
                    </p>
                  )}
                </div>
              </div>
            );
          })}
        </div>
      )}

      {/* ========================= */}
      {/* Create Candidate Drawer */}
      {/* ========================= */}
      <RightDrawer
        open={openCreateDrawer}
        onClose={() => setOpenCreateDrawer(false)}
        title="Create Candidate"
      >
        <AddNewCandidate
          onSuccess={() => {
            setOpenCreateDrawer(false);
            alert(
              "Candidate created successfully. Now use 'Add Existing Candidate' to add them to the pipeline."
            );
          }}
        />
      </RightDrawer>

      {/* ============================== */}
      {/* Add Existing Candidate Drawer */}
      {/* ============================== */}
      <RightDrawer
        open={openExistingDrawer}
        onClose={() => setOpenExistingDrawer(false)}
        title="Add Existing Candidate"
      >
        {selectedJob && (
          <AddExistingCandidate
            job={selectedJob}
            onSuccess={() => {
              setOpenExistingDrawer(false);
              loadPipeline(selectedJob.id);
            }}
          />
        )}
      </RightDrawer>
    </div>
  );
}
