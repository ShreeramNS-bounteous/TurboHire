import { useEffect, useState } from "react";
import { getBusinessUnits } from "../../api/businessUnits.api";
import api from "../../api/axios";
import { useNavigate } from "react-router-dom";

export default function CreateJob() {
  const navigate = useNavigate();

  const [businessUnits, setBusinessUnits] = useState([]);
  const [loading, setLoading] = useState(false);

  const [form, setForm] = useState({
    title: "",
    location: "",
    department: "",
    experienceMin: "",
    experienceMax: "",
    buId: "",
  });

  // 🔹 ROUNDS STATE
  const [rounds, setRounds] = useState([
    { roundName: "", roundOrder: 1 },
  ]);

  useEffect(() => {
    getBusinessUnits().then(setBusinessUnits);
  }, []);

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!form.title || !form.buId) {
      alert("Title and Business Unit are required");
      return;
    }

    try {
      setLoading(true);

      // 1️⃣ CREATE JOB
      const jobRes = await api.post("/api/jobs", {
        title: form.title,
        location: form.location,
        department: form.department,
        experienceMin: Number(form.experienceMin),
        experienceMax: Number(form.experienceMax),
        buId: Number(form.buId),
      });

      const jobId = jobRes.data.id;

      // 2️⃣ CREATE ROUNDS (THIS WAS MISSING ❌)
      await Promise.all(
        rounds
          .filter((r) => r.roundName.trim())
          .map((r, index) =>
          api.post(`/api/jobs/${jobId}/rounds`, {
            roundName: r.roundName,
            roundOrder: index + 1,
            evaluationTemplateCode: r.evaluationTemplateCode,
          })
          )
      );

      navigate("/recruiter/jobs");
    } catch (err) {
      console.error("Create job failed", err);
      alert("Failed to create job");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div>
      <h1 className="text-2xl font-semibold mb-4">Create Job</h1>

      <form
        onSubmit={handleSubmit}
        className="bg-white p-6 rounded shadow max-w-xl"
      >
        {/* Job Title */}
        <input
          name="title"
          placeholder="Job Title"
          value={form.title}
          onChange={handleChange}
          className="w-full border p-2 rounded mb-4"
        />

        {/* Business Unit */}
        <select
          name="buId"
          value={form.buId}
          onChange={handleChange}
          className="w-full border p-2 rounded mb-4"
        >
          <option value="">Select Business Unit</option>
          {businessUnits.map((bu) => (
            <option key={bu.id} value={bu.id}>
              {bu.name}
            </option>
          ))}
        </select>

        {/* Location */}
        <input
          name="location"
          placeholder="Location"
          value={form.location}
          onChange={handleChange}
          className="w-full border p-2 rounded mb-4"
        />

        {/* Experience */}
        <div className="grid grid-cols-2 gap-4 mb-4">
          <input
            type="number"
            name="experienceMin"
            placeholder="Min Experience"
            value={form.experienceMin}
            onChange={handleChange}
            className="border p-2 rounded"
          />
          <input
            type="number"
            name="experienceMax"
            placeholder="Max Experience"
            value={form.experienceMax}
            onChange={handleChange}
            className="border p-2 rounded"
          />
        </div>

        {/* Interview Rounds */}
        <div className="mb-6">
          <p className="font-medium mb-2">Interview Rounds</p>

          {rounds.map((r, index) => (
            <div key={index} className="flex gap-3 mb-2">
              <input
                placeholder={`Round ${index + 1}`}
                value={r.roundName}
                onChange={(e) => {
                  const copy = [...rounds];
                  copy[index].roundName = e.target.value;
                  setRounds(copy);
                }}
                className="flex-1 border p-2 rounded"
              />

              {rounds.length > 1 && (
                <button
                  type="button"
                  onClick={() =>
                    setRounds(rounds.filter((_, i) => i !== index))
                  }
                  className="text-red-600 text-sm"
                >
                  Remove
                </button>
              )}
            </div>
          ))}

          <button
            type="button"
            onClick={() =>
              setRounds([
                ...rounds,
                { roundName: "", roundOrder: rounds.length + 1 },
              ])
            }
            className="text-blue-600 text-sm mt-2"
          >
            + Add Round
          </button>
        </div>

        {/* Actions */}
        <div className="flex gap-4">
          <button
            type="submit"
            disabled={loading}
            className="bg-blue-600 text-white px-4 py-2 rounded"
          >
            {loading ? "Creating..." : "Create Job"}
          </button>

          <button
            type="button"
            onClick={() => navigate("/recruiter/jobs")}
            className="border px-4 py-2 rounded"
          >
            Cancel
          </button>
        </div>
      </form>
    </div>
  );
}
