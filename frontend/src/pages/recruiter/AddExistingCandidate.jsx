import { useEffect, useState } from "react";
import api from "../../api/axios";
import toast from "react-hot-toast";


export default function AddExistingCandidate({
  job,
  onClose,
  onSuccess,
  autoSelectId,
}) {
  const [candidates, setCandidates] = useState([]);
  const [selectedId, setSelectedId] = useState(null);
  const [loading, setLoading] = useState(false);
  const [search, setSearch] = useState("");

  const loadCandidates = async () => {
    try {
      const res = await api.get(`/api/candidates/available/${job.id}`);
      setCandidates(res.data);
    } catch (err) {
      toast.error("Failed to load candidates", err);
    }
  };

  useEffect(() => {
    if (job) loadCandidates();
  }, [job]);

  // 🔥 auto select new candidate
  useEffect(() => {
    if (autoSelectId) {
      setSelectedId(autoSelectId);
    }
  }, [autoSelectId]);

  const filteredCandidates = candidates.filter(
    (c) =>
      c.fullName.toLowerCase().includes(search.toLowerCase()) ||
      c.email.toLowerCase().includes(search.toLowerCase())
  );

  const handleAddToPipeline = async () => {
    if (!selectedId) {
      toast.error("Please select a candidate");
      return;
    }

    setLoading(true);

    try {
      await api.post("/api/pipeline", {
        candidateId: selectedId,
        jobId: job.id,
        buId: job.buId,
      });

      toast.success("Candidate added to pipeline 🎉");


      await loadCandidates(); // 🔥 refresh list

      onSuccess?.();
      onClose?.();
    } catch (err){
      if (err.response?.status === 409) {
        toast.error("Candidate already in pipeline");
      } else {
        toast.error("Failed to add candidate");
      }
    } finally {
      setLoading(false);
    }
  };

  return (
    <div>
      <h2 className="text-xl font-semibold mb-4">
        Add Existing Candidate
      </h2>

      <input
        placeholder="Search by name or email"
        value={search}
        onChange={(e) => setSearch(e.target.value)}
        className="w-full border p-2 rounded mb-4"
      />

      <div className="border rounded max-h-96 overflow-y-auto">
        {filteredCandidates.map((c) => (
          <label
            key={c.id}
            className={`flex items-center gap-3 p-3 border-b cursor-pointer ${
              selectedId === c.id
                ? "bg-blue-50"
                : "hover:bg-gray-50"
            }`}
          >
            <input
              type="radio"
              checked={selectedId === c.id}
              onChange={() => setSelectedId(c.id)}
            />
            <div>
              <p className="font-medium">{c.fullName}</p>
              <p className="text-sm text-gray-500">{c.email}</p>
            </div>
          </label>
        ))}
      </div>

      <div className="flex gap-4 mt-6">
        <button
          onClick={handleAddToPipeline}
          disabled={loading}
          className="bg-blue-600 text-white px-4 py-2 rounded"
        >
          {loading ? "Adding..." : "Add to Pipeline"}
        </button>

        <button
          onClick={onClose}
          className="border px-4 py-2 rounded"
        >
          Cancel
        </button>
      </div>
    </div>
  );
}
