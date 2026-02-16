import { useState } from "react";
import api from "../../../api/axios";

export default function EditJobDrawer({ job, onSaved }) {
  const [title, setTitle] = useState(job.title);
  const [location, setLocation] = useState(job.location);
  const [experienceMin, setExperienceMin] = useState(job.experienceMin);
  const [experienceMax, setExperienceMax] = useState(job.experienceMax);
  const [loading, setLoading] = useState(false);

  const handleSave = async () => {
    try {
      setLoading(true);
      await api.put(`/api/jobs/${job.id}`, {
        title,
        location,
        experienceMin,
        experienceMax,
      });
      onSaved();
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="space-y-4">
      <div>
        <label className="text-sm font-medium">Job Title</label>
        <input
          className="border w-full px-3 py-2 rounded"
          value={title}
          onChange={(e) => setTitle(e.target.value)}
        />
      </div>

      <div>
        <label className="text-sm font-medium">Location</label>
        <input
          className="border w-full px-3 py-2 rounded"
          value={location}
          onChange={(e) => setLocation(e.target.value)}
        />
      </div>

      <div className="grid grid-cols-2 gap-4">
        <div>
          <label className="text-sm font-medium">Min Experience</label>
          <input
            type="number"
            className="border w-full px-3 py-2 rounded"
            value={experienceMin}
            onChange={(e) => setExperienceMin(e.target.value)}
          />
        </div>

        <div>
          <label className="text-sm font-medium">Max Experience</label>
          <input
            type="number"
            className="border w-full px-3 py-2 rounded"
            value={experienceMax}
            onChange={(e) => setExperienceMax(e.target.value)}
          />
        </div>
      </div>

      <button
        onClick={handleSave}
        disabled={loading}
        className="bg-blue-600 text-white px-4 py-2 rounded"
      >
        {loading ? "Saving..." : "Save Changes"}
      </button>
    </div>
  );
}
