import { useEffect, useState } from "react";
import {
  getMyAvailabilitySlots,
  addAvailabilitySlot,
  deleteAvailabilitySlot,
} from "../../api/interviewerAvailability.api";
import { useAuth } from "../../auth/AuthContext"; // adjust path if needed

export default function Availability() {
  const { user } = useAuth(); // 👈 logged-in user
  const userId = user?.userId;

  const [slots, setSlots] = useState([]);
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);

  const [form, setForm] = useState({
    date: "",
    startTime: "",
    endTime: "",
  });

  const loadSlots = async () => {
    if (!user) return;
  
    try {
      const data = await getMyAvailabilitySlots(user.userId);
      setSlots(data || []);
    } finally {
      setLoading(false);
    }
  };
  

  useEffect(() => {
    loadSlots();
  }, [userId]);

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleAddSlot = async (e) => {
    e.preventDefault();
    console.log("AUTH USER:", user);

    if (!userId) {
      alert("User not loaded yet. Please refresh.");
      return;
    }
  
    if (!form.date || !form.startTime || !form.endTime) {
      alert("All fields are required");
      return;
    }
  
    if (form.startTime >= form.endTime) {
      alert("End time must be after start time");
      return;
    }
  
    try {
      setSaving(true);
      await addAvailabilitySlot(user.userId, {
        slotDate: form.date,
        startTime: form.startTime,
        endTime: form.endTime,
      });      
      setForm({ date: "", startTime: "", endTime: "" });
      loadSlots();
    } catch (err) {
      alert("Failed to add slot");
    } finally {
      setSaving(false);
    }
  };
  
  const handleDelete = async (slotId) => {
    if (!window.confirm("Delete this slot?")) return;
    await deleteAvailabilitySlot(slotId, userId);
    loadSlots();
  };

    return (
      
    <div className="max-w-3xl">
      <h1 className="text-2xl font-semibold mb-6">
        My Availability
      </h1>

      {/* ADD SLOT */}
      <form
        onSubmit={handleAddSlot}
        className="bg-white p-6 rounded shadow mb-8 space-y-4"
      >
        <div className="grid grid-cols-3 gap-4">
          <input
            type="date"
            name="date"
            value={form.date}
            onChange={handleChange}
            className="border p-2 rounded"
          />
          <input
            type="time"
            name="startTime"
            value={form.startTime}
            onChange={handleChange}
            className="border p-2 rounded"
          />
          <input
            type="time"
            name="endTime"
            value={form.endTime}
            onChange={handleChange}
            className="border p-2 rounded"
          />
        </div>

        <button
  type="submit"
  disabled={saving || !userId}
  className="bg-blue-600 text-white px-4 py-2 rounded disabled:opacity-50"
>
  {saving ? "Saving..." : "Add Slot"}
</button>
      </form>

      {/* SLOT LIST */}
      <div className="bg-white rounded shadow">
        <table className="w-full">
          <thead className="border-b">
            <tr>
              <th className="text-left p-4">Date</th>
              <th className="text-left p-4">Start</th>
              <th className="text-left p-4">End</th>
              <th className="text-right p-4">Action</th>
            </tr>
          </thead>
          <tbody>
            {loading && (
              <tr>
                <td colSpan="4" className="p-4 text-center text-gray-500">
                  Loading slots...
                </td>
              </tr>
            )}

            {!loading && slots.length === 0 && (
              <tr>
                <td colSpan="4" className="p-4 text-center text-gray-500">
                  No availability added
                </td>
              </tr>
            )}

            {slots.map((slot) => (
              <tr key={slot.id} className="border-b">
                <td className="p-4">{slot.slotDate}</td>
                <td className="p-4">{slot.startTime}</td>
                <td className="p-4">{slot.endTime}</td>
                <td className="p-4 text-right">
                  <button
                    onClick={() => handleDelete(slot.id)}
                    className="text-red-600 font-medium"
                  >
                    Delete
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}
