import { useState } from "react";
import api from "../../api/axios";
import toast from "react-hot-toast";


// Convert file → base64
const fileToBase64 = (file) =>
  new Promise((resolve, reject) => {
    const reader = new FileReader();
    reader.onload = () =>
      resolve(reader.result.split(",")[1]); // remove data prefix
    reader.onerror = reject;
    reader.readAsDataURL(file);
  });

  export default function AddNewCandidate({ job, onCreated }) {
    const [loading, setLoading] = useState(false);
    const [resumeFile, setResumeFile] = useState(null);
  
    const [form, setForm] = useState({
      fullName: "",
      email: "",
      phone: "",
    });
  
    const handleChange = (e) => {
      setForm({ ...form, [e.target.name]: e.target.value });
    };
  
    const handleFileChange = (e) => {
      const file = e.target.files[0];
      if (!file) return;
      if (file.type !== "application/pdf") {
        alert("Only PDF files are allowed");
        return;
      }
      setResumeFile(file);
    };
  
    const handleSubmit = async (e) => {
      e.preventDefault();
  
      if (!resumeFile || !form.fullName || !form.email) {
        toast.error("Resume, Full Name and Email are required");
        return;
      }
  
      try {
        setLoading(true);
  
        const resumeBase64 = await fileToBase64(resumeFile);
  
        const res = await api.post("/api/candidates", {
          fullName: form.fullName,
          email: form.email,
          phone: form.phone,
          source: "Manual",
          fileName: resumeFile.name,
          resumePdf: resumeBase64,
        });
  
        const createdCandidate = res.data;
        toast.success("Candidate created successfully ✅");
  
        // 🔥 trigger auto switch
        onCreated?.(createdCandidate.id);
  
        // reset
        setForm({ fullName: "", email: "", phone: "" });
        setResumeFile(null);
      } catch (err) {
        if (err.response?.status === 409) {
          toast.error("Candidate already exists. Use Add Existing.");
        } else {
          toast.error("Failed to create candidate");
        }
      } finally {
        setLoading(false);
      }
    };
  
    return (
      <div className="max-w-xl">
        <h1 className="text-2xl font-semibold mb-6">Add New Candidate</h1>
  
        <form onSubmit={handleSubmit} className="bg-white p-6 rounded shadow">
          <label className="block mb-6">
            <div className="border-2 border-dashed rounded p-6 text-center cursor-pointer hover:bg-gray-50">
              <p className="font-medium">
                {resumeFile
                  ? resumeFile.name
                  : "Drag & drop resume here or click to upload"}
              </p>
              <p className="text-sm text-gray-500">PDF only</p>
            </div>
            <input
              type="file"
              accept=".pdf"
              className="hidden"
              onChange={handleFileChange}
            />
          </label>
  
          <div className="space-y-4">
            <input
              name="fullName"
              placeholder="Full Name *"
              value={form.fullName}
              onChange={handleChange}
              className="w-full border p-2 rounded"
            />
            <input
              name="email"
              type="email"
              placeholder="Email *"
              value={form.email}
              onChange={handleChange}
              className="w-full border p-2 rounded"
            />
            <input
              name="phone"
              placeholder="Phone (optional)"
              value={form.phone}
              onChange={handleChange}
              className="w-full border p-2 rounded"
            />
          </div>
  
          <div className="flex gap-4 mt-6">
            <button
              type="submit"
              disabled={loading}
              className={`px-4 py-2 rounded text-white ${
                loading ? "bg-gray-400" : "bg-blue-600 hover:bg-blue-700"
              }`}
            >
              {loading ? "Creating..." : "Create Candidate"}
            </button>
          </div>
        </form>
      </div>
    );
  }
  