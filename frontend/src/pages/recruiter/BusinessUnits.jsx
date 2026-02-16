import { useEffect, useState } from "react";
import {
  getBusinessUnits,
  createBusinessUnit,
} from "../../api/businessUnits.api";
import { getJobs } from "../../api/jobs.api";

export default function BusinessUnits() {
  const [units, setUnits] = useState([]);
  const [jobs, setJobs] = useState([]);
  const [name, setName] = useState("");
  const [loading, setLoading] = useState(false);

  const loadData = async () => {
    try {
      const [buData, jobData] = await Promise.all([
        getBusinessUnits(),
        getJobs(),
      ]);

      setUnits(buData);
      setJobs(jobData);
    } catch (err) {
      console.error("Failed to load business units or jobs", err);
    }
  };

  useEffect(() => {
    loadData();
  }, []);

  const handleCreate = async () => {
    if (!name.trim()) return;

    try {
      setLoading(true);
      await createBusinessUnit(name);
      setName("");
      loadData();
    } catch (err) {
      alert("Failed to create business unit");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div>
      <h1 className="text-2xl font-semibold mb-4">Business Units</h1>

      {/* Create Business Unit */}
      <div className="bg-white p-4 rounded shadow mb-6 flex gap-4">
        <input
          className="border p-2 flex-1 rounded"
          placeholder="Business Unit Name"
          value={name}
          onChange={(e) => setName(e.target.value)}
        />
        <button
          onClick={handleCreate}
          disabled={loading}
          className="bg-blue-600 text-white px-4 rounded"
        >
          {loading ? "Creating..." : "Add"}
        </button>
      </div>

      {/* Business Units Table */}
      <div className="bg-white rounded shadow">
        <table className="w-full">
          <thead className="border-b">
            <tr>
              <th className="text-left p-4 w-20">ID</th>
              <th className="text-left p-4">Business Unit</th>
              <th className="text-right p-4 w-32">Openings</th>
            </tr>
          </thead>
          <tbody>
            {units.map((bu) => {
              const openCount = jobs.filter(
                (job) =>
                  (job.buId === bu.id || job.bu_id === bu.id) &&
                  job.status === "OPEN"
              ).length;

              return (
                <tr key={bu.id} className="border-b">
                  <td className="p-4">{bu.id}</td>
                  <td className="p-4 font-medium">{bu.name}</td>
                  <td className="p-4 text-right font-semibold">
                    {openCount}
                  </td>
                </tr>
              );
            })}
          </tbody>
        </table>

        {units.length === 0 && (
          <p className="p-4 text-gray-500">No business units found.</p>
        )}
      </div>
    </div>
  );
}
